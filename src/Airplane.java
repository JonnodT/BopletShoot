import java.util.Random;

/**
Class for enemy airplanes in the game
*/
public class Airplane extends FlyingObject implements Enemy{
	
	//How fast the plane moves on the screen 
	private int speed = 2; 
	

	public int getScore(){
		//Player gets 5 points for shooting down each enemy plane
		return 5; 
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
