package run;

import framework.Browser;
import framework.FrameworkParameters;
import framework.Settings;
import framework.TestParameters;
import framework.Util;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import framework.ParallelRunner;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class runmanager {
	private static FrameworkParameters framework_Parameters = FrameworkParameters.getInstance();
	static List<TestParameters> testInstanceToRun = new ArrayList<TestParameters>();

	public static String fileName;
	public static String projectName;
	public static int numberOfTestCases;
	static Properties props = new Properties();
	static String configFile = "config.properties";

	public static void main(String[] args) {
		try {
			setProperties(args);
			FileInputStream fis = new FileInputStream(configFile);
			props.load(fis);
			String sheetName = props.getProperty("inputFileSheet");
			int numberOfThreads = Integer.parseInt(props.getProperty("NumberOfThreads"));

			execute(fileName, sheetName, numberOfThreads);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setProperties(String[] args) {

		fileName = (fileName == null) ? "Test Manager.xlsm" : fileName;
		framework_Parameters.setRelativePath();
		Settings.setCommandLineProperties(args);
		props = Settings.getInstance();
		System.out.println("Properties------> " + props);
		projectName = props.getProperty("ProjectName");
		System.setProperty("ProjectName", projectName);
	}

	private static void execute(String runFile, String fileSheet, int nThreads) {

		String path = FrameworkParameters.getTestManagerPath();
		List<TestParameters> instanceToRun = getRunInfo(path + Util.getFileSeparator() + runFile, fileSheet);

		numberOfTestCases = instanceToRun.size();

		ExecutorService parallelRunner = Executors.newFixedThreadPool(nThreads);
		ParallelRunner testRunner = null;

		for (int instance = 0; instance < instanceToRun.size(); instance++) {
			testRunner = new ParallelRunner(instanceToRun.get(instance));
			parallelRunner.execute(testRunner);

			if (framework_Parameters.getStopExecution()) {
				break;
			}
		}

		parallelRunner.shutdown();
		while (!parallelRunner.isTerminated()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException a) {
				a.printStackTrace();
			}
		}

		if (testRunner == null) {
			return;
		} else {
			System.out.println("Execution is complete for all test cases.");
		}

	}

	public static List<TestParameters> getRunInfo(String runFile, String fileSheet) {
		try {
			Fillo fillo = new Fillo();
			Connection connection = fillo.getConnection(runFile);
			try {

				Recordset record = null;
				record = connection.executeQuery("Select * from " + fileSheet);

				if (record == null) {
					return null;
				}

				while (record.next()) {
					String run = record.getField("Run");
					if (run.equalsIgnoreCase("Yes")) {
						String scenarioToRun = record.getField("TestScenario");
						String testCaseToRun = record.getField("TestCaseName");
						System.out.println("Runnning: " + scenarioToRun + "." + testCaseToRun);
						TestParameters testParameters = new TestParameters(scenarioToRun, testCaseToRun);

						String browser = record.getField("Browser");
						if (!"".equals(browser)) {
							testParameters.setBrowser(Browser.valueOf(browser));
						}
						testInstanceToRun.add(testParameters);
					}
				}
				record.close();
				connection.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				connection.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return testInstanceToRun;
	}

}
