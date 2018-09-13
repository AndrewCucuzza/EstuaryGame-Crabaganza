
//New import statements added to implement key adaptor
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.util.*;
import java.io.*;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
//End of new import statements

/**
 * Model class is where changes are made throughout the game.
 * It controls the key stroke movements of the player, the mode of the game and hit detection.
 * @author crystal
 *
 */

class Model implements java.io.Serializable{

	/**
	 * Object for player
	 */
	Player player;
	/**
	 * Object for background
	 */
	Background background;
	/**
	 * Maximum health that a player can have
	 */
	final static int maxHealth = 5;
	/**
	 * Number of rows that a player can move through
	 */
	final static int maxHeight = 5;
	/**
	 * Speed of background movement
	 */
	static int bkgrndSpeed = 25;
	/**
	 * Used to increase speed of background
	 */
	static int bkgrndSpeedLmt = 50;
	/*
	 * Measures high score
	 */
	static int highScore = 0;

	/**
	 * What mode the game is in.
	 * Starts in start mode.
	 * Determines what screen the player is looking at.
	 */
	static Mode mode=Mode.START;
	/**
	 * Used for serializing
	 */
	static boolean save = false;
	/**
	 * Used for displaying quizzes 
	 * 
	 */
	int countQuiz=0;

	/** Keeps track of time for facts */
	int counter = 0;
	/** Total amount of time to present facts */
	int count = 100;
	/** Tutorial position count*/
	static int tutorialCount;
	/** To be used in the tutorial */
	static ArrayList<Obstacle> tutorial = new ArrayList<Obstacle>();
	/** A set of positions for each of the obstacles in tutorial */
	static ArrayList<Integer> xpositions = new ArrayList<Integer>();
	/**
	 * Object for quiz
	 */
	Quiz quiz;
	/**
	 * JPanel object for keystrokes
	 */
	static JPanel j;
	/**
	 * Constructor for the model class.
	 * Takes in parameters for objects created in view.
	 * Instantiates initial keystrokes
	 * @param Player p
	 * @param Background b
	 * @param JPanel j
	 * @param Quiz quiz
	 */
	public Model(Player p, Background b, JPanel j, Quiz quiz) {

		player = p; //Contains all information on its image and current health
		background = b;
		bkgrndSpeed = background.getFrameWidth()/128;
		bkgrndSpeedLmt = background.getFrameWidth()/64;
		this.quiz=quiz;
		this.j = j;
		
		
		
		//Set up tutorial
		setTutorial();
	}
	
	
	
	/**
	 *Moves the player down one height
	 */
	public void moveDown() {
		player.moveDown();
	}
	
	/**
	 *Moves the player up one height
	 */
	public void moveUp() {
		player.moveUp();
	}

	/**
	 * Create the Tutorial
	 */
	public void setTutorial() {
		tutorial = new ArrayList<Obstacle>();
		xpositions = new ArrayList<Integer>();
		tutorialCount = 0;
		tutorial.add(new Trash(2, background.getFrameHeight()));
		tutorial.add(new Trash(4, background.getFrameHeight()));
		tutorial.add(new Food(2, background.getFrameHeight()));
		tutorial.add(new Food(4, background.getFrameHeight()));
		tutorial.add(new GoldenFish(2, background.getFrameHeight()));
		tutorial.add(new Trap(4, background.getFrameHeight()));

		int xbarInterval = background.getFrameWidth()/3;
		for (int i = 0; i < tutorial.size(); i++) {
			xpositions.add(background.getFrameWidth() + 2*i*xbarInterval);
		}
		xpositions.set(xpositions.size()-1, xpositions.get(xpositions.size()-1)+2*xbarInterval);
	}
	/**
	 * Updates the positions in Tutorial
	 */
	public void updateTutorial() {
		if (xpositions.get(xpositions.size()-1)+300 > 0) {
			for (int i = 0; i < xpositions.size(); i++) {
				xpositions.set(i, xpositions.get(i)-Model.bkgrndSpeed);
			}
			tutorialCount+=Model.bkgrndSpeed;
		}

		for (int i = 0; i < tutorial.size(); i++) {
			if (xpositions.get(i) < player.imgPosition()) {
				if ((player.getHeight() == tutorial.get(i).getHeight())) { 
					tutorialHit(i);
				}	
			}
		}


	}
	/**
	 * Hits the player in tutorial
	 */
	public void tutorialHit(int i) {
		tutorial.get(i).hit(player);
		//System.out.println("Help me");
		tutorial.get(i).setInvisible();
		//System.out.println(tutorial.get(i).getVisible());
		player.addHealth(5);
		player.setPoints(0);
		tutorial.remove(i);
		xpositions.remove(i);
		player.setVincible();
	}


	/**
	 * Checks the users quiz answer for correctness.
	 * If it's incorrect there's a game over.
	 * It it's correct they gain points/health
	 * @param int pick
	 * @return Mode mode
	 */
	public Mode checkAnswer(int pick) {
		//		System.out.println("pick: "+pick);
		//		System.out.println("answer: " + quiz.getAnswer());
		if(pick==quiz.getAnswer()) {
			mode=Mode.CORRECT;
			player.addPoints(100);//Arbitrary for now, should be value for the Trap points
			if(player.getHealth()!=maxHealth) {
				player.addHealth(1);
			}
			Trap.timer.cancel();
		}else {
			player.addHealth(-3);
			mode=Mode.WRONG;
			Trap.timer.cancel();
		}
		return mode;
	}

	/**
	 * Checks if the mode is quiz. Depending on value of countQuiz it goes to mode wait.
	 * @param none
	 * @return Mode mode
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * Checks if the mode is quiz. Depending on value of countQuiz it goes to mode wait.
	 * @param none
	 * @return Quiz quiz
	 */
	public Quiz getQuiz() {
		return quiz;
	}

	/**
	 * Updates location of player/background
	 * Tests to see if game is overbase on how many obstacles have been used.
	 * Tests for hit detection
	 * @param none
	 * @return void
	 */
	public void updateLocationAndDirection() {
		//hitDetection(), if true
		if (mode == Mode.WRONG) {
			Trap.timer.cancel();
		}
		switch(mode) {
		case FORWARDFACT:
		case FORWARD:
			background.updateBackground(player.getPoints());
			hitDetection(false);
			Controller.resetKeys();
			break;
		case START:
			Controller.resetKeys();
			break;
		case TUTORIALFACT:
		case TUTORIALSTART:
			updateTutorial();
			Controller.resetKeys();
		default:
			Controller.resetKeys();
			break;
		}
		if (mode != Mode.FORWARDFACT && mode != Mode.TUTORIALFACT) {
			counter = 0;
		}
		if (mode == Mode.TUTORIALFACT) {
			counter = (counter + 1) % count;
			if (counter == 0)
				mode = Mode.TUTORIALSTART;
			if (counter == 1)
				quiz.determineFact();
		}
		if (mode == Mode.FORWARDFACT) {
			counter = (counter + 1) % count;
			if (counter == 0)
				mode = Mode.FORWARD;
			if (counter == 1)
				quiz.determineFact();
		}
	}

	/**
	 * Ends the game
	 * @param int pScore player score
	 */
	public static void endGame(int pScore) {

		Model.mode = Mode.END;

		ArrayList<Integer> scores = new ArrayList<Integer>();
		int maxScore = 0;

		try{

			FileInputStream in = new FileInputStream("scores.txt");
			Scanner sc = new Scanner(in);

			while(sc.hasNextInt()){
				scores.add(sc.nextInt());
			}

			maxScore = Collections.max(scores);

			if(maxScore < pScore){
				maxScore = pScore;
			}

		}
		catch(IOException ex){
			System.out.println("Scoreboard not already created, will create new scoreboard");
			maxScore = pScore;
		}

		highScore = maxScore;
		scores.add(pScore);

		try{   
			// Create new scoreboard
			BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt"));

			for(Integer i: scores){
				writer.write(i + "\n");
			}

			writer.close();

			System.out.println("Scoreboard has been updated");
		}	         
		catch(IOException ex){
			System.out.println("IOException is caught");
		}

	}

	/**
	 * Resets the game
	 * @return void
	 */
	public void resetGame() {
		bkgrndSpeed = background.getFrameWidth()/128;
		count = 100;
		counter = 0;
		countQuiz=0;
		background=new Background();
		player = new Player(background.getFrameHeight(), background.getFrameWidth());
	}
	
	/**
	 * Setter, changes the mode. Used for testing purposes.
	 * @param Mode mode
	 * @return void
	 */
	public void setMode(Mode mode) {
		this.mode=mode;
	}
	/**
	 * Setter, changes countQuiz. Used for testing purposes.
	 * @param countQuiz
	 * @return void
	 */
	public void setCountQuiz(int countQuiz) {
		this.countQuiz=countQuiz;
	}
	/**
	 * Getter, gets the players score
	 * @param none
	 * @return int points
	 */
	public int getScore() {
		return player.getPoints();
	}
	/**
	 * Getter, gets the players health
	 * @param none
	 * @return int health
	 */
	public int getHealth() {
		return player.getHealth();
	}
	/** For each obstacle in background's closest Xbar, 
	 * check to see if it hits, use the set +- range in each obstacle, 
	 * if true, run hit(), I still want to run hit() in Obstacle, but it can be done in model
	 * 		be sure to set Player invincible to true when hit,
	 * 		add value, be ready to set players health to max or isAlive to false
	 * 		add points, could be zero for Trash, just add to player points
	 * else do nothing
	 * 
	 * @param boolean test
	 * @return void
	 */
	public void hitDetection(boolean test) {

		if ((background.getClosest().getXpos() < player.imgPosition() && !player.getInvincible())||test) {
			for (Obstacle o : background.getClosest().getObstacles()) {
				if ((player.getHeight() == o.getHeight())||test) { 
					o.hit(player);
					o.setInvisible();
				}	
			}
		} else if (background.getClosest().getXpos() >= player.imgPosition()){
			player.setVincible();
		}	

		//We want access to a private method that will kill the crab
		//Important to restrict access to these methods with private 
		//We control when the crab dies inside the player class
		//player.checkAliveStatus();

		if(player.getHealth()<=0) {
			mode=Mode.DIE;
		}
	}
	/**
	 * Getter, gets player object
	 * @return Player player
	 */
	public Player getPlayer() {return player;}
	/**
	 * Getter, gets background
	 * @return Background background
	 */
	public Background getOurBackground() {return background;}

}
