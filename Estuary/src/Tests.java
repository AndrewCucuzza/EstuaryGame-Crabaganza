
import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import org.junit.Test;

public class Tests {

	Player p = new Player(1000, 1000);
	Background b = new Background();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int frameWidth = (int) screenSize.getWidth();
	int frameHeight = (int) screenSize.getHeight();
	View view = new View();
	Model model = new Model(view.getPlayer(), view.getOurBackground(), view.getPanel(),view.getQuiz());

	@Test
	public void testModelInfo() {
		model = new Model(view.getPlayer(), view.getOurBackground(), view.getPanel(),view.getQuiz());
		Background b = model.getOurBackground();
		Player p = model.getPlayer();
		Controller.resetKeys();
		model.hitDetection(false);
		model.setMode(Mode.START);
		model.moveDown();
		model.moveUp();
		assertEquals("Should be start value", Mode.START, model.getMode());
		assertEquals("Should be staring player values", 0,model.getScore());
		assertEquals("Should be staring player values", Model.maxHealth, model.getHealth());
		p.setHealth(0);
		model.hitDetection(false);
		assertEquals("Should be dead", Mode.DIE, model.getMode());
		model.resetGame();
		model.updateLocationAndDirection();
		model.setMode(Mode.START);
		model.updateLocationAndDirection();
		model.setMode(Mode.FORWARD);
		model.updateLocationAndDirection();
		model.setMode(Mode.DIE);
		model.updateLocationAndDirection();
		model.setMode(Mode.QUIZ);
		assertEquals("Should be mode quiz",Mode.QUIZ,model.getMode());
		model.setCountQuiz(1);		
		model.setMode(Mode.QUIZ);
		model.getMode();
		model.hitDetection(true);
		model.updateTutorial();
		
		
		assertEquals("Should be mode correct",Mode.CORRECT,model.checkAnswer(model.getQuiz().corrQues));
		assertEquals("Should be mode wrong",Mode.WRONG,model.checkAnswer(4));
		model.tutorialHit(0);
		model.setMode(Mode.TUTORIALSTART);
		model.updateLocationAndDirection();
		for (int i = 0; i < 200; i++) {
			model.setMode(Mode.FORWARDFACT);
			model.updateLocationAndDirection();
		}
		model.setMode(Mode.FORWARD);
		model.updateLocationAndDirection();
		for (int i = 0; i < 200; i++) {
			model.setMode(Mode.TUTORIALFACT);
			model.updateLocationAndDirection();
		}
		model.setMode(Mode.WRONG);
		model.updateLocationAndDirection();
		model.endGame(0);

	}

	@Test
	public void viewWithModes(){

		model.setMode(Mode.START);
		view.update(Mode.START,model.getScore(),model.getHealth());

		model.setMode(Mode.TUTORIALSTART);
		view.update(model.getMode(),model.getScore(),model.getHealth());

		model.setMode(Mode.TUTORIALFACT);
		view.update(model.getMode(),model.getScore(),model.getHealth());

		model.setMode(Mode.FORWARD);
		view.update(model.getMode(),model.getScore(),model.getHealth());

		model.setMode(Mode.FORWARDFACT);
		view.update(model.getMode(),model.getScore(),model.getHealth());

		model.setMode(Mode.QUIZ);
		view.update(model.getMode(),model.getScore(),model.getHealth());

		model.setMode(Mode.CORRECT);
		view.update(model.getMode(),model.getScore(),model.getHealth());

		model.setMode(Mode.WRONG);
		view.update(model.getMode(),model.getScore(),model.getHealth());

	}


	@Test
	public void morePlayerInfo(){

		assertEquals("Height should be 2", 2, p.getHeight());
		assertEquals("Health should be 5", 5, p.getHealth());
		assertEquals("Score should be 0", 0, p.getPoints());
		assertEquals("x location should be 100", 100, p.getXloc());
		assertEquals("The crab should not be invincible", false, p.getInvincible());
		assertEquals("image position should be 237", 237, p.imgPosition());

	}

	

	@Test
	public void testBackgroundInfo() {

		XBar test = new XBar(3);

		b = new Background();
		b.setVisible();
		int i = b.getHeightInterval();
		assertEquals("Should be ", (1*b.getHeightInterval()) - (0/2), b.convertHeight(1,0));
		assertEquals("Should be 0", 0, b.getBkgrndPos());
		assertEquals("Should be ", test.getClass(), b.getClosest().getClass());
		assertEquals("Should be frameheight", frameHeight, b.getFrameHeight());
		assertEquals("Should be Framewidth", frameWidth, b.getFrameWidth());
		assertEquals("Should be frameheight/5", frameHeight/6, b.getHeightInterval());

	}

	@Test
	public void testView() {
		view.resetGame(b, p);

	}

	@Test
	public void testHitFunctions() {

		Player p = new Player(100, 100);

		GoldenFish gf = new GoldenFish(3,100);
		gf.hit(p);

		Trash t = new Trash(3,100);
		t.hit(p);

		Trap tp = new Trap(3,100);
		tp.hit(p);


	}

	@Test
	public void testObstacles(){

		Obstacle o = new Obstacle();
		String s = o.toString();
		o.setPoints(1);
		o.setValue(1);

		assertEquals("Points should be 1", 1, o.getPoints());
		assertEquals("Value should be 1", 1, o.getValue());

		//assertEquals("Incorrect toString", "Type: , Height: 0, Value = 1", o.toString());

	}

	@Test
	public void testXbarInfo() {
		Background b = new Background();
		XBar closest = b.getClosest();
		XBar[] visible = b.getVisible();
		assertTrue(closest == visible[0]);
		assertEquals("Should have this starting position", frameWidth, closest.getXpos());
		closest.movePosition(1);
		assertEquals("Should have this starting position", frameWidth-1, closest.getXpos());
		closest.setPosition(0);
		assertEquals("Should have this starting position", 0, closest.getXpos());
	}

	@Test
	public void testQuizInfo() {
		Quiz q = new Quiz();
		Random rand = new Random();
		assertEquals("Number of lines on our Questions.txt", 20, q.numQuestions());
		q.determineFact();
		assertNotNull(q.getFact());
		assertNotNull(q.getAnswer());
		q.setNextQuiz();
	}

	@Test
	public void testFileFailure() {
		Quiz q = new Quiz();
		//q.readFile("None");		
	}

	@Test
	public void manipulatePlayer(){
		Image img;
		p.moveDown();
		p.addPoints(10);
		p.moveDown();
		p.moveDown();
		p.moveDown();
		p.moveDown();
		p.moveDown();
		p.moveDown();
		p.moveDown();
		p.moveDown();
		assertEquals("Height should be 1", 1, p.getHeight());
		assertEquals("Points should be 10", 10, p.getPoints());
		p.moveUp();
		p.moveUp();
		p.moveUp();
		p.moveUp();
		p.moveUp();
		p.moveUp();
		p.moveUp();
		p.moveUp();
		p.moveUp();
		p.moveUp();
		assertEquals("Height should be 5", 5, p.getHeight());
		p.setPoints(5);
		assertEquals("Points should be 5", 5, p.getPoints());
		p.setInvincible();
		assertEquals("The crab should be invincible", true, p.getInvincible());
		p.setVincible();
		assertEquals("The crab should not be invincible", false, p.getInvincible());
		p.setHealth(5);
		assertEquals("Health should be 5", 5, p.getHealth());
		img = p.getHealthImage();
		p.setHealth(4);
		assertEquals("Health should be 4", 4, p.getHealth());
		img = p.getHealthImage();
		p.setHealth(3);
		assertEquals("Health should be 3", 3, p.getHealth());
		img = p.getHealthImage();
		p.setHealth(2);
		assertEquals("Health should be 2", 2, p.getHealth());
		img = p.getHealthImage();
		p.setHealth(1);
		assertEquals("Health should be 1", 1, p.getHealth());
		img = p.getHealthImage();
		p.setHealth(2);
		assertEquals("Health should be 2", 2, p.getHealth());
		img = p.getHealthImage();
		p.addHealth(-2);
		assertEquals("Health should be 0", 0, p.getHealth());
		p.checkAliveStatus();
		p.addHealth(5);
		assertEquals("Health should be 5", 5, p.getHealth());
		p.setInvincible();
		p.moveDown();
		p.setInvincible();
		p.moveUp();
		for (int i = 0; i < 3; i++) {
			img = p.getImage(i);
			img = p.getLeftImage(i);
		}
	}

}
