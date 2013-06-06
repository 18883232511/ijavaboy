package org.jdbc.async.template.simple;

import java.util.List;

import org.jdbc.async.config.ConfigLoader;
import org.jdbc.async.config.SimpleConfig;
import org.jdbc.async.exception.SimpleSQLException;
import org.jdbc.async.pool.IPoolProvider;
import org.jdbc.async.pool.bonecp.BoneCPProvider;
import org.jdbc.async.template.DefaultJdbcTemplate;
import org.jdbc.async.utils.QueryUtils;

/**
 * �򵥵�Jdbc������װ
 * 
 * ���ṩ�첽����֧�֣����и��²���ֱ�Ӹ��µ����ݿ�
 * @author chenjie
 * 2012-12-1
 */
public class SimpleJdbcTemplate extends DefaultJdbcTemplate<Object[]> {

	/**
	 * ʹ��Ĭ�ϵ����ӳ�����
	 */
	public SimpleJdbcTemplate(){
		
		SimpleConfig config = ConfigLoader.getInstance().load(SimpleConfig.DEFAULT_CONFIG_NAME);
		this.poolProvider = new BoneCPProvider(config);
		
	}
	
	/**
	 * ʹ��ָ�������ӳ�����
	 * @param poolProvider
	 */
	public SimpleJdbcTemplate(IPoolProvider poolProvider) {
		super(poolProvider);
	}

	@Override
	public Object[] uniqueResult(String sql, Object... params)
			throws SimpleSQLException {

		List<Object[]> result = QueryUtils.arrayQuery(this.poolProvider, sql, params);

		if(result == null || result.size() == 0){
			return null;
		}else if(result.size() > 1){
			
			throw new SimpleSQLException("Expected unique result but queried more than one record.");
		}
		else {
			
			return result.get(0);
		}
	}

	@Override
	public List<Object[]> listResult(String sql, Object... params)
			throws SimpleSQLException {
		
		List<Object[]> result = QueryUtils.arrayQuery(this.poolProvider, sql, params);
		
		if(result == null || result.size() == 0){
			return null;
		}else {
			return result;
		}
	}
	
	@Override
	public long countResult(String sql, Object...params) throws SimpleSQLException{
		
		Object[] result = this.uniqueResult(sql, params);
		
		if(result == null){
			return 0;
		}else {
			Object count = result[0];
			return (Long)count;
		}
		
	}

}
