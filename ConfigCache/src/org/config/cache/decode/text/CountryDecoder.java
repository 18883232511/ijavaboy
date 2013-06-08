package org.config.cache.decode.text;

import org.config.cache.StringArray;
import org.config.cache.core.AbstractDecoder;
import org.config.cache.data.CountryConfig;

/**
 * �������ñ��н�����
 * @author chenjie
 * 2012-12-12
 */
public class CountryDecoder extends AbstractDecoder<CountryConfig> {

	@Override
	public CountryConfig decode(StringArray values) {
		
		CountryConfig config = new CountryConfig();
		config.fromStringArray(values);
		
		return config;
	}

}
