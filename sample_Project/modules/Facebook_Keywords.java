package sample_Project.modules;

import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import framework.ReusableLibrary;
import framework.ScriptHelper;
import uimap.meta_map;

public class Facebook_Keywords extends ReusableLibrary {

	private String TestDataSheetName = "Test_Data";
	String testName = dataTable.getData(TestDataSheetName, "TestCaseName");
	
	public Facebook_Keywords(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	public void logInToFacebook() {
		try {
			driver.get(properties.getProperty("f_url"));
			driver.manage().window().maximize();

			if (driver.getTitle().equals("Facebook - log in or sign up")) {
				driver.findElement(By.xpath(meta_map.username)).sendKeys(properties.getProperty("f_username"));
				driver.findElement(By.xpath(meta_map.password)).sendKeys(properties.getProperty("f_password"));
				driver.findElement(By.xpath(meta_map.loginButton)).click();

				Thread.sleep(6000);

				if (waitForAnElementToBeDisplayed(By.xpath(meta_map.accountVerificationPopup))) {
					driver.findElement(By.xpath(meta_map.verificationButton)).click();
					Thread.sleep(500);
				}

				if (waitForAnElementToBeDisplayed(By.xpath(meta_map.userAccount))) {
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

	public void searchFriends() {
		System.out.println("Test Case: "+testName+"  searchFriends - Executed");
	}

	public void sendFriendRequest() {
		System.out.println("Test Case: "+testName+"  sendFriendRequest - Executed");
	}

}
