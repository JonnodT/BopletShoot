import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.Timer;      //定时器
import java.util.TimerTask;  //定时任务
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

//swing中的事件：
//事件：发生了一件事
//事件的处理：发生了那个事之后做的操作
//侦听器


//事件				事件处理
//鼠标点击			从启动状态变为运行状态
//鼠标移动			英雄机随着动
//鼠标移出			从运行状态变为暂停状态
//鼠标移入			从暂停状态变为运行状态

//侦听器：
//1.先有个侦听器
//2.把侦听器添加到面板上

//鼠标侦听器：
//MouseListener 处理鼠标操作时间
//MouseMotionListener 处理鼠标滑动事件
//MouseAdapter   





/** 主程序*/

public class ShootGame extends JPanel{

	public static final int WIDTH = 400;     //窗口宽度
	public static final int HEIGHT = 654;    //窗口高度
	
	public static BufferedImage background;  //背景图
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	
	public static final int START = 0;    //启动状态
	public static final int RUNNING = 1;  //运行状态
	public static final int PAUSE = 2;	  //暂停状态
	public static final int GAME_OVER = 3;//游戏结束状态
	private int state = START;  //当前状态
	
	private Hero hero = new Hero();       //创建英雄机
	private FlyingObject[] flyings = {};  //当作敌机来用，装小蜜蜂和敌机
	private Bullet[] bullets = {};        //创建子弹数组
	
	
	
	static{  //初始化静态资源

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
	
	/** 生成敌人对象*/
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
	
	/** 敌人入场*/
	public void enterAction(){ //十毫秒运行一次，生成敌人对象，将对象添加到flyings数组
		flyEnteredIndex++;
		if(flyEnteredIndex%50 == 0){
			FlyingObject one = nextOne();
			flyings = Arrays.copyOf(flyings, flyings.length+1); //扩大容量
			flyings[flyings.length-1] = one;                    //将敌人添加到数组的最后一个元素
			
			
		}
	}
	
	
	
	/**敌机 小蜜蜂 子弹 向前飞行*/
	public void stepAction(){
		hero.step();
		for(int i = 0; i < flyings.length; i++){
			flyings[i].step();
		}
		for(int i = 0; i < bullets.length; i++){
			bullets[i].step();
		}
	}
	
	int shootIndex = 0; //子弹入场的计数
	
	public void shootAction(){  // 10毫秒走一次
		//创建子弹对象，添加到bullet数组中
		shootIndex++; //每10毫秒加一
		if (shootIndex%20 == 0){ //每300毫秒走一次
			//创建子弹对象，将子弹对象添加到bullets数组中
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets, bullets.length+bs.length);
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length,bs.length); //数组的追加 程序的扩展性好 把bs数组复制粘贴到bullets数组后面。
		}
	}
	
	public void outOfBoundsAction(){
		int index = 0;
		FlyingObject[] flyingsInBounds = new FlyingObject[flyings.length];
		for(int i = 0; i < flyings.length; i++){
			FlyingObject f = flyings[i];
			if(!f.outOfBounds()){				//如果不越界
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
	/**检查所有子弹和所有敌人的碰撞*/
	public void bangAction(){
		for(int i = 0; i < bullets.length; i++){
			bang(bullets[i],i);
		}
	}
	
	int score = 0;
	/**检查一颗子弹和所有敌人的碰撞*/
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
				score += e.getScore();//玩家得分
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
	/** 返回true则游戏结束*/
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
		//创建侦听器对象的（匿名内部类）
		MouseAdapter l = new MouseAdapter(){
			public void mouseMoved(MouseEvent e){ //只要鼠标一移动，就到这里面
				//英雄机随着动
				if(state == RUNNING){
				//获取鼠标的位置
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
		
		this.addMouseListener(l);        //添加监听器   
		this.addMouseMotionListener(l);  //添加监听器
		
		
		Timer timer = new Timer();
		int interval = 10;               //时间间隔（毫秒）
		timer.schedule(new TimerTask(){  //TimerTask是抽象类 
			public void run(){           //定时干的那个事 -- 10毫秒走一次
				if(state == RUNNING){
					enterAction();
					stepAction();	
					shootAction();      	 //子弹入场
					outOfBoundsAction();	 //删除越界对象
					bangAction();			 //子弹与敌机和小蜜蜂的碰撞
					checkGameOverAction();
				}
			repaint();      		 //重画，调用paint
											
			}
		},interval,interval);
		
	}
	
	public void paint(Graphics g){          //paint方法非常特殊，只能由系统自己调用，这里重写paint方法。paint方法调用 frame.setVisible(true)/repaint()
		
		g.drawImage(background,0,0,null);	//画背景
		paintHero(g);                		//paint方法代码多很乱，底下专门写方法
		paintFlyingObjects(g);
		paintBullets(g);
		paintScoreAndLife(g);				//画分和命
		paintState(g);
	}
	
	public void paintHero(Graphics g){      //画英雄机
		g.drawImage(hero.image, hero.x, hero.y, null);
	}
	
	public void paintFlyingObjects(Graphics g){//画敌机
		for(int i = 0; i < flyings.length; i++){
			FlyingObject f = flyings[i]; //获取每一个敌人
			g.drawImage(f.image,f.x,f.y,null);
			
		}
	}
	

	public void paintBullets(Graphics g){ //画子弹对象
		for(int i = 0; i < bullets.length; i++){
			Bullet b = bullets[i];
			g.drawImage(b.image, b.x, b.y, null);
		}
	}
	
	/** 画分和命*/
	public void paintScoreAndLife(Graphics g){
		g.setColor(Color.RED);
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		g.drawString("得分："+score,10,25);
		g.drawString("生命："+hero.getLife(), 300,25);
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
		JFrame frame = new JFrame("飞机大战"); //创建窗口
		ShootGame game = new ShootGame();    //创建面板
		frame.add(game);                     //把面板添加到相框frame中
		frame.setSize(WIDTH, HEIGHT);        //设置长宽
		frame.setAlwaysOnTop(true);          //永远置顶，点窗口外的地方不会被隐藏
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //关闭窗口程序终止
		frame.setLocationRelativeTo(null);   //打开窗口的时候相对于整个屏幕的零点（右上角），写null默认居中显示
		frame.setVisible(true);              //设置窗口可见 尽快调用paint方法
		game.action();                       
		
		
		
	}

	
}


