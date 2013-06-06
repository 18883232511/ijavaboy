package org.jdbc.async.pool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ���ӳ��ṩ�߽ӿ�
 * 
 * ���е����ӳ�ʵ����<code>BoneCPProvider</code>
 * 
 * @author chenjie
 * 2012-12-4
 */
public interface IPoolProvider {
	
	public void init() throws Exception;
	
	public void destroy();
	
	public Connection getConnection() throws SQLException;

}
