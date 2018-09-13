
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
/**
 * Obstacle class 
 * Implements serializable
 * Super class for food/traps/goldenfish
 * @author cryst
 *
 */
public class Obstacle implements java.io.Serializable{

	/**Which row it's on*/
	private int height;
	/**Amount of health added*/
	private int value;
	/**Number of points added*/
	private int points;
	/** range of hist */
	private int hitRange;
	/** width of image */
	private int imgWidth;
	/** height of image */
	private int imgHeight;
	/** will the obstacle be shown */
	private boolean visible;

	/**For determining the image to use*/
	String type;
	/** Placeholder image */
	transient private Image img;

	/**
	 * Constructor for obstacle
	 * sets visibility to true
	 */
	public Obstacle() {
		visible = true;
	}

	// Getters
	/**
	 * Returns the height of the obstacle
	 * @return the height of the obstacle
	 */
	public int getHeight() {return this.height;}
	/**
	 * Returns the value of the obstacle
	 * @return the value of the obstacle
	 */
	public int getValue() {return this.value;}
	/**
	 * Returns the points of the obstacle
	 * @return the points of the obstacle
	 */
	public int getPoints() {return this.points;}
	/**
	 * Returns the image height of the obstacle
	 * @return the image height of the obstacle
	 */
	public int getImgHeight() {return this.imgHeight;}
	/**
	 * Returns the image width of the obstacle
	 * @return the image width of the obstacle
	 */
	public int getImgWidth() {return this.imgWidth;}
	/**
	 * Returns the image of the obstacle
	 * @return the image of the obstacle
	 */
	public Image getImage() {return this.img;}
	/**
	 * Returns the visibility of the obstacle
	 * @return the visibility of the obstacle
	 */
	public boolean getVisible() {return this.visible;}

	// Setters
	/**
	 * Sets the height of the obstacles
	 * @param h the new height of the obstacle
	 */
	public void setHeight(int h) {this.height = h;}
	/**
	 * Sets the value of the obstacles
	 * @param v the new value of the obstacle
	 */
	public void setValue(int v) {this.value = v;}
	/**
	 * Sets the points of the obstacles
	 * @param p the new points of the obstacle
	 */
	public void setPoints(int p) {this.points = p;}
	/**
	 * Sets the image width of the obstacles
	 * @param w the new image width of the obstacle
	 */
	public void setImgWidth(int w) {this.imgWidth = w;}
	/**
	 * Sets the image height of the obstacles
	 * @param h the new image height of the obstacle
	 */
	public void setImgHeight(int h) {this.imgHeight = h;}
	/**
	 * Sets the visible argument to false
	 */
	public void setInvisible() {this.visible = false;}
	// Load the Image
	/**
	 * Loads the desired image for the obstacle
	 */
	public void loadImage(int w, int h) {
		try{
			img = ImageIO.read(new File("photos/" + type + ".png"));
		} catch (IOException e){
			e.printStackTrace();
		}

		try{
			img = img.getScaledInstance(w,h,Image.SCALE_DEFAULT);
		} catch (IllegalArgumentException e){
			//System.out.println(w + ", " + h + " " + type);
			//e.printStackTrace();
		}

	}

	// For back-end purposes
	/**
	 * Loads the desired image for the obstacle
	 * @return the type, height, and value of the obstacle
	 */
	public String toString(){
		return "Type: " + type + ", Height: " + this.height + ", Value = " + this.value;
	}

	// updated hit method
	/**
	 * Changes the player health and points
	 * @param the current player state, for changing points and health
	 */
	public void hit(Player player) {
		player.addHealth(value); 
		player.addPoints(points);
		player.setInvincible();
	}
}
