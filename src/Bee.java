import java.util.Random;

public class Bee extends FlyingObject implements Award{
	private int xSpeed = 1; //speed on the x-axis
	private int ySpeed = 2; //speed on the y-axis
	//0 reamns firepower bonus, and 1 means extra life. 
	private int awardType; 
	
	
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
