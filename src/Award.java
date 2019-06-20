

/**
Class for awards. Only bees at this point.
But can have more in the future
*/
public interface Award {
	public int FIRE_POWER = 0; 
	public int LIFE = 1;
	
	//return 0 reamns firepower bonus, and 1 means extra life. 
	public int getType();
}
