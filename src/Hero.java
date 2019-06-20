import java.awt.image.BufferedImage;
import java.util.Random;


/**
Class for the player's plane
*/
public class Hero extends FlyingObject{
	private int life; 
	private int firePower; 
	private BufferedImage[] images;
	//This variable helps with the shift of hero plane's images
	private int index;
	
	public Hero(){
		image = ShootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		x = 150;
		y = 400;
		//Default life = 3
		life = 3;
		firePower = 0;
		images = new BufferedImage[]{ShootGame.hero0, ShootGame.hero1};
		index = 0;
		
	}
	
	public void step(){ 
		//Switch image every 0.1 sec
		image = images[index++/10%images.length];
		}
	

	public Bullet[] shoot(){
		int xStep = this.width/4-2; 
		int yStep = 10;
		if(firePower>0){
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x + 1 * xStep,this.y - yStep);
			bs[1] = new Bullet(this.x + 3 * xStep,this.y - yStep);
			firePower -= 2;
			return bs;
		}else{
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
	
	public void addLife(){
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
	
	public void addFirePower(){ 
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
