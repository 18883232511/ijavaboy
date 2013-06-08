package org.config.cache.data;

import org.config.cache.StringArray;
import org.config.cache.core.IConfig;
import org.config.cache.utils.StringUtils;

/**
 * �������ñ�
 * @author chenjie
 * 2012-12-12
 */
public class MissionConfig implements IConfig {
	
	public static final int TRIGGER_NUM = 3; //���������������
	public static final int SUB_MODE_NUM = 4; //������ģʽ�������
	public static final int REWARD_ITEM_NUM = 5; //������Ʒ�������
	
	private Integer id; //����ID
	private String name; //��������
	private Integer type; //��������
	private Integer rootTaskID; //������ID
	private Integer nextTaskID; //��һ������ID
	private Integer minLevel; //��С�ȼ�
	private Integer maxLevel; //���ȼ�
	
	//��������
	private Integer triggerConditionNum; //������������
	private Integer[] triggerTypes; //��������
	private Integer[] triggerData; //������ֵ
	
	//�������
	private Integer completeModeNum; //��Ҫ���ǰn����ģʽ����
	private Integer[] subModes; //��ģʽ
	private Integer[] subTypes; //������
	private Integer[] subCompletes; //���ѡ��
	
	//����
	private Integer experience; //��������
	private Integer money; //����ͭǮ
	private Boolean random; //�Ƿ��������
	private Integer selectedItemNum; //����ѡ��Ľ�����Ʒ����
	private Integer[] rewardItems; //��������Ʒ
	private Integer[] rewardItemNums; //��������Ʒ����
	
	private String desc; //��������
	
	
	@Override
	public void fromStringArray(StringArray values) {
		
		this.id = values.getInt();
		this.name = values.getString();
		this.type = values.getInt();
		this.rootTaskID = values.getInt();
		this.nextTaskID = values.getInt();
		this.minLevel = values.getInt();
		this.maxLevel = values.getInt();
		
		this.triggerConditionNum = values.getInt();
		
		this.triggerTypes = new Integer[TRIGGER_NUM];
		this.triggerData = new Integer[TRIGGER_NUM];
		for(int i=0; i<TRIGGER_NUM; i++){
			this.triggerTypes[i] = values.getInt();
			this.triggerData[i] = values.getInt();
		}
		
		this.completeModeNum = values.getInt();
		
		this.subModes = new Integer[SUB_MODE_NUM];
		this.subTypes = new Integer[SUB_MODE_NUM];
		this.subCompletes = new Integer[SUB_MODE_NUM];
		
		for(int i=0; i<SUB_MODE_NUM; i++){
			this.subModes[i] = values.getInt();
			this.subTypes[i] = values.getInt();
			this.subCompletes[i] = values.getInt();
		}
		
		this.experience = values.getInt();
		this.money = values.getInt();
		this.random = values.getBool();
		this.selectedItemNum = values.getInt();
		
		this.rewardItems = new Integer[REWARD_ITEM_NUM];
		this.rewardItemNums = new Integer[REWARD_ITEM_NUM];
		
		for(int i=0; i<REWARD_ITEM_NUM; i++){
			this.rewardItems[i] = values.getInt();
			this.rewardItemNums[i] = values.getInt();
		}
		
		this.desc = values.getString();
	}

	@Override
	public String getKey() {

		return this.id+"";
	}

	public final Integer getId() {
		return id;
	}

	public final void setId(Integer id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final Integer getType() {
		return type;
	}

	public final void setType(Integer type) {
		this.type = type;
	}

	public final Integer getRootTaskID() {
		return rootTaskID;
	}

	public final void setRootTaskID(Integer rootTaskID) {
		this.rootTaskID = rootTaskID;
	}

	public final Integer getNextTaskID() {
		return nextTaskID;
	}

	public final void setNextTaskID(Integer nextTaskID) {
		this.nextTaskID = nextTaskID;
	}

	public final Integer getMinLevel() {
		return minLevel;
	}

	public final void setMinLevel(Integer minLevel) {
		this.minLevel = minLevel;
	}

	public final Integer getMaxLevel() {
		return maxLevel;
	}

	public final void setMaxLevel(Integer maxLevel) {
		this.maxLevel = maxLevel;
	}

	public final Integer getTriggerConditionNum() {
		return triggerConditionNum;
	}

	public final void setTriggerConditionNum(Integer triggerConditionNum) {
		this.triggerConditionNum = triggerConditionNum;
	}

	public final Integer[] getTriggerTypes() {
		return triggerTypes;
	}

	public final void setTriggerTypes(Integer[] triggerTypes) {
		this.triggerTypes = triggerTypes;
	}

	public final Integer[] getTriggerData() {
		return triggerData;
	}

	public final void setTriggerData(Integer[] triggerData) {
		this.triggerData = triggerData;
	}

	public final Integer getCompleteModeNum() {
		return completeModeNum;
	}

	public final void setCompleteModeNum(Integer completeModeNum) {
		this.completeModeNum = completeModeNum;
	}

	public final Integer[] getSubModes() {
		return subModes;
	}

	public final void setSubModes(Integer[] subModes) {
		this.subModes = subModes;
	}

	public final Integer[] getSubTypes() {
		return subTypes;
	}

	public final void setSubTypes(Integer[] subTypes) {
		this.subTypes = subTypes;
	}

	public final Integer[] getSubCompletes() {
		return subCompletes;
	}

	public final void setSubCompletes(Integer[] subCompletes) {
		this.subCompletes = subCompletes;
	}

	public final Integer getExperience() {
		return experience;
	}

	public final void setExperience(Integer experience) {
		this.experience = experience;
	}

	public final Integer getMoney() {
		return money;
	}

	public final void setMoney(Integer money) {
		this.money = money;
	}

	public final Boolean getRandom() {
		return random;
	}

	public final void setRandom(Boolean random) {
		this.random = random;
	}

	public final Integer getSelectedItemNum() {
		return selectedItemNum;
	}

	public final void setSelectedItemNum(Integer selectedItemNum) {
		this.selectedItemNum = selectedItemNum;
	}

	public final Integer[] getRewardItems() {
		return rewardItems;
	}

	public final void setRewardItems(Integer[] rewardItems) {
		this.rewardItems = rewardItems;
	}

	public final Integer[] getRewardItemNums() {
		return rewardItemNums;
	}

	public final void setRewardItemNums(Integer[] rewardItemNums) {
		this.rewardItemNums = rewardItemNums;
	}

	public final String getDesc() {
		return desc;
	}

	public final void setDesc(String desc) {
		this.desc = desc;
	}

	public String toString(){
		
		return StringUtils.toString(MissionConfig.class, this);
	}
}
