package org.jdbc.async.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Ĭ�ϵ������ļ������࣬Ĭ�ϴ�srcĿ¼��Ѱ�������ļ�
 * 
 * �����Ҫ�������ļ��ŵ����Ŀ¼���棬����Լ̳и�����дopenInputStream����
 * 
 * @author chenjie
 *
 */
public final class ConfigLoader {
	
	private static ConfigLoader instance = new ConfigLoader();
	
	private ConfigLoader(){}
	
	public static ConfigLoader getInstance(){
		
		return instance;
	}
	
	public SimpleConfig loadDefault(){
		
		return this.load("connection.properties");
	}
	
	/**
	 * Ĭ����srcĿ¼��
	 * @param path
	 * @return
	 */
	protected InputStream openInputStream(String path){
		
		 InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream(path);
		 
		 return in;
	}
	
	/**
	 * path : ��������ļ���
	 */
	public final SimpleConfig load(String path){
		 
		 InputStream in = this.openInputStream(path);
		 
		 Properties p = new Properties();
		 try {
			p.load(in);
			
			SimpleConfig config = new SimpleConfig();
			config.setDriver(p.getProperty("driver"));
			config.setConnectionString(p.getProperty("jdbcUrl"));
			config.setUsername(p.getProperty("username"));
			config.setPassword(p.getProperty("password"));
			
			return config;
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		 
		return null;
	}

}
