package org.jdbc.async.cache;

import java.util.LinkedList;
import java.util.List;

/**
 * ˫�������
 * 
 * �����̶߳����߳�д
 * 
 * ���߼��̶߳����п�ʱ��ͬ����������(IO�߳�����)�������������к�д����
 * @author chenjie
 * 2012-11-30
 */
public class DoubleCacheList<T> implements CacheList<T>{
	
	private final List<T> readQueue; //�߼��̶߳�����
	
	private final List<T> writeQueue; //IO�߳�д����
	
	private final Object readLock = new Object();
	private final Object writeLock = new Object();
	
	public DoubleCacheList(){
		//Collections.synchronizedList(new LinkedList<T>()); 
		this.readQueue = new LinkedList<T>();//new SynchronizedLinkedList<T>(new LinkedList<T>());
		
		//Collections.synchronizedList(new LinkedList<T>());
		this.writeQueue = new LinkedList<T>();//new SynchronizedLinkedList<T>(new LinkedList<T>());
	}
	
	@Override
	public T read(){
		
		synchronized (readLock) {
			
			if(this.readQueue.size() == 0){
				
				this.swap();
			}
			
			if(this.readQueue.size() > 0){
				
				return this.readQueue.remove(0);
			}
			
			return null;
		}
		
		
	}


	@Override
	public void write(T data){
		
		synchronized (writeLock) {
			
			this.writeQueue.add(data);
			
		}
		
		
	}
	
	/**
	 * ������������
	 */
	public void swap(){
		
		synchronized (writeLock) {
			
			this.readQueue.addAll(this.writeQueue);
			this.writeQueue.clear();
		}
	}
	
	
}
