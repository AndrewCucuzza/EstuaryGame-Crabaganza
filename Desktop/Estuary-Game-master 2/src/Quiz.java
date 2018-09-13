
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/**
 * The Quiz Class is for reading in the text file of questions and creating quizzes for the user.
 * 
 * @author crystal
 *
 */

public class Quiz implements java.io.Serializable{

	/**
	 * List of questions
	 */
	ArrayList<String> questions = new ArrayList<String>();
	/**
	 * List of all questions, answers, and choices
	 */
	ArrayList<String> lines = new ArrayList<String>();
	/** Used to store trivia facts */
	ArrayList<String> lines2 = new ArrayList<String>();

	/**
	 * Map with questions as keys and number of answer and values
	 */
	Map answers = new HashMap();
	/**
	 * Map with questions as keys and choices as values
	 */
	Map choices = new HashMap();
	/**
	 * Object of random needed for randomly picking questions
	 */
	Random rand;
	/**
	 * Contains the answer to a given question.
	 * Used to check for user correctness
	 */
	int answer;
	/**
	 * Number of choices for each question
	 */
	final int numChoices=3;
	/**
	 * Number of possible questions in total
	 */
	final int numQuestions=20;
	/**
	 * Number of questions already used.
	 * Used to track if all questions have been used already
	 */
	int questionsUsed = 0;
	/** Used to track trivia used */
	int triviaUsed = 1;
	/**
	 * int array used to track what trivia have already been used
	 * Each block in the array represents a trivia fact
	 */
	int[] used = new int[numQuestions];
	/**
	 * int array usedQ to track what questions have already been used
	 * Each block in the array represents a question
	 */
	int[] usedQ = new int[numQuestions];
	/**
	 * Random number
	 */
	int q;
	/**
	 * Allows us to track time left on the quiz
	 */
	static int quizTime = 0;
	/**
	 * Font for showing questions
	 * Subject to change
	 */
	Font font = new Font("Comic Sans",1,50);
	/** Gets corresponding question */
	int corrQues;
	/** Placeholder for a fact */
	String fact = "";
	/**
	 * Getter, gets the answer
	 * @param none
	 * @return int, answer to a given question
	 */
	public int getAnswer() {
		return answer;
	}

	/**
	 * Determines what fact to present
	 * @param none
	 * @return none
	 */
	public void determineFact() {
		corrQues = rand.nextInt(numQuestions);
		if(triviaUsed==numQuestions) {
			triviaUsed=0;
			Arrays.fill(used, 0);
		}
		while(used[corrQues]==1) {
			corrQues = rand.nextInt(numQuestions);
		}
		used[corrQues]=1;
		triviaUsed++;
		fact = lines2.get(corrQues);
		/*
		int len = 20;
		String present;
		String result = "";
		int z = 0;
		for (int i = 0; i < fact.length(); ) {
			if (i+len >= fact.length()) {
				result += fact.substring(i, fact.length());
				i = fact.length();
			} else {
				present = fact.substring(i, len+i);
				z = present.lastIndexOf(" ");
				result += present.substring(0, z);
				result+="\n";
				i+=z;
				System.out.println(result + "a");
			}
		}
		System.out.println(result);
		fact = result;*/
	}

	/**
	 * Allows access to the fact
	 * @param none
	 * @return returns the choosen fact
	 */
	public String getFact() {
		return fact;
	}

	/**
	 * 
	 * Constructor, reads in questions
	 * Finds questions/answers/choices
	 * @param none
	 * @return none
	 */
	public Quiz() {
		rand = new Random();
		used[0]=1; //have at least 1 quiz question available
		readFile("questions/questions.txt", lines);
		createQuiz();
		readFile("facts/facts.txt", lines2);
	}

	/**
	 * Finds out how many questions there are
	 * @param none
	 * @return int, size of questions arrayList
	 */
	public int numQuestions() {
		return questions.size();
	}

	/**
	 * Continues showing the same quiz if the user answer wrong
	 * @param Graphics g
	 * @param String question
	 * @return void
	 */
	public void keepShowingQuiz(Graphics g,String question) {
		g.setFont(font);
		g.drawString(question, 10, 50);
		String[] options = (String[]) choices.get(question);
		answer=(int) answers.get(question);
		for(int i=0;i<numChoices;i++) {
			g.drawString(options[i], 10, 100+i*75);
		}
	}

	/**
	 * If all questions have been used, each question is reset to haven not been chosen.
	 * A random question is chosen until one that has not been chosen yet is picked.
	 * The quiz is displayed on screen
	 * Formats questions to fit in box on side of screen
	 * @param Graphics g
	 * @return String question
	 */
	public String showQuiz(Graphics g, Player p, Background b) {
		if (b.getFrameHeight() < 1300) {
			font = new Font("Comic Sans",1,20);
		}
		g.drawImage(p.getCrabTrap(), p.getXloc(), 
				b.convertHeight(p.getHeight(), p.getImgHeight()), null);

		g.setColor(Color.black);
		g.fillRect(b.getFrameWidth()/2-20, b.getFrameHeight()/3-20, b.getFrameWidth()/3+40, b.getFrameHeight()/2+40);
		g.setColor(Color.white);
		g.fillRect(b.getFrameWidth()/2, b.getFrameHeight()/3, b.getFrameWidth()/3, b.getFrameHeight()/2);
		g.setColor(Color.black);
		String test = "";
		/* Test the font on your screen
		 * How many characters fit in a line?*/
		int len = 36;
		for (int i = 0; i < len; i++) {
			test+="a";
		}

		g.setFont(font);
		//g.drawString(test, b.getFrameWidth()/2, b.getFrameHeight()/3+b.getFrameHeight()/45);

		g.setColor(Color.black);
		g.fillRect(4*b.getFrameWidth()/5-10, b.getFrameHeight()/3+20-10, b.getFrameWidth()/35+20, b.getFrameHeight()/2-40+20);
		double total = b.getFrameHeight()/2-40;
		double decrement = ((double) Quiz.quizTime/15000)*((double)b.getFrameHeight()/2-40);
		if ((double) Quiz.quizTime/15000 < 0.33) {
			g.setColor(Color.green);
		} else if ((double) Quiz.quizTime/15000 < 0.66) {
			g.setColor(Color.yellow);
		} else {
			g.setColor(Color.red);
		}
		g.fillRect(4*b.getFrameWidth()/5, (int) (b.getFrameHeight()/3+20 + decrement),
				b.getFrameWidth()/35, (int) (total-decrement));
		g.setColor(Color.black);




		//String question = "The code I wrote won't account for a complete word. Implementing new lines in our file would be best.";
		//To test the question
		String question = questions.get(q);
		String present = "";
		String extra = "";
		int rows = 0;
		int z;
		for (int i = 0; i < question.length(); rows++) {
			if (i+len >= question.length()) {
				g.drawString(question.substring(i, question.length()),  
						b.getFrameWidth()/2+10, b.getFrameHeight()/3+b.getFrameHeight()/40+rows*b.getFrameHeight()/35);
				i = question.length();
			} else {
				present = question.substring(i, len+i);
				z = present.lastIndexOf(" ");
				if (z != -1) {
					present = present.substring(0, z);
					g.drawString(present,  
							b.getFrameWidth()/2+10, (b.getFrameHeight()/3)+(b.getFrameHeight()/40)+(rows*b.getFrameHeight()/35));
					i += z;
				} else {
					g.drawString(present,  
							b.getFrameWidth()/2+10, (b.getFrameHeight()/3)+(b.getFrameHeight()/40)+(rows*b.getFrameHeight()/35));
					i+=len;
				}

			}
		}
		g.setFont(font);
		//g.drawString(questions.get(q), 10, 50);
		String[] options = (String[]) choices.get(questions.get(q));
		answer=(int) answers.get(questions.get(q));
		//		System.out.println(x);
		//		System.out.println(questions.get(q));
		//		System.out.println(answers);
		present = "";
		extra = "";
		z=0;
		for(int j=0;j<numChoices;j++) {
			question = options[j];
			present = "";
			extra = "";
			for (int i = 0; i < question.length(); rows++) {
				if (i+len >= question.length()) {
					g.drawString(question.substring(i, question.length()),  
							b.getFrameWidth()/2+10, b.getFrameHeight()/3+b.getFrameHeight()/40+rows*b.getFrameHeight()/35);
					i = question.length();
				} else {
					present = question.substring(i, len+i);
					z = present.lastIndexOf(" ");
					if (z != -1) {
						present = present.substring(0, z);
						g.drawString(present,  
								b.getFrameWidth()/2+10, (b.getFrameHeight()/3)+(b.getFrameHeight()/40)+(rows*b.getFrameHeight()/35));
						i += z;
					} else {
						g.drawString(present,  
								b.getFrameWidth()/2+10, (b.getFrameHeight()/3)+(b.getFrameHeight()/40)+(rows*b.getFrameHeight()/35));
						i+=len;
					}

				}
			}
		}
		return questions.get(q);
	}
	
	/**
	 * Resets quiz variables for when game restarts
	 */
	public void resetQuiz() {
		quizTime = 0;
		triviaUsed = 1;
		questionsUsed=0;
		Arrays.fill(usedQ, 0);
		Arrays.fill(used, 0);
		used[0]=1;
	}

	/**
	 * Gets the next quiz question
	 */
	public void setNextQuiz() {
		q = rand.nextInt(numQuestions);
		if(numQuestions==questionsUsed || triviaUsed==questionsUsed) {
			questionsUsed=0;
			Arrays.fill(usedQ, 0);
		}
		while(used[q]==0 || usedQ[q]==1) {
			q = rand.nextInt(numQuestions);
		}
		usedQ[q]=1;
		questionsUsed++;
	}

	/**
	 * Goes through al text.
	 * Fills in questions list.
	 * Fills in choices/answers maps.
	 * @param none
	 * @return void
	 */
	public void createQuiz() {
		for(int i=0; i<lines.size(); i+=numChoices+2) {				
			questions.add((String)lines.get(i));
			String[] choice = {(String)lines.get(i+2),(String) lines.get(i+3),(String) lines.get(i+4)};
			choices.put((String)lines.get(i), choice);
			answers.put((String)lines.get(i),Integer.parseInt((String) lines.get(i+1)));
		}	
	}

	/**
	 * Reads in text file of questions.
	 * @param String name
	 * @return void
	 */
	public void readFile(String name, ArrayList<String> test) {
		File file = new File(name);
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st;
			try {
				while ((st = br.readLine()) != null)
					test.add(st);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
