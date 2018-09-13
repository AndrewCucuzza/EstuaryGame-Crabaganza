
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

//import View.Animated;

/**
 * View class controls everything the user sees on screen.
 * Updated with information from model.
 * @author crystal
 *
 */

class View extends JFrame {

	/**Background object to display ocean floor*/
	Background background = new Background();
	/**Player object to display crab*/
	Player player = new Player(background.getFrameHeight(), background.getFrameWidth());
	/**Temporary Image*/
	BufferedImage temp;

	/**Frame object*/
	static JFrame frame = new JFrame();
	/**JPanel object*/
	static JPanel panel = new JPanel();
	/**New quiz object to show quiz questions*/
	Quiz quiz = new Quiz();
	Random rand = new Random();
	/**String for the question being shown*/
	String currentQuestion;
	/**Inital mode is start*/
	Mode mode = Mode.START;
	/**Font for text, subject to change*/
	Font font = new Font(Font.DIALOG,1,30);
	/**Font for text, subject to change*/
	Font scoreFont = new Font("Comic Sans MS",1,50);
	/**Font for quiz questions*/ 
	Font quizFont = new Font("Comic Sans MS",1,50);
	/**Used to ensure only one question is shown at a time*/
	int trackQuestion=0;
	/**Used to track crab animations*/
	int frameNum = 0;
	/**Used to track score*/
	int score;
	/**Used to track health*/
	int health;
	/**Used to track crab animations at the end of the game*/
	int drawEnd=100;
	/**String for the fact shown*/
	static String fact;
	/**Tracks location for other crabs shown at the end of the game*/
	int otherCrabLocation= background.getFrameWidth();


	/**
	 * Animated class extends DrawPanel and is used to repaint screen
	 * What's painted depends on the mode
	 * @author crystal
	 *
	 */
	public class Animated extends DrawPanel {


		/**
		 * For now none of our images are animated and can just be translated across the screen
		 * Background and player will hold the arguments for the images, created in their constructors
		 * 
		 */
		public Animated() {

		}
		/**
		 * Paint method that displays on the frame
		 * Depends on the mode
		 * Start shows the crab and obstacles.
		 * Die shows the dead crab, encourages player to try again.
		 * End shows the crab meeting up with other crabs. Diplays points.
		 * Forward shows the player moving and displays obstacles.
		 * Quiz shows quiz question and choices
		 * Correct tells player they got answer correct.
		 * Wrong tells play they got the question wrong, and what the correct answer was.
		 * @param Graphics g
		 * @return void
		 * 
		 */
		@Override
		public void paint(Graphics g) {

			switch(mode) {
			case TUTORIALFACT:
			case TUTORIALSTART:
			case START:
				g.drawImage(background.getBackground(), 0, 0, Color.gray, this);

				// Draw top logo
				int logoOffset = (background.getFrameWidth() - player.getCrabImage().getWidth(null))/2;
				g.drawImage(player.getCrabImage(),logoOffset,0,this);

				// Draw bottom arrows
				int bottomOffset = (background.getFrameWidth() - player.getBottomImage().getWidth(null))/2;
				int bottomHeight = player.getBottomImage().getHeight(null);
				g.drawImage(player.getBottomImage(), bottomOffset,background.convertHeight(5, bottomHeight),this);

				if (Model.mode == Mode.START) {
					// Draw middle images
					g.drawImage(player.getFoodImage(), 300, background.convertHeight(2, player.getFoodImage().getHeight(null)), this);
					g.drawImage(player.getTrashImage(), 300, background.convertHeight(3, player.getTrashImage().getHeight(null)), this);
					g.drawImage(player.getQuizImage(), 300, background.convertHeight(4, player.getTrashImage().getHeight(null)), this);
				}
				if (Model.mode != Mode.START) {
					int i = Model.xpositions.size();

					if (Model.tutorialCount < 4*background.getFrameWidth()/3) {
						g.setFont(quizFont);
						g.setColor(Color.red);
						g.drawString("Avoid harmful red trash.",  
								(int)(background.getFrameWidth()/4.5), 
								background.convertHeight(3, 0));
					} else if (Model.tutorialCount < 7*background.getFrameWidth()/3) {
						g.setFont(quizFont);
						g.setColor(Color.green);
						g.drawString("Collect beneficial green food for health and points.",  
								(int)(background.getFrameWidth()/4.5), 
								background.convertHeight(3, 0));
					} else  {
						if (Model.mode != Mode.TUTORIALFACT) {
							g.setFont(quizFont);
							g.setColor(Color.yellow);
							g.drawString("1) Collect GoldenFish to learn facts and earn points.",  
									(int)(background.getFrameWidth()/4.5), 
									background.convertHeight(3, 0));
						}

						g.setFont(quizFont);
						g.setColor(Color.yellow);
						g.drawString("2) Enter a Trap to answer a quiz (answer with 1,2,3 keys) and earn points!",  
								(int)(background.getFrameWidth()/4.5), 
								background.convertHeight(4, 0));

					}
				}

				if (Model.mode == Mode.TUTORIALSTART || Model.mode == Mode.TUTORIALFACT) {
					// Draw a bunch of tutorial images;
					for (int i = 0; i < Model.tutorial.size(); i++) {

						if (Model.tutorial.get(i).getVisible()) {
							g.drawImage(Model.tutorial.get(i).getImage(), Model.xpositions.get(i), 
									background.convertHeight(Model.tutorial.get(i).getHeight(), Model.tutorial.get(i).getImgHeight()), this);
						}
					}
					if (Model.mode == Mode.TUTORIALFACT) {
						g.setFont(quizFont);
						g.setColor(Color.white);
						int len = 36;
						String question = quiz.getFact();
						String present = "";
						String extra = "";
						int rows = 0;
						int z;
						for (int i = 0; i < question.length(); rows++) {
							if (i+len >= question.length()) {
								g.drawString(question.substring(i, question.length()),  
										(int)(background.getFrameWidth()/4.5), 
										background.convertHeight(2, 0)+rows*background.getFrameHeight()/35);
								i = question.length();
							} else {
								present = question.substring(i, len+i);
								z = present.lastIndexOf(" ");
								if (z != -1) {
									present = present.substring(0, z);
									g.drawString(present,  
											(int)(background.getFrameWidth()/4.5), 
											background.convertHeight(2, 0)+(rows*background.getFrameHeight()/35));
									i += z;
								} else {
									g.drawString(present,  
											(int)(background.getFrameWidth()/4.5), 
											background.convertHeight(2, 0)+(rows*background.getFrameHeight()/35));
									i+=len;
								}

							}
						}
						//g.drawString(quiz.getFact(), (int)(background.getFrameWidth()/4.5),160);
						g.setColor(Color.black);
					}
				}

				g.drawImage(player.getImage(frameNum), player.getXloc(), 
						background.convertHeight(player.getHeight(), player.getImgHeight()), this);	
				break;
			case DIE:
				g.drawImage(background.getBackground(), 0, 0, Color.gray, this);
				g.drawImage(player.getDeadImage(),(int)(background.getFrameWidth()/4),50,this);
				break;
			case END:
				g.drawImage(background.getBackground(), background.getBkgrndPos(), 0, Color.gray, this);
				if(drawEnd<(background.getFrameWidth()/7)) {
					otherCrabLocation-=8;
					drawEnd+=8;		
					frameNum++;
				}else {
					g.setColor(Color.BLACK);
					g.fillRect((int)(background.getFrameWidth()/4)-10,(int)(background.getFrameHeight()/4)-10,(background.getFrameWidth()/2)+20,(background.getFrameHeight()/2)+20);
					g.setColor(Color.WHITE);
					g.fillRect((int)(background.getFrameWidth()/4),(int)(background.getFrameHeight()/4),(background.getFrameWidth()/2),(background.getFrameHeight()/2));
					g.setColor(Color.BLACK);
					g.setFont(font);
					g.drawString("Congratulations!!!", (int)(background.getFrameWidth()/4)+20, (int)(background.getFrameHeight()/4)+40);
					g.drawString("Your Score: " + score, (int)(background.getFrameWidth()/4)+20, (int)(background.getFrameHeight()/4)+70);
					g.drawString("High Score: " + Model.highScore, (int)(background.getFrameWidth()/4)+20, (int)(background.getFrameHeight()/4)+100);
					g.drawString("Press ENTER to continue", (int)(background.getFrameWidth()/4)+20, (int)(background.getFrameHeight()/4)+150);					

				}




				for(int i=0; i < 3; i++) {
					g.drawImage(player.getLeftImage(frameNum), otherCrabLocation, 
							(i)*background.getHeightInterval()+50, this);

				}
				//draw the player

				g.drawImage(player.getImage(frameNum), drawEnd, 
						background.convertHeight(player.getHeight(), player.getImgHeight()), this);

				//draw the score
				g.setFont(scoreFont);
				g.setColor(Color.white);
				g.drawString("Score: " + score, (int)(background.getFrameWidth()/2)-100,35);
				g.drawImage(player.getHealthImage(), (int)((background.getFrameWidth()/2)-100), 
						50, this);
				break;


			case FORWARDFACT:	
				//draw the background
				drawBackground(g);

				//draw the score
				g.setFont(quizFont);
				//draw the player
				g.drawImage(player.getImage(frameNum), player.getXloc(), 
						background.convertHeight(player.getHeight(), player.getImgHeight()), this);
				frameNum++;
				g.setColor(Color.white);
				int len = 36;
				String question = quiz.getFact();
				String present = "";
				String extra = "";
				int rows = 0;
				int z;
				for (int i = 0; i < question.length(); rows++) {
					if (i+len >= question.length()) {
						g.drawString(question.substring(i, question.length()),  
								(int)(background.getFrameWidth()/4.5), 
								160+rows*background.getFrameHeight()/35);
						i = question.length();
					} else {
						present = question.substring(i, len+i);
						z = present.lastIndexOf(" ");
						if (z != -1) {
							present = present.substring(0, z);
							g.drawString(present,  
									(int)(background.getFrameWidth()/4.5), 
									160+(rows*background.getFrameHeight()/35));
							i += z;
						} else {
							g.drawString(present,  
									(int)(background.getFrameWidth()/4.5), 
									160+(rows*background.getFrameHeight()/35));
							i+=len;
						}

					}
				}
				//g.drawString(quiz.getFact(), (int)(background.getFrameWidth()/4.5),160);
				g.setColor(Color.black);
				break;
			case FORWARD:
				//draw the background
				drawBackground(g);

				//draw the score
				g.setFont(scoreFont);
				g.setColor(Color.white);						
				//draw the player
				g.drawImage(player.getImage(frameNum), player.getXloc(), 
						background.convertHeight(player.getHeight(), player.getImgHeight()), this);
				frameNum++;
				break;
			case QUIZ:		
				drawBackground(g);
				if(trackQuestion==0) {
					quiz.setNextQuiz();
				}
				currentQuestion=quiz.showQuiz(g, player, background);

				trackQuestion=1;

				break;
			case CORRECT:
				trackQuestion=0;
				drawBackground(g);
				g.drawImage(player.getImage(frameNum), player.getXloc(), 
						background.convertHeight(player.getHeight(), player.getImgHeight()), this);

				trackQuestion=0;
				g.setFont(font);
				g.setColor(Color.black);
				g.fillRect(background.getFrameWidth()/2-20, background.getFrameHeight()/3-20, 
						background.getFrameWidth()/3+40, background.getFrameHeight()/2+40);
				g.setColor(Color.white);
				g.fillRect(background.getFrameWidth()/2, background.getFrameHeight()/3, 
						background.getFrameWidth()/3, background.getFrameHeight()/2);
				g.setColor(Color.black);
				g.drawString("Correct! Press ENTER to continue", 
						background.getFrameWidth()/2+10, (background.getFrameHeight()/3)+(background.getFrameHeight()/40));

				break;
			case WRONG:
				trackQuestion=0;
				drawBackground(g);
				trackQuestion=0;
				g.setFont(font);

				g.drawImage(player.getCrabTrap(), player.getXloc(), 
						background.convertHeight(player.getHeight(), player.getImgHeight()), this);

				g.setColor(Color.black);
				g.fillRect(background.getFrameWidth()/2-20, background.getFrameHeight()/3-20, 
						background.getFrameWidth()/3+40, background.getFrameHeight()/2+40);
				g.setColor(Color.white);
				g.fillRect(background.getFrameWidth()/2, background.getFrameHeight()/3, 
						background.getFrameWidth()/3, background.getFrameHeight()/2);
				g.setColor(Color.black);

				//quiz.keepShowingQuiz(g, currentQuestion);
				g.drawString("Wrong :( Correct answer is number "+ quiz.getAnswer(), 
						background.getFrameWidth()/2+10, (background.getFrameHeight()/3)+
						(background.getFrameHeight()/40)+(0*background.getFrameHeight()/35));
				g.drawString("Press ENTER to continue", 
						background.getFrameWidth()/2+10, (background.getFrameHeight()/3)+
						(background.getFrameHeight()/40)+(1*background.getFrameHeight()/35));

				g.drawImage(player.getHealthImage(), (int)((background.getFrameWidth()/2)-100), 
						50, this); 

				break;

			default: 
				break;
			}

		}
	}
	/**
	 * Draws background image, obstacles, score, health bar
	 * @param g
	 */
	public void drawBackground(Graphics g) {
		g.drawImage(background.getBackground(), background.getBkgrndPos(), 0, Color.gray, this);
		for (XBar xb : background.getVisible()) {
			for (Obstacle o : xb.getObstacles()) {
				if (o.getVisible()) {
					g.drawImage(o.getImage(), xb.getXpos(), background.convertHeight(o.getHeight(), o.getImgHeight()), this);
				}
			}
		}
		//draw the score
		g.setFont(scoreFont);
		g.setColor(Color.white);
		g.drawString("Score: " + score, (int)(background.getFrameWidth()/2)-100,35);
		g.drawImage(player.getHealthImage(), (int)((background.getFrameWidth()/2)-100), 
				50, this); 
	}
	/**
	 * Class draw panel extends JPanel
	 * Used for displaying background and player animations
	 *
	 */
	@SuppressWarnings("serial")
	private class DrawPanel extends JPanel {
		int picNum = 0;

		//drawPanel.add(b);
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
		}
	}
	public Dimension getPreferredSize() {
		return new Dimension(background.getFrameWidth(), background.getFrameHeight());
	}

	/**
	 * View constructor.
	 * Sets up frame and panel
	 */
	public View() {
		frame.getContentPane().add(new Animated());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(background.getFrameWidth(), background.getFrameHeight());    	    
		frame.setVisible(true);
		panel.setBackground(Color.white); //Yellow is shown during quiz
		//panel.add(b);
		frame.add(panel);
		frame.setFocusable(false);
		if (background.getFrameHeight() < 1300) {
			quizFont = new Font("Comic Sans MS",1,20);
			scoreFont = new Font("Comic Sans MS",1,20);
		}

	}

	/**
	 * Takes the new player and background from Model and resets
	 * @param Background b
	 * @param Player p
	 */
	public void resetGame(Background b, Player p) {
		otherCrabLocation= background.getFrameWidth();
		drawEnd=100;
		background = b;
		player = p;
		quiz.resetQuiz();;
	}
	/**
	 * Overrides writeObject method
	 * @param s
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
//		s.writeObject(mode);
//		s.writeInt(player.getHeight());
//		s.writeInt(player.getXloc());
	}
	/**
	 * Overrides readObject method
	 * @param s
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
//		mode = (Mode) s.readObject();
//		pHeight=(s.readInt()); 
//		pLoc=(s.readInt());

	}
	/**
	 * Overrides view toString() method
	 * used with serializable to see what state was saved
	 */
	public String toString() {
		return ("Mode: " + mode + " height: " + player.getHeight() + " xloc: " + player.getXloc() + " Score: " + score);
	}
	/**
	 * Getter, gets the panel
	 * @param none
	 * @return JPanel panel
	 */
	public JPanel getPanel() {return panel;}
	/**
	 * Getter, gets player
	 * @return Player player
	 */
	public Player getPlayer() {return player;}
	/**
	 * Getter, gets background
	 * @return Background background
	 */
	public Background getOurBackground() {return background;}

	/**
	 * Getter, gets quiz
	 * @return Quiz quiz
	 */
	public Quiz getQuiz() {
		return quiz;
	}

	/**
	 * Updates view based on information from model.
	 * @param Mode mode
	 * @param int score
	 * @param int health
	 * @return void
	 */
	public void update(Mode mode,int score,int health) {
		this.mode=mode;
		this.score=score;
		this.health=health;

		frame.repaint();
		try {
			Thread.sleep(0);		
		} 
		catch (InterruptedException e) {

			e.printStackTrace();

		}
	}
}
