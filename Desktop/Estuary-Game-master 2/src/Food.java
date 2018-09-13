
import java.util.Random;
/**
 * Class for food
 * Extends class obstacle
 * food will either be detritus or clam
 * @author cryst
 *
 */
public class Food extends Obstacle{

	/**
	 * Constructor for food objects
	 * @param h the height of the image
	 */
	public Food(int h, int fHeight){

		super();

		// Set the height to the one determined in XBar
		this.setHeight(h); 

		Random r = new Random();


		// 50/50 chance of creating detritus or clam
		if(r.nextBoolean()){

			this.setValue(1);
			this.setPoints(10);

			int iHeight = fHeight/10;
			int iWidth = iHeight;

			this.setImgWidth(iWidth);
			this.setImgHeight(iHeight);
			type = "detritus";

		}else{

			this.setValue(2);
			this.setPoints(20);

			int iHeight = fHeight/10;
			int iWidth = iHeight;

			this.setImgWidth(iWidth);
			this.setImgHeight(iHeight);
			type = "clam";

		} 

		this.loadImage(this.getImgWidth(),this.getImgHeight());

	}

}
