package uimap;

import org.openqa.selenium.By;

public class insta_map {

	public final static By username = By.name("username");
	public final static By password = By.name("password");
	public final static String loginButton = "//div[contains(text(), 'Log ')]";
	public final static String notNowButton = "//div//button[contains(text(),'Not Now')]";
	public final static String notNowDialog = "//*[@role='dialog']//button[contains(text(),'Not Now')]";
	
	public final static String userAccount (String user) {
		return "//div/div/a[contains(text(), "+user+")]";
	}
	
	
	
	
	
	
	
	
	
	
	
}
