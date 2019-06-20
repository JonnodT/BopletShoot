import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.Timer;      
import java.util.TimerTask;  
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;


public class ShootGame extends JPanel{

	//Set window size
	public static final int WIDTH = 400;     
	public static final int HEIGHT = 654;   
	
	public static BufferedImage background;  
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	

	// Different states of the game
	public static final int START = 0;   
	public static final int RUNNING = 1; 
	public static final int PAUSE = 2;	 
	public static final int GAME_OVER = 3;
	//current state
	private int state = START;  
	
	//create hero
	private Hero hero = new Hero();       
	private FlyingObject[] flyings = {};  
	private Bullet[] bullets = {};        
	
	
	
	static{  

		try{
			background = ImageIO.read(ShootGame.class.getResource("static/background.png"));
			start = ImageIO.read(ShootGame.class.getResource("static/start.png"));
			pause = ImageIO.read(ShootGame.class.getResource("static/pause.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("static/gameover.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("static/airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("static/bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("static/bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("static/hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("static/hero1.png"));
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** Create enemy*/
	public FlyingObject nextOne(){ 
		 Random rand = new Random();
		 int type = rand.nextInt(20);
		 if (type == 0){ //随机数为0生成奖励
			 return new Bee();
		 }else{          //随机数为1-19生成敌人
			 return new Airplane();
		 }
	}
	
	int flyEnteredIndex = 0;
	
	/**
	Let enemy fly into the window
	*/
	public void enterAction(){ 
		flyEnteredIndex++;
		if(flyEnteredIndex%50 == 0){
			FlyingObject one = nextOne();
			flyings = Arrays.copyOf(flyings, flyings.length+1); 
			flyings[flyings.length-1] = one;                    
			
			
		}
	}
	

	public void stepAction(){
		hero.step();
		for(int i = 0; i < flyings.length; i++){
			flyings[i].step();
		}
		for(int i = 0; i < bullets.length; i++){
			bullets[i].step();
		}
	}
	
	//Count bullets
	int shootIndex = 0; 
	
	public void shootAction(){  
		shootIndex++; 
		if (shootIndex%20 == 0){ 

			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets, bullets.length+bs.length);
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length,bs.length); 
		}
	}
	
	public void outOfBoundsAction(){
		int index = 0;
		FlyingObject[] flyingsInBounds = new FlyingObject[flyings.length];
		for(int i = 0; i < flyings.length; i++){
			FlyingObject f = flyings[i];
			if(!f.outOfBounds()){			
				flyingsInBounds[index] = f;
				index++;
			}
		}
		flyings = Arrays.copyOf(flyingsInBounds, index);
		
		int index2 = 0;
		Bullet[] bulletsInBounds = new Bullet[bullets.length];
		for (int c = 0; c < bullets.length; c++){
			Bullet b = bullets[c];
			if(!b.outOfBounds()){
				bulletsInBounds[index2] = b;
				index2++;
			}
		}
		bullets = Arrays.copyOf(bulletsInBounds, index2);
	}

	/**Check for bullet hitting enemy*/
	public void bangAction(){
		for(int i = 0; i < bullets.length; i++){
			bang(bullets[i],i);
		}
	}
	
	int score = 0;
	public void bang(Bullet b, int bIndex){
		int index = -1;
		for(int i = 0; i < flyings.length; i++){
			FlyingObject f = flyings[i];
			if(f.ShotBy(b)){
				index = i;
				break;
			} 
		}
		if(index != -1)	{
			FlyingObject one = flyings[index];
			if(one instanceof Enemy){
				Enemy e = (Enemy)one;
				score += e.getScore();
			}
			if(one instanceof Award){
				Award a = (Award)one;
				int type = a.getType();
				switch(type){
					case Award.FIRE_POWER:
						hero.addFirePower();
						break;
					case Award.LIFE:
						hero.addLife();
						break; 
				}
			}
			FlyingObject t = flyings[index];
			flyings[index] = flyings[flyings.length-1];
			flyings[flyings.length-1] = t; 
			flyings = Arrays.copyOf(flyings, flyings.length-1);
			
			Bullet db = b;
			bullets[bIndex] = bullets[bullets.length-1];
			bullets[bullets.length-1] = db;
			bullets = Arrays.copyOf(bullets, bullets.length-1);
		}
	}
	
	public void checkGameOverAction(){
		if(isGameOver()){
			state = GAME_OVER;
		}
	}
	/** Game over if return true*/
	public boolean isGameOver(){
		for(int i = 0; i < flyings.length; i++){
			FlyingObject f = flyings[i];
			if(hero.hit(f)){
				hero.subtractLife();
				hero.clearFirePower();
				flyings[i] = flyings[flyings.length - 1];
				flyings[flyings.length - 1] = f;
				flyings = Arrays.copyOf(flyings, flyings.length-1);
			}
		}
		return hero.getLife()<=0;
	}
	
	public void action(){ 
		MouseAdapter l = new MouseAdapter(){
			public void mouseMoved(MouseEvent e){ 
				if(state == RUNNING){
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x,y);
				}
			}
			
			public void mouseDragged(MouseEvent e){
				if(state == RUNNING){
					int x = e.getX();
					int y = e.getY();	
					hero.moveTo(x,y);
				}
			}
			
			public void mouseClicked(MouseEvent e){
				switch(state){
					case START:
						state = RUNNING;
						break;
					case GAME_OVER:
						score = 0;
						hero = new Hero();
						flyings = new FlyingObject[0];
						bullets = new Bullet[0];
						state = START;
						break;
				}
			}
			
			public void mouseEntered(MouseEvent e){
				if(state == PAUSE){
					state = RUNNING;
				}
			}
			
			public void mouseExited(MouseEvent e){
				if(state == RUNNING){
					state = PAUSE;
				}
			}
		};
		
		this.addMouseListener(l);        
		this.addMouseMotionListener(l); 
		
		
		Timer timer = new Timer();
		int interval = 10;               
		timer.schedule(new TimerTask(){  
			public void run(){     
				//Walk every 10 milliseconds    
				if(state == RUNNING){
					enterAction();
					stepAction();	
					shootAction();      	 
					outOfBoundsAction();	 
					bangAction();			 
					checkGameOverAction();
				}
			repaint();      		 
											
			}
		},interval,interval);
		
	}
	
	public void paint(Graphics g){         
		
		g.drawImage(background,0,0,null);	
		paintHero(g);                		
		paintFlyingObjects(g);
		paintBullets(g);
		paintScoreAndLife(g);				
		paintState(g);
	}
	
	public void paintHero(Graphics g){      
		g.drawImage(hero.image, hero.x, hero.y, null);
	}
	
	public void paintFlyingObjects(Graphics g){
		for(int i = 0; i < flyings.length; i++){
			FlyingObject f = flyings[i]; 
			g.drawImage(f.image,f.x,f.y,null);
			
		}
	}
	

	public void paintBullets(Graphics g){ 
		for(int i = 0; i < bullets.length; i++){
			Bullet b = bullets[i];
			g.drawImage(b.image, b.x, b.y, null);
		}
	}
	
	public void paintScoreAndLife(Graphics g){
		g.setColor(Color.RED);
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		g.drawString("Score："+score,10,25);
		g.drawString("Life："+hero.getLife(), 300,25);
	}
	
	public void paintState(Graphics g){
		switch(state){
		case START:
			g.drawImage(start, 0, 40, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Boplet Shoot");
		ShootGame game = new ShootGame();    
		frame.add(game);                     
		frame.setSize(WIDTH, HEIGHT);       
		frame.setAlwaysOnTop(true);          
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setLocationRelativeTo(null);  
		frame.setVisible(true);              
		game.action();           
	}
}


