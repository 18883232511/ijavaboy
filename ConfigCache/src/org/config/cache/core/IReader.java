package org.config.cache.core;

import org.config.cache.exception.SimpleConfigException;

/**
 * �ļ���ȡ�ӿ�
 * @author chenjie
 * 2012-12-10
 */
public interface IReader {
	
	public String read(String path) throws SimpleConfigException;

}
