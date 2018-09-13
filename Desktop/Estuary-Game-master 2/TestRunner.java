
import org.junit.runner.*;

public class TestRunner{

	public static void main(String[] args){
		JUnitCore junit = new JUnitCore();
		Result result = junit.run(Tests.class);
		System.out.printf("Test ran: %s, Failed: %s%n",
				result.getRunCount(), result.getFailureCount());

	}

}
