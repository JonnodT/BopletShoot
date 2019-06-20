import java.util.Random;

/** 子弹 仅仅是个飞行物*/

public class Bullet extends FlyingObject{
	private int speed = 3; 
	
	public Bullet(int x, int y)	{
		image = ShootGame.bullet;
		width = image.getWidth();
		height = image.getHeight();
		this.x = x; //子弹的x
		this.y = y; //子弹的y
	}
	
	public void step(){
		y -= speed;
	}
	
	public boolean outOfBounds(){
		return this.y <= -this.height;
	}
}
