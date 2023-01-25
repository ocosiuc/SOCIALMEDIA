package sample_Project.modules;

import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import framework.ReusableLibrary;
import framework.ScriptHelper;
import sample_Project.uimap.insta_map;

public class Instagram_Keywords extends ReusableLibrary{

	private String TestDataSheetName = "Test_Data";
	String testName = dataTable.getData(TestDataSheetName, "TestCaseName");
	
	public Instagram_Keywords(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	public void logInToInstagram() {
		try {
			driver.get(properties.getProperty("i_url"));
			driver.manage().window().maximize();
			Thread.sleep(1000);

			if (driver.getTitle().equals("Login � Instagram")) {
				driver.findElement(By.xpath(insta_map.username)).sendKeys(properties.getProperty("i_username"));
				driver.findElement(By.xpath(insta_map.password)).sendKeys(properties.getProperty("i_password"));
				driver.findElement(By.xpath(insta_map.loginButton)).click();

				if (driver.findElements(By.xpath(insta_map.notNowButton)).size() == 1) {
					driver.findElement(By.xpath(insta_map.notNowButton)).click();
					Thread.sleep(500);
				}

				if (driver.findElements(By.xpath(insta_map.notNowDialog)).size() == 1) {
					driver.findElement(By.xpath(insta_map.notNowDialog)).click();
					Thread.sleep(500);
				}

				if (waitForAnElementToBeDisplayed(By.xpath(insta_map.userAccount(properties.getProperty("i_username"))))) {
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

	// ******************************* Keep adding your methods below this line.
	// *******************************

	public void searchHashTag() {
		System.out.println("Test Case: "+testName+" searchHashTag - Executed");
	}

	public void selectHashTag() {
		System.out.println("Test Case: "+testName+"  selectHashTag - Executed");
	}


}
