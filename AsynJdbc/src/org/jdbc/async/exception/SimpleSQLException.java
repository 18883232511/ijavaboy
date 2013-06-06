package org.jdbc.async.exception;

/**
 * �յ��ײ���쳣��Ϣ��װ�ɱ����쳣���ύ���ϲ㡣
 * @author chenjie
 * 2012-11-27
 */
@SuppressWarnings("serial")
public class SimpleSQLException extends Exception {
	
	public SimpleSQLException(){
		super();
	}
	
	public SimpleSQLException(String msg){
		super(msg);
	}
	
	public SimpleSQLException(String msg, Throwable clause){
		super(msg, clause);
	}
	
	public SimpleSQLException(Throwable clause){
		super(clause.getMessage(), clause);
	}

}
