
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * Class Background
 * Implements Serializable
 * Creates the background with the ocean floor
 * Also displays obstacles
 * @author Patrick Boyle
 * @author Karl Stomberg
 * @author Crystal Conroy
 * @author Vincent Conte
 * @author Andrew Cucuzza
 */
public class Background implements java.io.Serializable{

	/**
	 * @author Patrick Boyle
	 * @author Karl Stomberg
	 * @author Crystal Conroy
	 * @author Vincent Conte
	 * @author Andrew Cucuzza
	 */
	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	/** The frame width*/
	private int frameWidth = (int) screenSize.getWidth();
	/** The frame height */
	private int frameHeight = (int) screenSize.getHeight();
	/** Background image in file width */
	private int bkImgWidth = 1018;//works with 1018
	/** Background image in file height */
	private int bkImgHeight = 2000;
	/** Horizontal distance between obstacle rows */
	private final int xbarInterval = frameWidth/3;

	/** Vertical distance between obstacles */
	private int heightInterval;

	/** The background image behind everything  */
	transient private BufferedImage bkgrnd;
	/** The start background image  */
	transient private BufferedImage bkgrndStart;
	/** Current left corner position, tracking for looping */
	private int bkgrndPosX = 0;
	/** Timer to assertain how quickly to increase speed */
	private int timer = 120;
	/** Will increase the number of obstacles */
	private int difficulty = 3;
	/** Number of total rows of obstacles for the game */
	private final int maxXbar = 50;
	/** Stores all the xbars in this array */
	private XBar[] obstacles;
	/** Stores all xbars that will be drawn */
	private XBar[] visible;
	/** Closest index in obstacles, length of visible */
	private int closestIndex = 0, onScreen = 4;
	/** Keeps track of current state of the game */
	private int xbarIndex = 0;
	/** Which xbar is the closest to the player */
	private XBar closest;

	/** 
	 * Class constructor.
	 * Calls drawBackground(). 
	 * Loads in every XBar for the game.
	 */
	public Background() {
		heightInterval = frameHeight/6; //Temporary
		bkgrnd = new BufferedImage(2*frameWidth, frameHeight, BufferedImage.TYPE_INT_RGB);

		try {
			drawBackground();
		} catch (IOException e) {
			e.printStackTrace();
		}

		obstacles = new XBar[maxXbar+onScreen];
		visible = new XBar[onScreen];

		for (int i=0; i < maxXbar; i++) {
			obstacles[i] = new XBar(difficulty, frameWidth, frameHeight);
			if (i < onScreen) {
				visible[i] = obstacles[i];
				visible[i].setPosition(frameWidth+i*xbarInterval);
			}
		}
		for (int i=0; i < onScreen; i++) {
			obstacles[maxXbar+i] = new XBar(1, frameWidth, frameHeight);
		}

		xbarIndex = 3;
		closest = visible[closestIndex];
	}


	/** 
	 * Called from Background - updateBackground()
	 * Sets the visible array to the next set of total obstacles.
	 */
	public void setVisible() {
		if (onScreen+closestIndex < maxXbar+onScreen) {
			for (int i = closestIndex, j = 0; i < onScreen+closestIndex; i++, j++) {
				visible[j] = obstacles[i];
			}
			closest = obstacles[closestIndex];
			visible[onScreen-1].setPosition(frameWidth+xbarInterval);
		}
	}

	/** 
	 * Converts int height to vertical pixel height
	 * @param Takes in an int height h
	 */
	public int convertHeight(int h, int imgHeight) {
		int result = (h*heightInterval) - (imgHeight/2);
		return result;
	}


	/** 
	 * Called from the constructor. 
	 * Loads background from file.
	 * Draws extra to loop the image.
	 */
	private void drawBackground() throws IOException {

		Graphics g = bkgrnd.getGraphics();
		Image section = ImageIO.read(new File("photos/background.png"));

		double widthScale = ((double)bkImgWidth)/((double)bkImgHeight);
		bkImgHeight = frameHeight;
		bkImgWidth = (int)(bkImgHeight*widthScale);

		section = section.getScaledInstance(bkImgWidth,bkImgHeight,Image.SCALE_DEFAULT);

		int i = 0;
		//g.drawImage(section, 0, 0, null);

		while (i*bkImgWidth < frameWidth+bkImgWidth) {
			g.drawImage(section, i*bkImgWidth, 0, null);
			i++;
		}	

	}

	/**
	 * Returns the background image
	 * @return the background image
	 */
	public BufferedImage getBackground() {return bkgrnd;}

	/**
	 * Returns the horizontal background position
	 * @return the horizontal background position
	 */
	public int getBkgrndPos() {return -1*bkgrndPosX;}

	/**
	 * Returns the visible XBars
	 * @return the visible XBars
	 */
	public XBar[] getVisible() {return visible;}

	/**
	 * Returns the closest XBar
	 * @return the closest XBar
	 */
	public XBar getClosest() {
		if (closest.getXpos() + closest.getWidth() < 100)
			return visible[1];
		else
			return closest;
	}

	/**
	 * Returns the set heightInterval between obstacles
	 * @return the set heightInterval between obstacles
	 */
	public int getHeightInterval() {return heightInterval;}

	/**
	 * Returns the frame width
	 * @return the frame width
	 */
	public int getFrameWidth() {return frameWidth;}

	/**
	 * Returns the frame height
	 * @return the frame height
	 */
	public int getFrameHeight() {return frameHeight;}

	/** 
	 * Called from Model - updateLocationAndDirection()
	 * Increments Background and visible position.
	 * @param int score Score of the player
	 */
	public void updateBackground(int score) {
		if(timer % 60 == 0) {
			if(Model.bkgrndSpeed != Model.bkgrndSpeedLmt) {
				Model.bkgrndSpeed += 1;
			}
		}
		timer -= 1;
		bkgrndPosX = (bkgrndPosX + Model.bkgrndSpeed) % bkImgWidth;
		if (xbarIndex < maxXbar+onScreen) {
			for (int i = 0; i < onScreen; i++) {
				visible[i].movePosition(Model.bkgrndSpeed);
			}
			if (closest.getXpos() < -300) {
				closestIndex++;
				xbarIndex++;
				setVisible();
			}
		} 
		if(xbarIndex>=maxXbar+onScreen) {
			Model.endGame(score);
		}
	}
}
