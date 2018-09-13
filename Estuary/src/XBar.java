
import java.io.Serializable;
import java.util.*;
/**
 * Class xBar implement serializable
 * Creates columns of obstacles
 * @author cryst
 *
 */
public class XBar implements Serializable{

	/** List of obstacles in each column */
	ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	/** the hardcoded height of the game */
	final int numRows = Model.maxHeight;
	/** the variable number of obstacles in each XBar */
	private int numObstacles;
	/** position of current xbar */	
	private int xpos;
	/** width of xbars */	
	private int xbarwidth;
	/** height for scaling images */
	private int bgHeight;

	/**
	 * Default constructor, uses max number of rows
	 */
	public XBar(int h){
		xbarwidth = 0;
		populateList(numRows, h);

	}


	/**
	 * Separate constructor, the lower the number, the smaller the number fed into the random generator of obstacles
	 * @param difficulty the max numbers of obstacles
	 * @param x the x position of the bar
	 */
	public XBar(int difficulty, int x, int h){
		xbarwidth = 0;
		xpos = x; //frameWidth
		populateList(difficulty, h);

	}

	/**
	 * Fills up the obstacle list
	 * @param dif the max numbers of obstacles
	 */
	private void populateList(int dif, int bgHeight){

		// Create a random int to determine how many obstacles will be in the column
		Random rand = new Random();
		if (dif != 1)
			numObstacles = rand.nextInt(dif) + 2; //Creates at least 2 obstacles always
		else 
			numObstacles = rand.nextInt(dif);

		if (numObstacles > numRows)
			numObstacles = numRows;

		// For determining the already filled spots
		ArrayList<Integer> coveredSpots = new ArrayList<Integer>();

		// For each proposed obstacles
		for(int i = 0;i<numObstacles;i++){

			Obstacle o;

			// Find the height, replace it if it's an overlap
			int height = rand.nextInt(numRows)+1;

			while(coveredSpots.contains(height)){
				height = rand.nextInt(numRows)+1;
			}

			// Prevents future overlaps
			coveredSpots.add(height);

			// 50/50 chance of create a food or trash with given height
			if(rand.nextBoolean()){
				if (rand.nextBoolean() && rand.nextBoolean())
					o = new GoldenFish(height, bgHeight);
				else
					o = new Food(height, bgHeight);

			}else {
				if (rand.nextBoolean() && rand.nextBoolean())
					o = new Trap(height, bgHeight);
				else
					o = new Trash(height, bgHeight);

			}
			// What is the width?
			if (o.getImgWidth() > xbarwidth)
				xbarwidth = o.getImgWidth();

			// Add the new obstacle
			obstacles.add(o);

		}

	}

	/**
	 * Updates the x position
	 * @param x the amount to move the xbar by
	 */
	public void movePosition(int x) {xpos-=x;}
	/**
	 * Sets the x position
	 * @param x the new x position for the x bar
	 */
	public void setPosition(int x) {xpos=x;}
	/**
	 * Gts the x position
	 * @return the current x position
	 */
	public int getXpos() {return xpos;}
	/**
	 * Gts the xbar width
	 * @return the current xbar width
	 */
	public int getWidth() {return xbarwidth;}

	/**
	 * Gets the obstacles list, for other classes
	 * @returns the list of obstacles
	 */
	public ArrayList<Obstacle> getObstacles(){
		return this.obstacles;
	}

}
