package com.tarena.shoot;
import java.awt.image.BufferedImage;
import java.util.Random;

/** 是飞行物*/

public class Hero extends FlyingObject{
	private int life; //命
	private int firePower; //火力值
	private BufferedImage[] images;// 图片数组
	private int index; // 协助图片切换
	
	public Hero(){
		image = ShootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		x = 150;
		y = 400;
		life = 3; //默认3条名
		firePower = 0;// 火力值0单倍火力
		images = new BufferedImage[]{ShootGame.hero0, ShootGame.hero1};
		index = 0;
		
	}
	
	public void step(){ //十毫秒走一次step
		//切图片算法100毫秒切一次
		image = images[index++/10%images.length];
		}
	
	/**英雄机发射子弹*/
	public Bullet[] shoot(){
		int xStep = this.width/4-2; //四分之一英雄机的宽
		int yStep = 10;
		if(firePower>0){ //双倍火力
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x + 1 * xStep,this.y - yStep);
			bs[1] = new Bullet(this.x + 3 * xStep,this.y - yStep);
			firePower -= 2;
			return bs;
		}else{            //单倍火力
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x + 2 * xStep,this.y - yStep);
			return bs;
		}
	}
	
	public void moveTo(int x, int y){
		this.x = x - this.width/2;
		this.y = y - this.height/2;
	}
	
	public boolean outOfBounds(){
		return false;
	}
	
	public void addLife(){ //英雄机增命
		life++;
	}
	
	public int getLife(){
		return life;
	}
	
	public void subtractLife(){
		life--;
	}
	
	public void clearFirePower(){
		firePower = 0;
	}
	
	public void addFirePower(){ //英雄机增火力值
		firePower += 40;
	}
	
	public boolean hit(FlyingObject obj){
		int x1 = obj.x - this.width/2;  
		int x2 = obj.x + this.width/2 + obj.width; 
		int y1 = obj.y - this.height/2;
		int y2 = obj.y + obj.height + this.height/2;
		int x = this.x + this.width/2;
		int y = this.y + this.height/2;
		
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
		  
	}
}
