
/**
 * Golden fish class extends obstacle
 * Gives a fact when player hits it
 * @author cryst
 *
 */
public class GoldenFish extends Obstacle{

	/**
	 * Constructor for food objects
	 * @param h the height of the image
	 */
	public GoldenFish(int h, int fheight){

		super();


		// Set the height to the one determined in XBar
		this.setHeight(h); 

		this.setValue(0);
		this.setPoints(50);
		this.setImgWidth(200);
		this.setImgHeight(120);
		type = "golden_fish";

		this.loadImage(this.getImgWidth(),this.getImgHeight());

	}
	public void hit(Player player) {
		super.hit(player);
		if (Model.mode != Mode.TUTORIALSTART)
			Model.mode = Mode.FORWARDFACT;
		else
			Model.mode = Mode.TUTORIALFACT;
	}
}