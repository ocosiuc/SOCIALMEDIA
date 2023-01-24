package framework;

import java.util.Properties;

/**
 * Abstract base class for reusable libraries created by the user
 * @author 
 */
public abstract class ReusableLibrary {
	/**
	 * The {@link DataTable} object (passed from the test script)
	 */
	protected DataTable dataTable;
	/**
	 * The {@link SeleniumReport} object (passed from the test script)
	 */
	//protected SeleniumReport report;
	/**
	 * The {@link Driver} object
	 */
	protected Driver driver;

	//protected WebDriverUtil driverUtil;
	
	/**
	 * The {@link ScriptHelper} object (required for calling one reusable library from another)
	 */
	protected ScriptHelper scriptHelper;
	
	/**
	 * The {@link Properties} object with settings loaded from the framework properties file
	 */
	protected Properties properties;
	/**
	 * The {@link FrameworkParameters} object
	 */
	protected FrameworkParameters frameworkParameters;
	 
	
	/**
	 * Constructor to initialize the {@link ScriptHelper} object and in turn the objects wrapped by it
	 * @param scriptHelper The {@link ScriptHelper} object
	 */
	public ReusableLibrary(ScriptHelper scriptHelper) {
		this.scriptHelper = scriptHelper;
		this.dataTable = scriptHelper.getDataTable();
		//this.report = scriptHelper.getReport();
		driver = scriptHelper.gethafDriver();
		//this.driverUtil = scriptHelper.getDriverUtil();
		
		properties = Settings.getInstance();
		frameworkParameters = FrameworkParameters.getInstance();
	}
}