package run;

import java.io.FileInputStream;
import java.util.Properties;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import components.facebook;
import components.instagram;
import components.twitter;

public class runmanager {
	
	static Properties prop = new Properties();
	static String fileName = "config.properties";

	public static void main(String[] args) {
		try {
			String[] keywords = null;
			
			Class<? extends Object> classObj = null;
			Object obj = null;
			
			FileInputStream fis = new FileInputStream(fileName);
			prop.load(fis);
			
			if(prop.getProperty("instagram").equalsIgnoreCase("yes")) {				
				obj = new instagram();
		        classObj = obj.getClass();
		        keywords = "logIn;navigateToMyInstaAccount;checkLikes".split(";");
		        
			}else if(prop.getProperty("facebook").equalsIgnoreCase("yes")) {
				obj = new facebook();
		        classObj = obj.getClass();
		        keywords = "logIn;navigateToMyAccount;checkMessages".split(";");
		        
			}else if(prop.getProperty("twitter").equalsIgnoreCase("yes")) {
				obj = new twitter();
		        classObj = obj.getClass();
		        keywords = "logIn;navigateToMyFeed;checkTwits".split(";");
			}
			
			
			if(classObj != null) {
				 // get all methods in the class
		        Method[] allMethods = classObj.getDeclaredMethods();
		  
		        // loop through the methods to find the method addMe
		        
		        for(int i = 0; i < keywords.length; i ++) {
		        	String keyword = keywords[i];
		        	
			        for (Method m : allMethods) {	            
			            String methodName = m.getName();
			            if (methodName.equals(keyword)) {
			                try {                   
			                    // invoke the method directly with its parameters
			                    m.invoke(obj);
			                    break;
			                }catch (InvocationTargetException e) {
			                	e.printStackTrace();
			                }
			            }  
			        }
		        }
			}else {
				System.out.println("Check your config file to enable/disable specific social media app.");
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
