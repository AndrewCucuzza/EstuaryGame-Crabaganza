
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
/**
 * Player class
 * Gets all images
 * Converts image sizes
 * Manages player health, score
 * @author cryst
 *
 */
public class Player implements Serializable{
	/** images to be loaded into */
	transient private Image img1, img2, img3;
	/** Images of left facing crabs */
	transient private Image left1, left2, left3;
	/** Images of dead crabs */
	transient private Image imgDead;
	/** Images of crab logo */
	transient private Image crabLogo;
	/** Images of tutorial food */
	transient private Image foodHover;
	/** Images of regular food */
	transient private Image foodNohover;
	/** Images of tutorial trash */
	transient private Image trashHover;
	/** Images of regular trash */
	transient private Image trashNohover;
	/** Images of tutorial quiz */
	transient private Image quizHover;
	/** Images of regular quiz */
	transient private Image quizNohover;
	/** Images of up/down arrows */
	transient private Image arrows;
	/** Images of left facing crabs */
	transient private Image imgLeft;
	/** Images of left facing crabs */
	transient private Image hp1, hp2, hp3, hp4, hp5;
	/** Images of traps */
	transient private Image imgTrap;
	/** image height */
	private int imgWidth = 208;
	/** image width */
	private int imgHeight = 303;
	/** integer used to store player's health */
	private int health;
	/** integer used to store location of player height */
	private int height;
	/** integer used to store player's points */
	private int points;
	/** pseudo final integer, may make this later */
	private int xloc; //pseudo final integer, may make this later
	/** boolean that is set whether the player is alive or not */
	private boolean isAlive;
	/** boolean to make the player invincible for certain cases */
	private boolean invincible;

	/**
	 * Class Constructor
	 * Sets player health, height, and points
	 * Loads in all necessary images from file
	 * @param int bgHeight height of the background
	 */
	public Player(int bgHeight, int bgWidth) {

		double widthScale = ((double)imgWidth)/((double)imgHeight);
		imgHeight = bgHeight/5;
		imgWidth = (int)(imgHeight*widthScale);

		this.health = Model.maxHealth;
		this.height = 2; //yloc will be determined with Background
		this.points = 0;
		xloc = 100;
		isAlive = true;
		invincible = false;
		try {
			img1 = loadImage("crab1");
			img2 = loadImage("crab2");
			img3 = loadImage("crab3");
			imgDead = loadImage("death_screen");
			hp5 = loadImage("5hp");
			hp4 = loadImage("4hp");
			hp3 = loadImage("3hp");
			hp2 = loadImage("2hp");
			hp1 = loadImage("1hp");
			crabLogo=loadImage("crabaganza_logo");
			trashNohover=loadImage("trash_nohover");
			trashHover=loadImage("trash_hover");
			foodNohover=loadImage("food_nohover");
			foodHover=loadImage("food_hover");
			quizNohover=loadImage("quiz_nohover");
			quizHover=loadImage("quiz_hover");
			arrows=loadImage("arrows");
			left1 = loadImage("left1");
			left2 = loadImage("left2");
			left3 = loadImage("left3");
			imgTrap = loadImage("crab_in_trap");
		} catch (IOException e) {
			e.printStackTrace();
		}

		img1 = img1.getScaledInstance(imgWidth,imgHeight,Image.SCALE_DEFAULT);
		img2 = img2.getScaledInstance(imgWidth,imgHeight,Image.SCALE_DEFAULT);
		img3 = img3.getScaledInstance(imgWidth,imgHeight,Image.SCALE_DEFAULT);

		left1 = left1.getScaledInstance(imgWidth,imgHeight,Image.SCALE_DEFAULT);
		left2 = left2.getScaledInstance(imgWidth,imgHeight,Image.SCALE_DEFAULT);
		left3 = left3.getScaledInstance(imgWidth,imgHeight,Image.SCALE_DEFAULT);

		imgTrap = imgTrap.getScaledInstance(imgWidth,imgHeight,Image.SCALE_DEFAULT);

		// Load arrow images

		int arrowsWidth = getWidthByHeight(arrows,bgHeight/6);
		int arrowsHeight = bgHeight/6;

		arrows = arrows.getScaledInstance(arrowsWidth,arrowsHeight,Image.SCALE_DEFAULT);

		// Load crab logo images

		int crabLogoWidth = getWidthByHeight(crabLogo,bgHeight/5);
		int crabLogoHeight = bgHeight/5;

		crabLogo = crabLogo.getScaledInstance(crabLogoWidth,crabLogoHeight,Image.SCALE_DEFAULT);

		// Load middle images

		int trashImgWidth = getWidthByHeight(trashHover,bgHeight/6);
		int trashImgHeight = bgHeight/6;

		trashHover = trashHover.getScaledInstance(trashImgWidth,trashImgHeight,Image.SCALE_DEFAULT);
		trashNohover = trashNohover.getScaledInstance(trashImgWidth,trashImgHeight,Image.SCALE_DEFAULT);

		int foodImgWidth = getWidthByHeight(foodHover,bgHeight/6);
		int foodImgHeight = bgHeight/6;

		foodHover = foodHover.getScaledInstance(foodImgWidth,foodImgHeight,Image.SCALE_DEFAULT);
		foodNohover = foodNohover.getScaledInstance(foodImgWidth,foodImgHeight,Image.SCALE_DEFAULT);

		int quizImgWidth = getWidthByHeight(quizHover,bgHeight/6);
		int quizImgHeight = bgHeight/6;

		quizHover = quizHover.getScaledInstance(quizImgWidth,quizImgHeight,Image.SCALE_DEFAULT);
		quizNohover = quizNohover.getScaledInstance(quizImgWidth,quizImgHeight,Image.SCALE_DEFAULT);

		imgScalingWork(bgWidth/2,
				(int)((double)(bgWidth/2) * (((double)804)/((double)916))), imgDead);

	}

	/**
	 * Correctly Scales the image to desired width and height
	 * @param int w the desired width
	 * @param int h the desired height
	 * @param Image img to be scaled correctly
	 */
	public void imgScalingWork(int w, int h, Image img) {
		img = img.getScaledInstance(w, h, Image.SCALE_DEFAULT);
	}

	public int getWidthByHeight(Image img, int h){

		return ((int)(((double)h) * (((double)img.getWidth(null))/((double)img.getHeight(null)))));

	}

	/**
	 * Loads in files based on string name
	 * @param String s for filename
	 * @return the Image loaded in from file
	 */
	public Image loadImage(String s) throws IOException {
		return ImageIO.read(new File("photos/"+ s + ".png"));
	}

	/**
	 * Moves the players height up by one or none if max
	 */
	public void moveUp() {

		if (height < Model.maxHeight)
			height += 1;
		else
			height = Model.maxHeight;
		if (invincible)
			invincible = false;
	}

	/**
	 * Moves the players height down by one or none if max
	 */
	public void moveDown() {

		if (height > 1)
			height -= 1;
		else
			height = 1;
		if (invincible)
			invincible = false;

	}

	/**
	 * Returns the players img in motion or dead crab
	 * @return the players painted image
	 */
	public Image getImage(int frameNum) {
		if (isAlive)
			switch(frameNum%3){
			case 0:
				return img1;
			case 1:
				return img2;
			case 2:
				return img3;
			default:
				return img2;
			}
		else
			return imgDead;
	}

	/**
	 * Returns the crabaganza starting image for the start screen
	 * @return the crabaganza logo
	 */
	public Image getCrabImage() {

		return crabLogo;
	}
	/**
	 * Returns the crab stuck in a trap
	 * @return the trapped crab
	 */
	public Image getCrabTrap() {
		return imgTrap;
	}

	/**
	 * Returns the image telling player to use the arrows
	 * @return the image telling player to use the arrows
	 */
	public Image getBottomImage() {
		return arrows;
	}

	/**
	 * Returns the image showing the food information
	 * @param int h the height of the crab
	 * @return the image showing the food information
	 */
	public Image getFoodImage() {
		if(getHeight() == 2){
			return foodHover;
		}else{
			return foodNohover;
		}
	}

	/**
	 * Returns the image showing the trash information
	 * @param int h the height of the crab
	 * @return the image showing the trash information
	 */
	public Image getTrashImage() {
		if(getHeight() == 3){
			return trashHover;
		}else{
			return trashNohover;
		}
	}

	/**
	 * Returns the image showing the quiz information
	 * @param int h the height of the crab
	 * @return the image showing the quiz information
	 */
	public Image getQuizImage() {
		if(getHeight() == 4){
			return quizHover;
		}else{
			return quizNohover;
		}
	}

	/**
	 * Returns the image of a left-facing crab for the end of the game
	 * @param int frameNum frame number
	 * @return the image of a left-facing crab
	 */
	public Image getLeftImage(int frameNum) {
		switch(frameNum%3){
		case 0:
			return left1;
		case 1:
			return left2;
		case 2:
			return left3;
		default:
			return left2;
		}
	}

	/**
	 * Returns the image of a dead crab for when you lose
	 * @return the image of a dead crab
	 */
	public Image getDeadImage() {
		return imgDead;
	}

	/**
	 * Returns the players health image based on their health
	 * @return the players health image
	 */
	public Image getHealthImage() {
		switch(this.health){
		case 1:
			return hp1;
		case 2:
			return hp2;
		case 3:
			return hp3;
		case 4: 
			return hp4;
		default:
			return hp5;
		}
	}


	/**
	 * Allows access to kill the crab if conditions are met
	 */
	public void checkAliveStatus() {
		if (health < 1) {
			deadCrab();
		} 
	}

	/**
	 * Sets the player to dead
	 */
	private void deadCrab() {isAlive = false;}

	//Getters
	/**
	 * Returns the players x position on screen with offset xloc
	 * @return the players x position on screen with offset xloc
	 */
	public int imgPosition() {return imgWidth + xloc;}
	/**
	 * Returns invincible status
	 * @return invincible status
	 */
	public boolean getInvincible() {return invincible;}
	/**
	 * Returns the player health
	 * @return the player health
	 */
	public int getHealth() {return health;}
	/**
	 * Returns the player points
	 * @return the player points
	 */
	public int getPoints() {return points;}
	/**
	 * Returns the player height
	 * @return the player height
	 */
	public int getHeight() {return height;}
	/**
	 * Returns the image height of the player
	 * @return the image height of the player
	 */
	public int getImgHeight() {return this.imgHeight;}
	/**
	 * Returns the image width of the player
	 * @return the image width of the player
	 */
	public int getImgWidth() {return this.imgWidth;}

	/**
	 * Returns the player xloc
	 * @return the player xloc
	 */
	public int getXloc() {return xloc;}

	//Setters
	/**
	 * Sets the player health to int h
	 * @param h the new set player health
	 */
	public void setHealth(int h) {health = h;}
	/**
	 * Adds int a health to current health, positive or negative
	 * Keeps the health in bounds
	 * @param a the amount adding to health
	 */

	public void addHealth(int a){
		health += a;
		if (health > Model.maxHealth)
			health = Model.maxHealth;
		else if (health < 1)
			health = 0;
	}
	/**
	 * Sets the players points
	 * @param p the new set player points
	 */
	public void setPoints(int p) {points = p;}
	/**
	 * Adds int a points to the player's
	 * @param a the amount of points added
	 */
	public void addPoints(int a) {points += a;}
	/**
	 * Sets invincible to true
	 */
	public void setInvincible() {invincible = true;}
	/**
	 * Sets invincible to false
	 */
	public void setVincible() {invincible = false;}

//	@Override
//	public int compareTo(Obstacle o) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
}
