package org.jdbc.async.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * ��ѯ�������ӿ�
 * @author chenjie
 * 2012-12-4
 */
public interface IResultSetProcessor<T> {
	
	public List<T> handleSet(ResultSet rs) throws SQLException;

}
