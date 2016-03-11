import static org.junit.Assert.*;

import org.junit.Test;

import ocr.URLChecker;

public class ValidURLTester{

	final static String[] VALID_URLS = {"http://google.com",
						  "http://www.google.com",
						  "www.google.com",
						  "https://google.com",
						  "https://www.google.com"};
	final static String[] INVALID_URLS = {"http//google.com",
							"google.com",
							"htp://google.com",
							"htps://google.com",
							"ww.google.com",
							"www.google.com.",
							"www .google.com"};
	
	final static String[] VALID_EMAILS = {"user@gmail.com",
							"user.name@gmail.com"};
	final static String[] INVALID_EMAILS = {"user @gmail.com",
							  "user@gmail@.com",
							  "@user@gmail.com",
							  "user@gmail..com"};
	
	@Test
	public void testUrls(){
		
		for(String url : VALID_URLS){
			assertTrue(URLChecker.isValidUrl(url));
		}
		
		for(String url : INVALID_URLS){
			assertFalse(URLChecker.isValidUrl(url));
		}
	}
	
	@Test
	public void testEmails(){

		for(String email : VALID_EMAILS){
			assertTrue(URLChecker.isEmail(email));
		}
		
		for(String email : INVALID_EMAILS){
			assertFalse(URLChecker.isEmail(email));
		}
	}
}
