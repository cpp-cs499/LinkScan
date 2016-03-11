package ocr;

import org.apache.commons.validator.EmailValidator;
import org.apache.commons.validator.UrlValidator;

public class URLChecker {
	
	public static boolean isValid(String input){
		
		return isValidUrl(input) || isEmail(input);
	}

	public static boolean isValidUrl(String input){
		//checks to see if the text is a valid url
		
		//using apache
		String[] acceptedSchemes = { "https", "http" };
    	UrlValidator validator = new UrlValidator(acceptedSchemes);
    	
    	boolean result = validator.isValid(input);
    	
    	if(result){
    		return true;
    	}
    	
    	result = isWww(input);
    	
    	return result;
	}
	
	public static boolean isWww(String input){
		String regex = "www.\\w+.\\w+";
		return input.matches(regex);
	}
	
	public static boolean isEmail(String input){
		//checks to see if input in an email
		return EmailValidator.getInstance().isValid(input);
	}
}
