package com.run;

import org.owasp.esapi.Authenticator;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;

public class EsapiTest {

	public static void main(String[] args) {
		
		String username = "unmeshv02";
		
		try {
			String vUsername = ESAPI.validator().getValidInput("username",
					username, "UserName", 25, false);
					
			Authenticator auth = ESAPI.authenticator();
			
			System.out.println(auth);
			System.out.println(vUsername);
			
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (IntrusionException e) {
			e.printStackTrace();
		}
	}
}
