package org.jdbc.async.cache;

/**
 * ����ӿ�
 * @author chenjie
 * 2012-11-30
 */
public interface CacheList<T> {
	
	public T read();
	
	public void write(T data);

}
