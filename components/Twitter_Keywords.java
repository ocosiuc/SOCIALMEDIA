package components;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uimap.twit_map;

public class twitter {

	
	public static WebDriver driver;
	public static ChromiumDriver cdriver;
	static Properties prop = new Properties();
	static String fileName = "config.properties";

	public static void logIn() {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			prop.load(fis);
			
			System.setProperty("webdriver.chrome.driver", prop.getProperty("chrome_driver_path"));
			driver = new ChromeDriver();
			driver.get(prop.getProperty("t_url"));
			driver.manage().window().maximize();
			
			Thread.sleep(1000);
	
			if(driver.getTitle().equals("Explore / Twitter")){	
				driver.findElement(By.xpath(twit_map.loginLink)).click();
				driver.findElement(By.xpath(twit_map.username)).sendKeys(prop.getProperty("t_username"));
				driver.findElement(By.xpath(twit_map.nextButton)).click();
				driver.findElement(By.xpath(twit_map.password)).sendKeys(prop.getProperty("t_password"));
				driver.findElement(By.xpath(twit_map.loginButton)).click();
				
				if(waitForAnElementToBeDisplayed(By.xpath(twit_map.userAccount))){
					System.out.println("Logged in successfully.");
				}else {
					System.out.println("Was not able to login.");
					driver.close();
				}				
			}else{
				System.out.println("Logo not found");
				driver.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Will wait for an element to be loaded (up to 15 seconds) 
	public static boolean waitForAnElementToBeDisplayed (By el) throws InterruptedException, IOException {   
		 WebDriverWait wait = new WebDriverWait(driver, 15);
		 if(wait.until(ExpectedConditions.visibilityOfElementLocated(el)) != null) {
			 System.out.println("Element is now visible");
			 return true;
		 }
		 return false;
	 }
	
	
	
	//*******************************    Keep adding your methods below this line.    *******************************
}
