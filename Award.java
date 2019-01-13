package com.tarena.shoot;
/**奖励 */

public interface Award {
	public int FIRE_POWER = 0; //火力值
	public int LIFE = 1; //命
	
	// 获取奖励类型 返回0为火力值 返回1为命
	public int getType();
}
