package framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Class that encapsulates the user settings specified in the properties file of
 * the framework
 * 
 * @author
 */
public class Settings {
	private static Properties cmdLineProps = new Properties();
	/**
	 * Function to return the singleton instance of the {@link Properties} object
	 * 
	 * @return Instance of the {@link Properties} object
	 * 
	 */
	public static String projectName;

	public static Properties getInstance() {
		return loadFromPropertiesFile();
	}

	public static Properties getMobilePropertiesInstance() {
		return loadFromPropertiesFileForMobile();
	}

	public static void setCommandLineProperties(String[] props) {
		cmdLineProps.setProperty("Current Date", (new Date()).toString());
		for (String prop : props) {
			String[] strProperty = prop.split("=");
			System.out.println(strProperty[0] + " = " + strProperty[1]);
			cmdLineProps.setProperty(strProperty[0], strProperty[1]);
		}
	}

	public static Properties loadFromPropertiesFile() {
		Properties properties = new Properties();
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

		String globalSettings = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "config.properties";

		if (frameworkParameters.getRelativePath() == null) {
			System.out.println("FrameworkParameters.relativePath is not set!");
			// throw new Exception("FrameworkParameters.relativePath is not set!");
		}

		Properties tempProperties = new Properties();

		try {

			tempProperties.load(new FileInputStream(globalSettings));

			String prj1 = tempProperties.getProperty("ProjectName");
			String strSettingsFile = frameworkParameters.getRelativePath() + Util.getFileSeparator() + prj1
					+ Util.getFileSeparator() + "project.properties";
			File settingsFile = new File(strSettingsFile);
			if (settingsFile.exists()) {

				properties.load(new FileInputStream(strSettingsFile));

				String prj2 = properties.getProperty("ProjectName");

				if (!prj1.equalsIgnoreCase(prj2)) {
					System.out.println(
							"Project name in " + globalSettings + " and " + strSettingsFile + " are different");
					// throw new Exception("Project name in " + globalSettings + " and " +
					// strSettingsFile + " are different");
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("FileNotFoundException while loading the Global Settings file");
			// throw new Exception("FileNotFoundException while loading the Global Settings
			// file");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException while loading the Global Settings file");
			// throw new FrameworkException("IOException while loading the Global Settings
			// file");
		}

		return properties;
	}

	public static Properties loadFromPropertiesFileForMobile() {
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

		if (frameworkParameters.getRelativePath() == null) {
			System.out.println("FrameworkParameters.relativePath is not set!");
			// throw new FrameworkException("FrameworkParameters.relativePath is not set!");
		}

		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream(frameworkParameters.getRelativePath() + Util.getFileSeparator()
					+ "Mobile Automation Settings.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("FileNotFoundException while loading the Mobile Automation Settings file");
			// throw new FrameworkException("FileNotFoundException while loading the Mobile
			// Automation Settings file");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException while loading the Mobile Automation Settings file");
			// throw new FrameworkException("IOException while loading the Mobile Automation
			// Settings file");
		}

		return properties;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}