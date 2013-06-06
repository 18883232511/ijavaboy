package org.jdbc.async.template.async;

import java.util.concurrent.atomic.AtomicInteger;

import org.jdbc.async.event.UpdateEvent;
import org.jdbc.async.event.UpdateEventManager;
import org.jdbc.async.exception.SimpleSQLException;
import org.jdbc.async.pool.IPoolProvider;
import org.jdbc.async.utils.UpdateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * �첽���²���ִ���߳�
 * @author chenjie
 * 2012-12-1
 */
public class AsyncUpdater implements Runnable {
	private final static Logger logger = LoggerFactory.getLogger(AsyncUpdater.class);

	private final static Integer DEFAULT_UPDATE_INTERVAL = 1000; //ms;
	
	private final AtomicInteger threadId = new AtomicInteger();
	private int updateInterval = AsyncUpdater.DEFAULT_UPDATE_INTERVAL; //��������Ϊ��ʱ����updateInterval�ٴγ���
	
	private UpdateEventManager eventManager;
	private IPoolProvider poolProvider;
	
	/**
	 * ָ�����ӳ�
	 * @param provider
	 */
	public AsyncUpdater(IPoolProvider provider){
		this.eventManager = UpdateEventManager.getInstance();
		this.poolProvider = provider;
	}
	
	/**
	 * ָ�����ӳغ͸��¼��
	 * @param provider
	 * @param updateInterval
	 */
	public AsyncUpdater(IPoolProvider provider, int updateInterval){
		this(provider);
		this.updateInterval = updateInterval;
	}
	
	@Override
	public void run() {
		
		Thread.currentThread().setName("AsyncUpdater-"+threadId.incrementAndGet());
		
		while(true){
			
			UpdateEvent event = null;
			while((event = this.eventManager.readUpdateEvent()) != null){
				
				try {
					
					UpdateUtils.update(poolProvider, event.getSql(), event.getParams());
					
				} catch (SimpleSQLException e) {
					//�����ṩ��־��¼
					e.printStackTrace();
					
				}
			}
			
			try {
				logger.debug("The read queue is empty......" + System.currentTimeMillis());
				Thread.sleep(this.updateInterval);
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		
	}

	public final int getUpdateInterval() {
		return updateInterval;
	}

	public final void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	
}
