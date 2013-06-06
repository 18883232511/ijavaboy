package org.jdbc.async.processor.array;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbc.async.parser.RowParser;
import org.jdbc.async.processor.BaseListProcessor;

/**
 * ��Object[]�������ʽ��װ������¼��ѯ���
 * @author chenjie
 * 2012-12-4
 */
public class ArrayProcessor extends BaseListProcessor<Object[]> {
	
	@Override
	protected Object[] handleRow(ResultSet rs) throws SQLException {
		
		return RowParser.parseArray(rs);
	}

}
