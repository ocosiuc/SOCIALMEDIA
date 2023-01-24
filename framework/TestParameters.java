package framework;

//import framework.Browser;

public class TestParameters {
	private final String currentScenario;
	private final String currentTestCase;
	public static String currentTC;
	private Browser browser;

	/**
	 * {@link TestParameters object}
	 */
	public TestParameters(String currentScenario, String currentTestCase) {
		this.currentScenario = currentScenario;
		this.currentTestCase = currentTestCase;
		currentTC = currentTestCase;
	}

	public String getCurrentScenario() {
		return currentScenario;
	}

	public String getCurrentTestCase() {
		return currentTestCase;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public Browser getBrowser() {
		return browser;
	}

}
