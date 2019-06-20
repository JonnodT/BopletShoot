import java.util.Random;

/** 敌机 是飞行物也是敌人*/
public class Airplane extends FlyingObject implements Enemy{
	
	private int speed = 2; //走步步数
	
	public int getScore(){
		return 5; //打掉一个敌机5分
	}
	
	public Airplane(){
		image = ShootGame.airplane;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width);
		y = -this.height;
	}
	
	public void step(){
		y+=speed;
	}
	
	public boolean outOfBounds(){
		return this.y >= ShootGame.HEIGHT;
	}
}
