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
import uimap.insta_map;

public class instagram {
	
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
			driver.get(prop.getProperty("i_url"));
			
			driver.manage().window().maximize();
			Thread.sleep(1000);
	
			if(driver.getTitle().equals("Login • Instagram")){			
				driver.findElement(insta_map.username).sendKeys(prop.getProperty("i_username"));
				driver.findElement(insta_map.password).sendKeys(prop.getProperty("i_password"));
				driver.findElement(By.xpath(insta_map.loginButton)).click();

				if(driver.findElements(By.xpath(insta_map.notNowButton)).size()==1){
					driver.findElement(By.xpath(insta_map.notNowButton)).click();
					Thread.sleep(500);
				}
				
				if(driver.findElements(By.xpath(insta_map.notNowDialog)).size()==1){
					driver.findElement(By.xpath(insta_map.notNowDialog)).click();
					Thread.sleep(500);
				}
										
				if(waitForAnElementToBeDisplayed(By.xpath(insta_map.userAccount(prop.getProperty("i_username"))))){
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
