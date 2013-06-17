package org.iserver.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.iserver.config.ServerDefine;
import org.iserver.data.Request;
import org.iserver.data.Response;
import org.iserver.manager.SessionManager;
import org.iserver.protocal.IDecoder;
import org.iserver.protocal.IEncoder;
import org.iserver.protocal.text.TextDecoder;
import org.iserver.protocal.text.TextEncoder;

/**
 * @author chenjie 2013-6-7
 */
public class IServer implements Runnable {

	private Selector selector;

	private IDataHandler dataHandler; // ���ݰ�����

	private SessionManager sessionManager; // �Ự����

	private Queue<SelectionKeyChangeReq> keyChangeReqList; // ����ı�ͨ���ĸ���Ȥ�¼�
	private Map<SocketChannel, Queue<byte[]>> pendingData; // ��д�������
	
	private IDecoder<Request> decoder;	//Э�����
	private IEncoder<Response> encoder; //Э�����

	public IServer() {

	}

	public void init() {
		this.sessionManager = SessionManager.getInstance();
		this.dataHandler = new DefaultDataHandler(this);

		this.keyChangeReqList = new LinkedBlockingQueue<SelectionKeyChangeReq>();
		this.pendingData = new ConcurrentHashMap<SocketChannel, Queue<byte[]>>();
		
		this.decoder = new TextDecoder();
		this.encoder = new TextEncoder();
	}

	public void bind(int port) {
		try {
			selector = Selector.open();

			ServerSocketChannel server = ServerSocketChannel.open();
			server.configureBlocking(false);
			server.socket().bind(new InetSocketAddress(port));
			server.socket().setReuseAddress(true);
			server.register(this.selector, SelectionKey.OP_ACCEPT);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * �ϲ���øýӿ�д������
	 * 
	 * @param channel
	 * @param data
	 */
	public void addWritableData(SocketChannel channel, byte[] data) {

		SelectionKeyChangeReq req = new SelectionKeyChangeReq(channel, SelectionKey.OP_WRITE);
		this.keyChangeReqList.add(req);

		Queue<byte[]> queue = this.pendingData.get(channel);
		if (queue == null) {
			queue = new LinkedBlockingQueue<byte[]>();
			this.pendingData.put(channel, queue);

		}

		queue.add(data);

		this.selector.wakeup(); // ֪ͨ���µ�ͨ����д
	}

	/**
	 * ע���µ�����
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void acceptNewSocket(SelectionKey key) throws IOException {
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		SocketChannel client = server.accept();
		client.configureBlocking(false);
		SelectionKey keySelected = client.register(this.selector, SelectionKey.OP_READ);

		this.sessionManager.addNewSession(client, keySelected);
	}

	/**
	 * ��ȡ����
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void read(ByteBuffer readBuffer, SelectionKey key) throws IOException {

		SocketChannel client = (SocketChannel) key.channel();
		try {
			readBuffer.clear();

			int size = client.read(readBuffer);

			if (size == -1) {
				key.channel().close();
				key.cancel();
				return;
			}

			if (this.dataHandler != null) {
				this.dataHandler.handleRead(client, readBuffer.array(), size);
			} else {
				// data handler not exists;
				System.out.println("The data handler not specified");
			}

		} catch (IOException e) {
			key.channel().close();
			key.cancel();
		}

	}

	/**
	 * д����
	 * 
	 * @param key
	 */
	private void write(ByteBuffer writeBuffer, SelectionKey key) {
		SocketChannel client = (SocketChannel) key.channel();
		Queue<byte[]> queue = this.pendingData.get(client);
		if (queue != null) {

			while (!queue.isEmpty()) {
				writeBuffer.clear();
				byte[] buffer = queue.poll();
				writeBuffer.put(buffer);
				writeBuffer.flip();
				try {
					client.write(writeBuffer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
		key.interestOps(SelectionKey.OP_READ);	//д����,������Ϊ�ɶ�

	}

	//ע�����Ȥ�¼�
	private void handleSelectionKeyChangeReqs() {

		while (!this.keyChangeReqList.isEmpty()) {

			SelectionKeyChangeReq req = this.keyChangeReqList.poll();

			SelectionKey key = req.getChannel().keyFor(this.selector);
			key.interestOps(req.getType());
		}

		this.keyChangeReqList.clear();
	}

	@Override
	public void run() {

		ByteBuffer writeBuffer = ByteBuffer.allocate(ServerDefine.WRITE_BUFFER_SIZE);
		ByteBuffer readBuffer = ByteBuffer.allocate(ServerDefine.READ_BUFFER_SIZE);
		while (true) {

			// handle the changed key op req;
			handleSelectionKeyChangeReqs();

			try {
				this.selector.select();

				Set<SelectionKey> keySet = this.selector.selectedKeys();
				Iterator<SelectionKey> keyItor = keySet.iterator();
				while (keyItor.hasNext()) {
					SelectionKey key = keyItor.next();
					keyItor.remove();

					if (!key.isValid()) {
						continue;
					}

					if (key.isAcceptable()) {

						this.acceptNewSocket(key);

					} else if (key.isReadable()) {

						this.read(readBuffer, key);
					}

					else if (key.isWritable()) {

						this.write(writeBuffer, key);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public final IDataHandler getDataHandler() {
		return dataHandler;
	}

	public final IDecoder<Request> getDecoder() {
		return decoder;
	}

	public final IEncoder<Response> getEncoder() {
		return encoder;
	}
	
	

}
