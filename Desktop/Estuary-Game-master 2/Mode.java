
/**
 * Mode enumerator
 * Gives different modes of the game
 * @author cryst
 *
 */
public enum Mode {
	DIE("die"),
	START("start"),
	TUTORIALSTART("tutorialstart"),
	TUTORIALFACT("tutorialfact"),
	END("end"),	
	FORWARD("forward"),
	FORWARDFACT("forwardfact"),
	QUIZ("quiz"),
	CORRECT("correct"),
	WRONG("wrong"),
	WAIT("wait"),
	RESET("reset");

	private String name = null;

	private Mode(String s){
		name = s;
	}
	public String getName() {
		return name;
	}
}
