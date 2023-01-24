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
import uimap.meta_map;

public class facebook {
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
			driver.get(prop.getProperty("f_url"));
			driver.manage().window().maximize();
	
			if(driver.getTitle().equals("Facebook - log in or sign up")){			
				driver.findElement(By.xpath(meta_map.username)).sendKeys(prop.getProperty("f_username"));
				driver.findElement(By.xpath(meta_map.password)).sendKeys(prop.getProperty("f_password"));
				driver.findElement(By.xpath(meta_map.loginButton)).click();

				Thread.sleep(1000);
				
				if(waitForAnElementToBeDisplayed(By.xpath(meta_map.accountVerificationPopup))){
					driver.findElement(By.xpath(meta_map.verificationButton)).click();
					Thread.sleep(500);
				}
									
				if(waitForAnElementToBeDisplayed(By.xpath(meta_map.userAccount))){
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
	
	public static void navigateToMyAccount() {
		try {
			//your code here
			System.out.println("Navigate to My Account.");
			
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void checkMessages() {
		try {
			//your code here
			System.out.println("Messages checked.");
			
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
