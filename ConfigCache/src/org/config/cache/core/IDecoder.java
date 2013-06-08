package org.config.cache.core;

import org.config.cache.exception.SimpleConfigException;

/**
 * ���ļ��е�ÿһ�е�λ���н���
 * @author chenjie
 * 2012-12-10
 */
public interface IDecoder<T extends IConfig> {

	/**
	 * �����json��ʽ����item��һ��json�ַ����磺{"name":"chenjie","id":"12"}��
	 * �����text��ʽ����item���ı��е�һ�У�ÿһ��Ԫ����\t�ָ�
	 * @param item
	 * @return
	 * @throws SimpleConfigException
	 */
	public T decode(String item) throws SimpleConfigException; 
}
