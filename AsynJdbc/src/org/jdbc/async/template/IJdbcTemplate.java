package org.jdbc.async.template;

import java.sql.SQLException;
import java.util.List;

import org.jdbc.async.exception.SimpleSQLException;
import org.jdbc.async.transaction.TransactionCallback;

/**
 * ��װJDBC����
 * @author chenjie
 * 2012-12-4
 */
public interface IJdbcTemplate<T> {

	
	public void destroy();
	
	/**
	 * ��ѯ��¼��
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public long countResult(String sql, Object...params) throws SimpleSQLException;
	
	/**
	 * ��ѯΨһ��¼,�������һ��,���׳��쳣
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public T uniqueResult(String sql, Object...params) throws SimpleSQLException;
	
	/**
	 * ��ѯ������¼
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<T> listResult(String sql, Object...params) throws SimpleSQLException;
	
	public void executeUpdate(String sql, Object...params) throws SimpleSQLException;
	
	/**
	 * �������
	 * @param callback : ������ƻص��ӿ�
	 * @return
	 */
	public Object runInTransaction(TransactionCallback callback);
}
