package org.jdbc.async.parser;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ����ѯ���ת��Ϊ��Ӧ��ʵ����
 * @author chenjie
 * 2012-12-4
 */
public class BeanParser {
	
	private static final int PROPERTY_NOT_FOUND = -1;
	
	/**
	 * ��ResultSet��һ��ת����һ��ʵ����
	 * @param rs
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public <T> T parseBean(ResultSet rs, Class<T> type) throws SQLException{
		
		PropertyDescriptor[] pds = this.getPropertyDescriptors(type);
		
		ResultSetMetaData meta = rs.getMetaData();
		
		int[] columns = this.mapColumnToProperty(meta, pds);
		
		return this.createBean(rs, type, pds, columns);
	}
	
	public <T> List<T> parseBeanList(ResultSet rs, Class<T> type) throws SQLException{

		PropertyDescriptor[] pds = this.getPropertyDescriptors(type);
		
		ResultSetMetaData meta = rs.getMetaData();
		
		int[] columns = this.mapColumnToProperty(meta, pds);
		
		List<T> result = new ArrayList<T>();
		while(rs.next()){
			result.add(this.createBean(rs, type, pds, columns));
		}
		
		return result;
	}
	
	/**
	 * ����һ��ʵ��
	 * @param rs
	 * @param type
	 * @param props
	 * @param columnToProperties
	 * @return
	 * @throws SQLException
	 */
	private <T> T createBean(ResultSet rs, Class<T> type, PropertyDescriptor[] props, int[] columnToProperties) throws SQLException{
		
		T bean = this.newInstance(type);
		
		
		
		for(int i=1; i<columnToProperties.length; i++){
			if(columnToProperties[i] == PROPERTY_NOT_FOUND){
				continue;
			}
			
			PropertyDescriptor prop = props[columnToProperties[i]];
			Class<?> propType = prop.getPropertyType();
			
			Object value = this.getColumnValue(rs, i, propType);
			
			this.callSetter(bean, prop, value);
			
		}
		
		return bean;
		
	}
	
	/**
	 * ����setter��������ֵ
	 * @param target
	 * @param prop
	 * @param value
	 * @throws SQLException
	 */
	private void callSetter(Object target, PropertyDescriptor prop, Object value) throws SQLException{
		
		Method setter = prop.getWriteMethod();
		
		if(setter == null)return;
		
		Class<?>[] params = setter.getParameterTypes();
		Class<?> paramType = params[0];  //ȡ��һ��

		if(this.isTypeEnabled(paramType, value)){
			
			try {
				setter.invoke(target, value);
				
			} catch (IllegalAccessException e) {
				
				throw new SQLException(String.format("Can not set %s : %s", prop.getName(), e.getMessage()));
			} catch (IllegalArgumentException e) {
				
				throw new SQLException(String.format("Can not set %s : %s", prop.getName(), e.getMessage()));
			} catch (InvocationTargetException e) {
				
				throw new SQLException(String.format("Can not set %s : %s", prop.getName(), e.getMessage()));
			}
			
		}else{
			
			throw new SQLException(String.format("Can not set %s : incompatible type,cannot convert %s to %s", prop.getName(), value.getClass().getName(), paramType.getName()));
		}
	}
	
	/**
	 * �жϽ�Ҫ���õ�ֵ�����ͺ�setter�����Ĳ��������Ƿ�ƥ��
	 * @param type
	 * @param value
	 * @return
	 */
	private boolean isTypeEnabled(Class<?> type, Object value){
		
		//Date���⣬���⴦��
		if(value instanceof java.util.Date){
			final String typeName = type.getName();
			java.util.Date valueDate = (java.util.Date)value;
			
			if("java.sql.Date".equals(typeName)){
				value = new java.sql.Date(valueDate.getTime());
			}else if("java.sql.Time".equals(typeName)){
				value = new java.sql.Time(valueDate.getTime());
			}else if("java.sql.Timestamp".equals(typeName)){
				value = new java.sql.Timestamp(valueDate.getTime());
			}
		}
		
		
		if( value == null || type.isInstance(value)){
			
			return true;
		}else if (type.equals(Integer.TYPE) && Integer.class.isInstance(value)) {
            
			return true;

        }  else if (type.equals(Long.TYPE) && Long.class.isInstance(value)) {
            return true;

        } else if (type.equals(Double.TYPE) && Double.class.isInstance(value)) {
            return true;

        } else if (type.equals(Float.TYPE) && Float.class.isInstance(value)) {
            return true;

        } else if (type.equals(Short.TYPE) && Short.class.isInstance(value)) {
            return true;

        } else if (type.equals(Byte.TYPE) && Byte.class.isInstance(value)) {
            return true;

        } else if (type.equals(Character.TYPE) && Character.class.isInstance(value)) {
            return true;

        } else if (type.equals(Boolean.TYPE) && Boolean.class.isInstance(value)) {
            return true;

        }
		
		return false;
	}
	
	/**
	 * ��ȡָ���е�ֵ��������ƥ��
	 * @param rs
	 * @param columnIndex
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	private Object getColumnValue(ResultSet rs, int columnIndex, Class<?> type) throws SQLException{
		
		if(!type.isPrimitive() && rs.getObject(columnIndex) == null){
			//�����ԭ�����ͣ���û�л�ȡ�������򷵻ؿ�ֵ
			return null;
		}
		
		if(type.equals(String.class)){
			
			return rs.getString(columnIndex);
			
		}else if(type.equals(Integer.TYPE) || type.equals(Integer.class)){
			
			return Integer.valueOf(rs.getInt(columnIndex));
		}else if(type.equals(Long.TYPE) || type.equals(Long.class)){
			
			return Long.valueOf(rs.getLong(columnIndex));
		}else if(type.equals(Float.TYPE) || type.equals(Float.class)){
			
			return Float.valueOf(rs.getFloat(columnIndex));
		}else if(type.equals(Double.TYPE) || type.equals(Double.class)){
			
			return Double.valueOf(rs.getDouble(columnIndex));
		}else if(type.equals(Short.TYPE) || type.equals(Short.class)){
			
			return Short.valueOf(rs.getShort(columnIndex));
		}else if(type.equals(Byte.TYPE) || type.equals(Byte.class)){
			
			return Byte.valueOf(rs.getByte(columnIndex));
		}else if(type.equals(Boolean.TYPE) || type.equals(Boolean.class)){
			
			return Boolean.valueOf(rs.getBoolean(columnIndex));
		}else if(type.equals(Timestamp.class)){
			
			return rs.getTimestamp(columnIndex);
		}else {
			
			return rs.getObject(columnIndex);
		}
		
	}
	
	/**
	 * ����һ����ʵ��
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	private <T> T newInstance(Class<T> clazz) throws SQLException{
		
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			
			throw new SQLException(String.format("Can not create %s : %s", clazz.getName(), e.getMessage()));
			
		} catch (IllegalAccessException e) {
			
			throw new SQLException(String.format("Can not create %s : %s", clazz.getName(), e.getMessage()));
		}
		
	}
	
	/**
	 * ��ȡ����������
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	private PropertyDescriptor[] getPropertyDescriptors(Class<?> type) throws SQLException{
		
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type);
			
			return beanInfo.getPropertyDescriptors();
			
		} catch (IntrospectionException e) {

            throw new SQLException(
                    "Bean introspection failed: " + e.getMessage());
		}
		
		
	}
	
	/**
	 * meta�е���������PropertyDescriptor[]����������һ��ӳ��
	 * @param meta
	 * @param pds
	 * @return
	 * @throws SQLException
	 */
	private int[] mapColumnToProperty(ResultSetMetaData meta, PropertyDescriptor[] pds) throws SQLException{
		
		final int columnCount = meta.getColumnCount();
		
		int[] result = new int[columnCount+1]; //��Ϊ���ݿ��е��е������Ǵ�1��ʼ��
		
		Arrays.fill(result, PROPERTY_NOT_FOUND); 
		
		for(int i=1; i<=columnCount; i++){
			
			String columnName = meta.getColumnLabel(i);
			
			for(int j=0; j<pds.length; j++){
				
				if(columnName.equalsIgnoreCase(pds[j].getName())){
					
					result[i] = j;
					
					break;
				}
				
			}
			
		}
		
		return result;
		
	}

}
