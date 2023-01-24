package framework;

import org.openqa.selenium.WebDriver;


/**
 * Wrapper class for common framework objects, to be used across the entire test
 * case and dependent libraries
 * 
 * @author
 */
public class ScriptHelper {

	private final DataTable dataTable;
	private Driver hafDriver;
	// private WebDriverUtil driverUtil;

	/**
	 * Constructor to initialize all the objects wrapped by the {@link ScriptHelper}
	 * class
	 * 
	 * @param dataTable  The {@link DataTable} object
	 * @param driver     The {@link WebDriver} object
	 * @param driverUtil The {@link WebDriverUtil} object
	 */

	public ScriptHelper(DataTable dataTable, Driver hafDriver) {// , WebDriverUtil driverUtil) {
		this.dataTable = dataTable;
		this.hafDriver = hafDriver;
		// this.driverUtil = driverUtil;
	}

	public ScriptHelper(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	/**
	 * Function to get the {@link DataTable} object
	 * 
	 * @return The {@link DataTable} object
	 */
	public DataTable getDataTable() {
		return dataTable;
	}

	/**
	 * Function to get the {@link SeleniumReport} object
	 * 
	 * @return The {@link SeleniumReport} object
	 */
//	public SeleniumReport getReport() {
//		return report;
//	}

	/**
	 * Function to get the {@link DriverScript} object
	 * 
	 * @return The {@link DriverScript} object
	 */
	public Driver gethafDriver() {
		return hafDriver;
	}

	/**
	 * Function to get the {@link WebDriverUtil} object
	 * 
	 * @return The {@link WebDriverUtil} object
	 */
//	public WebDriverUtil getDriverUtil() {
//		return driverUtil;
//	}

}