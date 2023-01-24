package framework;

import java.io.File;
import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import framework.Util;
//import framework.FrameworkParameters;
//import framework.Settings;
//import framework.WebDriverFactory;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

public class DriverScript {

	private static int currentIteration;
	private int currentSubIteration;
	public static String sActions;
	private final static FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	private static Properties properties;
	private static TestParameters testParameters;
	private static String reportPath;
	private static DataTable dataTable;
	// private WebDriverUtil driverUtil;
	private ScriptHelper scriptHelper;
	private Driver driver;
	private List<String> businessFlowData;

	public DriverScript(TestParameters testParameters) {
		DriverScript.testParameters = testParameters;
	}

	public static Method method[];

	public void testExecutor() {
		startUp();
		initializeWebDriver();
		initializeDatatable();
		initializeTestScript();
		executeTestIterations();
		quitWebDriver();
		wrapUp();
	}

	private void initializeWebDriver() {
		WebDriver webDriver = WebDriverFactory.getWebDriver(testParameters.getBrowser());
		driver = new Driver(webDriver);
		driver.setTestParameters(testParameters);
		WaitPageLoad();
		implicitWaitForDriver();
	}

	private void WaitPageLoad() {
		long pageLoadTimeout = Long.parseLong(properties.get("PageTimeout").toString());
		driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	private void implicitWaitForDriver() {
		long objectSyncTimeout = Long.parseLong(properties.get("PageTimeout").toString());
		driver.manage().timeouts().implicitlyWait(objectSyncTimeout, TimeUnit.SECONDS);
	}

	private void executeTestIterations() {

		try {
			executeTestCase(businessFlowData);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

	}

	private void executeTestCase(List<String> businessFlowData)
			throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {

		for (int currentKeywordNum = 0; currentKeywordNum < businessFlowData.size(); currentKeywordNum++) {
			String[] currentFlowData = businessFlowData.get(currentKeywordNum).split(",");
			String currentKeyword = currentFlowData[0];

			int nKeywordIterations;
			if (currentFlowData.length > 1) {
				nKeywordIterations = Integer.parseInt(currentFlowData[1]);
			} else {
				nKeywordIterations = 1;
			}

			Map<String, Integer> keywordDirectory = new HashMap<String, Integer>();
			for (int currentKeywordIteration = 0; currentKeywordIteration < nKeywordIterations; currentKeywordIteration++) {
				if (keywordDirectory.containsKey(currentKeyword)) {
					keywordDirectory.put(currentKeyword, keywordDirectory.get(currentKeyword) + 1);
				} else {
					keywordDirectory.put(currentKeyword, 1);
				}
				currentSubIteration = keywordDirectory.get(currentKeyword);

				dataTable.setCurrentRow(testParameters.getCurrentTestCase(), currentIteration, currentSubIteration);

				if (currentSubIteration > 1) {
					System.out.println(currentKeyword + " (Sub-Iteration: " + currentSubIteration + ")");
				} else {
					System.out.println(currentKeyword);
				}

				invokeBusinessComponent(currentKeyword);
			}
		}
	}

	private void invokeBusinessComponent(String currentKeyword)
			throws IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {

		Boolean isMethodFound = false;
		final String CLASS_FILE_EXTENSION = ".class";

		ArrayList<File> projects = getComponentDirectories();

		for (File packageDirectory : projects) {

			File[] packageFiles = packageDirectory.listFiles();
			String packageName = packageDirectory.getName();

			String currentDirectory = packageDirectory.toString();
			packageName = currentDirectory.replace(frameworkParameters.getRelativePath() + Util.getFileSeparator(), "");
			packageName = packageName.replace(Util.getFileSeparator(), ".");

			for (int i = 0; i < packageFiles.length; i++) {
				File packageFile = packageFiles[i];
				String fileName = packageFile.getName();

				// We only want the .class files
				if (fileName.endsWith(CLASS_FILE_EXTENSION)) {
					// Remove the .class extension to get the class name
					String className = fileName.substring(0, fileName.length() - CLASS_FILE_EXTENSION.length());

					Class<?> reusableComponents = Class.forName(packageName + "." + className);
					Method executeComponent;

					try {
						currentKeyword = currentKeyword.substring(0, 1).toLowerCase() + currentKeyword.substring(1);
						executeComponent = reusableComponents.getMethod(currentKeyword, (Class<?>[]) null);
					} catch (NoSuchMethodException ex) {
						continue;
					}

					isMethodFound = true;

					Constructor<?> ctor = null;
					for (Constructor<?> constObj : reusableComponents.getDeclaredConstructors()) {
						if (constObj.getParameterCount() == 1) {
							ctor = constObj;
							break;
						}
					}

					Object businessComponent = ctor.newInstance(scriptHelper);
					executeComponent.invoke(businessComponent, (Object[]) null);

					break;
				}
			}
		}

		if (!isMethodFound) {
			System.out.println("Keyword " + currentKeyword + " not found within any class "
					+ "inside the businesscomponents package");
		}
	}

	private ArrayList<File> getComponentDirectories() {
		ArrayList<File> projects = new ArrayList<File>();

		try {
			String strProjectList = properties.getProperty("ProjectName");
			if (!strProjectList.isEmpty()) {
				String[] tempProjectList = strProjectList.split(",");

				for (String project : tempProjectList) {
					String projectFolder = frameworkParameters.getRelativePath() + Util.getFileSeparator()
							+ Util.getFileSeparator() + project;
					File f1 = new File(projectFolder + Util.getFileSeparator() + "modules");
					File f2 = new File(projectFolder + Util.getFileSeparator() + "modulegroups");
					if (f1.exists() && f1.isDirectory()) {
						projects.add(f1);
					}
					if (f2.exists() && f2.isDirectory()) {
						projects.add(f2);
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getStackTrace());

		}
		return projects;
	}

	private void initializeTestScript() {
		scriptHelper = new ScriptHelper(dataTable, driver);
		initializeBusinessFlow();
	}

	private void initializeBusinessFlow() {
		ExcelDataAccess businessFlowAccess = new ExcelDataAccess(dataTable.getPath(),
				testParameters.getCurrentScenario());
		businessFlowAccess.setDatasheetName("Test_Steps");

		int rowNum = businessFlowAccess.getRowNum(testParameters.getCurrentTestCase(), 0);
		if (rowNum == -1) {
			System.out.println("The test case \"" + testParameters.getCurrentTestCase()
					+ "\" is not found in the Test Steps sheet!");
		}

		String dataValue;
		businessFlowData = new ArrayList<String>();

		int currentColumnNum = 1;
		while (true) {
			dataValue = businessFlowAccess.getValue(rowNum, currentColumnNum);
			if ("".equals(dataValue)) {
				break;
			}
			businessFlowData.add(dataValue);
			currentColumnNum++;
		}

		if (businessFlowData.isEmpty()) {
			System.out.println(
					"No test steps found against the test case \"" + testParameters.getCurrentTestCase() + "\"");
		}
	}


	private static void startUp() {
		properties = Settings.getInstance();
	}

	
	private synchronized static void initializeDatatable() {
		String runTimeDatatablePath;
		String dataTablePath = getDataTablepath();
		Boolean includeTestDataInReport = Boolean.parseBoolean(properties.getProperty("IncludeTestDataInReport"));

		if (includeTestDataInReport) {
			runTimeDatatablePath = reportPath + Util.getFileSeparator() + "Datatables";
			File runTimeDatatable = new File(runTimeDatatablePath + Util.getFileSeparator()
					+ testParameters.getCurrentScenario() + FrameworkParameters.excelExtension);

			if (!runTimeDatatable.exists()) {
				File datatable = new File(dataTablePath + Util.getFileSeparator() + testParameters.getCurrentScenario()
						+ FrameworkParameters.excelExtension);
				try {
					FileUtils.copyFile(datatable, runTimeDatatable);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Error in creating run-time datatable: Copying the datatable failed...");
				}
			}

			File runTimeCommonDatatable = new File(runTimeDatatablePath + Util.getFileSeparator() + "Common Testdata"
					+ FrameworkParameters.excelExtension);

			if (!runTimeCommonDatatable.exists()) {
				File commonDatatable = new File(dataTablePath + Util.getFileSeparator() + "Common Testdata"
						+ FrameworkParameters.excelExtension);
				try {
					FileUtils.copyFile(commonDatatable, runTimeCommonDatatable);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Error in creating run-time datatable: Copying the common datatable failed...");
				}
			}
		} else {
			runTimeDatatablePath = dataTablePath;
		}

		dataTable = new DataTable(runTimeDatatablePath, testParameters.getCurrentScenario());
		dataTable.setDataReferenceIdentifier("#");

	}

	private static String getDataTablepath() {
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator();
		@SuppressWarnings("unused")
		String dataTableFolder = "";

		try {
			String strProductList = properties.getProperty("ProjectName");

			if (!strProductList.isEmpty()) {
				String[] tempProductList = strProductList.split(",");

				for (String product : tempProductList) {
					File f1 = new File(datatablePath + product + Util.getFileSeparator() + "Datatables");

					if (f1.exists() && f1.isDirectory()) {
						File dataTableFile = new File(f1 + Util.getFileSeparator() + testParameters.getCurrentScenario()
								+ FrameworkParameters.excelExtension);

						if (dataTableFile.exists() && dataTableFile.isFile()) {
							dataTableFolder = product;
							datatablePath = datatablePath + Util.getFileSeparator() + product + Util.getFileSeparator()
									+ "Datatables";
							break;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error in creating run-time datatable: Copying the common datatable failed...");
		}
		return datatablePath;
	}


	private void quitWebDriver() {
		driver.quit();
	}

	private void wrapUp() {
		System.out.println("End of execution!!!");
	}

}