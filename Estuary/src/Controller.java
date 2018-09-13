
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import java.awt.EventQueue;
/**
 * Part of MVC model that controls model and view.
 * Implements serializable
 * @author crystal
 *
 */
public class Controller implements java.io.Serializable{

	private static Model model;
	private View view;
	final int drawDelay = 30; //msec
	Action updateProgram;
	Mode tester;
	//JButton b;
	/**
	 * Constructor for controller
	 * Has button presses
	 * Updates program 
	 * Implemments serializable
	 */
	public Controller() {

		view = new View();
		model = new Model(view.getPlayer(), view.getOurBackground(), view.getPanel(),view.getQuiz());
		
		Model.j.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0), "down");
		Model.j.getActionMap().put("up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Model.mode!=Mode.WAIT&&Model.mode!=Mode.QUIZ&&Model.mode!=Mode.CORRECT&&Model.mode!=Mode.WRONG) {
					model.moveUp();
				}
			}
		});
		Model.j.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0), "up");
		Model.j.getActionMap().put("down", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Model.mode!=Mode.WAIT&&Model.mode!=Mode.QUIZ&&Model.mode!=Mode.CORRECT&&Model.mode!=Mode.WRONG) {
					model.moveDown();
				}
			}
		});
		Model.j.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0), "save");
		Model.j.getActionMap().put("save", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Model.save = true;
			}
		});
		Model.j.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T,0), "tutorial");
		Model.j.getActionMap().put("tutorial", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Model.mode == Mode.START)
					Model.mode = Mode.TUTORIALSTART;
			}
		});
		
		updateProgram = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if (model.getMode()!=Mode.RESET) {
					model.updateLocationAndDirection();
					tester=model.getMode();	
					view.update(tester,model.getScore(),model.getHealth());    
				} else {
					model.resetGame();
					view.resetGame(model.getOurBackground(), model.getPlayer());
					Model.mode = Mode.START;
				}
				if(Model.save) {
					Model.save=false;
					// Serialization 
					View v = null;
					try
					{   
						//Saving of object in a file
						ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("outFile.txt"));
						// Method for serialization of object
						//out.writeObject(model);
						out.writeObject(view);
						//out.flush();
						out.close();

						ObjectInputStream in = new ObjectInputStream(new FileInputStream("outFile.txt"));
						//m = (Model) in.readObject();
						//System.out.println(m);
						v = (View) in.readObject();
						System.out.println(v);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}

			}
		};

	}
	
	/**
	 * Key strokes that change purpose throughout game.
	 * Includes keys 1,2,3,enter.
	 * Keys 1,2,3 are used to check answers during quizzes.
	 * Enter is used to change game mode.
	 * @param none
	 * @return void
	 */
	public static void resetKeys() {
		Model.j.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_1,0),"1");
		Model.j.getActionMap().put("1", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(Model.mode==Mode.WAIT||Model.mode==Mode.QUIZ) {
					model.checkAnswer(1);
				}else {
					Model.mode=Mode.FORWARDFACT;
				}        	
			}
		});
		Model.j.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_2,0),"2");
		Model.j.getActionMap().put("2", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Model.mode==Mode.WAIT||Model.mode==Mode.QUIZ) {
					model.checkAnswer(2);
				}else {
					Model.mode=Mode.FORWARDFACT;
				} 
			}
		});
		Model.j.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_3,0),"3");
		Model.j.getActionMap().put("3", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Model.mode==Mode.WAIT||Model.mode==Mode.QUIZ) {
					model.checkAnswer(3);
				}else {
					Model.mode=Mode.FORWARDFACT;
				} 
			}
		});
		Model.j.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"enter");
		Model.j.getActionMap().put("enter", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(Model.mode) {
				case TUTORIALSTART:
				case START:
					Model.mode=Mode.FORWARD;
					break;
				case WRONG:
					Model.mode=Mode.FORWARDFACT;
					break;
				case END:
					Model.mode=Mode.RESET;
					break;
				case DIE:
					Model.mode=Mode.RESET;
					break;
				case CORRECT:
					Model.mode=Mode.FORWARDFACT;
					break;
				default:
					break;
				}
			}
		});
	}	
	/**
	 * main method
	 * creates new controller, starts game
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				Controller c = new Controller();
				Timer t = new Timer(c.drawDelay, c.updateProgram);
				t.start();
			}
		});
	}}	
