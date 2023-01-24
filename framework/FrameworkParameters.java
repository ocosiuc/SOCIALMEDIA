package framework;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FrameworkParameters {
	private String relativePath;
	private String runConfiguration;
	public static final String excelExtension = ".xlsx";
	private boolean stopExecution = false;

	private static FrameworkParameters Framework_Parameters = new FrameworkParameters();

	public FrameworkParameters() {
		// TODO Auto-generated constructor stub
	}

	public static FrameworkParameters getInstance() {
		return Framework_Parameters;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public void setRelativePath() {
		String relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		setRelativePath(relativePath);
	}

	public String getRunConfiguration() {
		return runConfiguration;
	}

	public void setRunConfiguration(String runConfiguration) {
		this.runConfiguration = runConfiguration;
	}

	public static String getTestManagerPath() {
		String projectName = System.getProperty("ProjectName");
		String dir = (new File(System.getProperty("user.dir"))).getAbsolutePath() + System.getProperty("file.separator")
				+ projectName;
		return dir;
	}

	public static List<String> getKeywordPackages(String apps) {
		String[] appArray = apps.split(",");
		List<String> appPKG = new ArrayList<String>();
		for (String app : appArray) {
			appPKG.add(app + ".components." + app + "_Keywords");
		}
		return appPKG;
	}

	public boolean getStopExecution() {
		return stopExecution;
	}

	public void setStopExecution(boolean stopExecution) {
		this.stopExecution = stopExecution;
	}

	public static void clear() {
		Framework_Parameters = new FrameworkParameters();
	}
}
