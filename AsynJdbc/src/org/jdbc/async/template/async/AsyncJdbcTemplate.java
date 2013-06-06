package org.jdbc.async.template.async;

import org.jdbc.async.event.UpdateEvent;
import org.jdbc.async.event.UpdateEventManager;
import org.jdbc.async.exception.SimpleSQLException;
import org.jdbc.async.pool.IPoolProvider;
import org.jdbc.async.template.simple.SimpleJdbcTemplate;
import org.jdbc.async.utils.UpdateUtils;

/**
 * �첽Jdbc������װ
 * @author chenjie
 * 2012-12-1
 */
public class AsyncJdbcTemplate extends SimpleJdbcTemplate {

	private UpdateEventManager asyncEventManager; //�첽���������
	
	private AsyncUpdater updater; //�첽���²���
	
	/**
	 * ʹ��Ĭ�ϵ����ӳ�����
	 */
	public AsyncJdbcTemplate(){
		super();
		this.init();
	}
	
	/**
	 * ʹ��ָ�������ӳ�����
	 * @param poolProvider
	 */
	public AsyncJdbcTemplate(IPoolProvider poolProvider) {
		super(poolProvider);
		this.init();
	}
	
	private final void init(){
		
		this.asyncEventManager = UpdateEventManager.getInstance();
		
		this.updater = new AsyncUpdater(this.poolProvider);
		
		this.startSyncUpdater();
	}
	
	public AsyncJdbcTemplate(IPoolProvider poolProvider, int updateInterval){
		
		this(poolProvider);
		
		this.updater.setUpdateInterval(updateInterval);
	}
	
	/**
	 * ���������߳�
	 */
	private void startSyncUpdater(){
		
		new Thread(this.updater).start();
		
	}
	
	/**
	 * ���뻺�壬���־û�
	 */
	@Override
	public void executeUpdate(String sql, Object...params) {
		
		UpdateEvent event = new UpdateEvent(sql, params);
		
		this.asyncEventManager.addUpdateEvent(event);
		
	}
	
	/**
	 * ����ִ��һ���������
	 * @param sql
	 * @param params
	 * @throws SimpleSQLException
	 */
	public void executeUpdateImmediately(String sql, Object...params) throws SimpleSQLException{
		
		
		UpdateUtils.update(this.poolProvider, sql, params);
	}
}
