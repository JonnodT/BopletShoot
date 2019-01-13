package com.tarena.shoot;
import java.awt.image.BufferedImage; //BufferedImage是专门的数据类型装图片的

/** 飞行物类 */

public abstract class FlyingObject {
	protected BufferedImage image;
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	
	public abstract void step();
	
	public abstract boolean outOfBounds();
	
	public boolean ShotBy(Bullet bullet){
		int Ex1 = this.x;
		int Ex2 = this.x + this.width;
		int Ey1 = this.y;
		int Ey2 = this.y + this.height;
		int Bx1 = bullet.x;
		int Bx2 = bullet.x + bullet.width;
		int By1 = bullet.y;
		int By2 = bullet.y + bullet.height;
		boolean result = Bx1 < Ex2 && Bx2 > Ex1 && By1 < Ey2 && By2 > Ey1;
		return result; 
	}
}
