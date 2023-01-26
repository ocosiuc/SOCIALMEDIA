package framework;


/**
 * Class to facilitate parallel execution of test scripts
 * 
 * @author
 */
public class ParallelRunner implements Runnable {

	private final TestParameters testParameters;
	private int testBatchStatus = 0;

	/**
	 * Constructor to initialize the details of the test case to be executed
	 * 
	 * @param testParameters The {@link SeleniumTestParameters} object (passed from
	 *                       the {@link Allocator})
	 */
	public ParallelRunner(TestParameters testParameters) {
		super();
		this.testParameters = testParameters;
	}

	/**
	 * Function to get the overall test batch status
	 * 
	 * @return The test batch status (0 = Success, 1 = Failure)
	 */
	public int getTestBatchStatus() {
		return testBatchStatus;
	}

	//@Override
	public void run() {
		DriverScript driverScript = new DriverScript(this.testParameters);
		TestParameters.currentTC = testParameters.getCurrentTestCase();
		System.out.println("\nExecuting Test Case: " + this.testParameters.getCurrentTestCase());
		driverScript.testExecutor();
	}
}
