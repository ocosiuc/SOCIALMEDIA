package sample_Project.uimap;

public class insta_map {

	public final static String loginLink = "//a[contains(@href, 'login')]";
	public final static String username = "//input[contains(@autocomplete, 'username')]";
	public final static String nextButton = "//span[contains(text(), 'Next')]";
	public final static String password = "//input[contains(@autocomplete, 'password')]";
	public final static String loginButton = "//div[@aria-labelledby]//span[contains(text(), 'Log in')]";
	
	public final static String userAccount(String name) {
		return name;
	}
	
	public final static String notNowButton = "";
	public final static String notNowDialog = "";
	

}
