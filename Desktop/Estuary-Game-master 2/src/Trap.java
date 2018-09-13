
import java.util.Timer;
/**
 * Trap class extends obstacle class
 * Traps trigger quizzes for the player
 * quizzes give the player 10 seconds to answer
 * @author cryst
 *
 */
public class Trap extends Obstacle{

	/**
	 * Constructor for food objects
	 * @param h the height of the image
	 */
	static Timer timer;
	public Trap(int h, int fheight){

		super();

		// Set the height to the one determined in XBar
		this.setHeight(h); 

		this.setValue(0);
		this.setPoints(100);
		this.setImgWidth(150);
		this.setImgHeight(150);
		type = "crab_trap";



		this.loadImage(this.getImgWidth(),this.getImgHeight());

	}
	/**
	 * If player hits trap trigger quiz
	 * @param player
	 */
	public void hit(Player player) {
		super.hit(player);
		Model.mode = Mode.QUIZ;
		Quiz.quizTime = 0;
		timer = new Timer();
		timer.schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						Model.mode = Mode.WRONG;
						player.addHealth(-3);
						//System.out.println("STOOOOOPPPPPPP!!!!!!!!");
					}
				}, 
				15000);
		timer.schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						Quiz.quizTime+=30;
						//System.out.println(Quiz.quizTime);
					}
				}, 
				0,30);
		timer.purge();
	}
}
