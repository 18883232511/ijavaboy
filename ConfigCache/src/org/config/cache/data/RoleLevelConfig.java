package org.config.cache.data;

import org.config.cache.StringArray;
import org.config.cache.core.IConfig;

/**
 * ��ҵȼ���
 * @author chenjie
 * 2012-12-12
 */
public class RoleLevelConfig implements IConfig {

	private Integer level; //�ȼ�
	private Integer experience; //����
	
	@Override
	public void fromStringArray(StringArray values) {
		this.level = values.getInt();
		this.experience = values.getInt();
	}

	@Override
	public String getKey() {
		
		return this.level+"";
	}

	public final Integer getLevel() {
		return level;
	}

	public final void setLevel(Integer level) {
		this.level = level;
	}

	public final Integer getExperience() {
		return experience;
	}

	public final void setExperience(Integer experience) {
		this.experience = experience;
	}

	
}
