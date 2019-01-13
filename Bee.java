package com.tarena.shoot;

import java.util.Random;

public class Bee extends FlyingObject implements Award{
	private int xSpeed = 1; //横着走的步数
	private int ySpeed = 2; //纵着走的步数
	private int awardType; //奖励类型 0 或 1 0代表奖励火力值 1代表奖励命
	
	
    public int getType(){
    	return awardType;
    }
    
    public Bee(){
    	image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width);
		y = -this.height;
		awardType = rand.nextInt(2);
    }
    public void step(){
		x += xSpeed;
		y += ySpeed;
		if(x>=ShootGame.WIDTH-this.width){
			xSpeed = -xSpeed;
		}
		if(x<0){
			xSpeed = -xSpeed;
		}
	}
    
    public boolean outOfBounds(){
		return this.y >= ShootGame.HEIGHT;
	}
}
