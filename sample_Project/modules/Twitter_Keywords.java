package sample_Project.modules;

import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import framework.ReusableLibrary;
import framework.ScriptHelper;
import uimap.twit_map;

public class Twitter_Keywords extends ReusableLibrary{

	private String TestDataSheetName = "Test_Data";
	String testName = dataTable.getData(TestDataSheetName, "TestCaseName");
	
	public Twitter_Keywords(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	public void logInToTwitter() {
		try {
			driver.get(properties.getProperty("t_url"));
			driver.manage().window().maximize();

			Thread.sleep(1000);

			if (driver.getTitle().equals("Explore / Twitter")) {
				driver.findElement(By.xpath(twit_map.loginLink)).click();
				driver.findElement(By.xpath(twit_map.username)).sendKeys(properties.getProperty("t_username"));
				driver.findElement(By.xpath(twit_map.nextButton)).click();
				driver.findElement(By.xpath(twit_map.password)).sendKeys(properties.getProperty("t_password"));
				driver.findElement(By.xpath(twit_map.loginButton)).click();

				if (waitForAnElementToBeDisplayed(By.xpath(twit_map.userAccount))) {
					System.out.println("Logged in successfully.");
				} else {
					System.out.println("Was not able to login.");
					driver.close();
				}
			} else {
				System.out.println("Logo not found");
				driver.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Will wait for an element to be loaded (up to 15 seconds)
	public boolean waitForAnElementToBeDisplayed(By el) throws InterruptedException, IOException {
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 15);
		if (wait.until(ExpectedConditions.visibilityOfElementLocated(el)) != null) {
			System.out.println("Element is now visible");
			return true;
		}
		return false;
	}

	// ******************************* Keep adding your methods below this line.  *******************************
	public void checkYourTwits() {
		System.out.println("Test Case: "+testName+"  checkYourTwits - Executed");
	}

	public void likeTwits() {
		System.out.println("Test Case: "+testName+"  likeTwits - Executed");
	}

}
