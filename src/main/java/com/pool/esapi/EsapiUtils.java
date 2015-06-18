package com.pool.esapi;

import java.util.Arrays;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.EncoderConstants;
import org.owasp.esapi.Randomizer;
import org.owasp.esapi.StringUtilities;
import org.owasp.esapi.errors.AuthenticationCredentialsException;
import org.owasp.esapi.errors.AuthenticationException;

public class EsapiUtils {

	/**
	 * Generate a strong password that is not similar to the specified old
	 * password.
	 *
	 * @param oldPassword
	 *            the password to be compared to the new password for similarity
	 * @return a new strong password that is dissimilar to the specified old
	 *         password
	 */
	public static String generateStrongPassword(String oldPassword) {
		Randomizer r = ESAPI.randomizer();
		int letters = r.getRandomInteger(4, 6); // inclusive, exclusive
		int digits = 7 - letters;
		String passLetters = r.getRandomString(letters,
				EncoderConstants.CHAR_PASSWORD_LETTERS);
		String passDigits = r.getRandomString(digits,
				EncoderConstants.CHAR_PASSWORD_DIGITS);
		String passSpecial = r.getRandomString(1,
				EncoderConstants.CHAR_PASSWORD_SPECIALS);
		String newPassword = passLetters + passSpecial + passDigits;
		if (StringUtilities.getLevenshteinDistance(oldPassword, newPassword) > 5) {
			return newPassword;
		}
		return generateStrongPassword(oldPassword);
	}

	/**
	 * 
	 * <p/>
	 * This implementation checks: - for any 3 character substrings of the old
	 * password - for use of a length * character sets > 16 (where character
	 * sets are upper, lower, digit, and special jtm - 11/16/2010 - added check
	 * to verify pw != username (fix for
	 * http://code.google.com/p/owasp-esapi-java/issues/detail?id=108)
	 */
	public static void verifyPasswordStrength(String oldPassword, String newPassword,
			String accountName) throws AuthenticationException {
		if (newPassword == null) {
			throw new AuthenticationCredentialsException("Invalid password",
					"New password cannot be null");
		}

		// can't change to a password that contains any 3 character substring of
		// old password
		if (oldPassword != null) {
			int length = oldPassword.length();
			for (int i = 0; i < length - 2; i++) {
				String sub = oldPassword.substring(i, i + 3);
				if (newPassword.indexOf(sub) > -1) {
					throw new AuthenticationCredentialsException(
							"Invalid password",
							"New password cannot contain pieces of old password");
				}
			}
		}

		// new password must have enough character sets and length
		int charsets = 0;
		for (int i = 0; i < newPassword.length(); i++) {
			if (Arrays.binarySearch(EncoderConstants.CHAR_LOWERS,
					newPassword.charAt(i)) >= 0) {
				charsets++;
				break;
			}
		}
		for (int i = 0; i < newPassword.length(); i++) {
			if (Arrays.binarySearch(EncoderConstants.CHAR_UPPERS,
					newPassword.charAt(i)) >= 0) {
				charsets++;
				break;
			}
		}
		for (int i = 0; i < newPassword.length(); i++) {
			if (Arrays.binarySearch(EncoderConstants.CHAR_DIGITS,
					newPassword.charAt(i)) >= 0) {
				charsets++;
				break;
			}
		}
		for (int i = 0; i < newPassword.length(); i++) {
			if (Arrays.binarySearch(EncoderConstants.CHAR_SPECIALS,
					newPassword.charAt(i)) >= 0) {
				charsets++;
				break;
			}
		}

		// calculate and verify password strength
		int strength = newPassword.length() * charsets;
		if (strength < 16) {
			throw new AuthenticationCredentialsException("Invalid password",
					"New password is not long and complex enough");
		}
		if (accountName.equalsIgnoreCase(newPassword)) {
			// password can't be account name
			throw new AuthenticationCredentialsException("Invalid password",
					"Password matches account name, irrespective of case");
		}
	}

	public static void verifyPasswordStrength(String newPassword, String accountName)
			throws AuthenticationException {
		if (newPassword == null) {
			throw new AuthenticationCredentialsException("Invalid password",
					"New password cannot be null");
		}

		// new password must have enough character sets and length
		int charsets = 0;
		for (int i = 0; i < newPassword.length(); i++) {
			if (Arrays.binarySearch(EncoderConstants.CHAR_LOWERS,
					newPassword.charAt(i)) >= 0) {
				charsets++;
				break;
			}
		}
		for (int i = 0; i < newPassword.length(); i++) {
			if (Arrays.binarySearch(EncoderConstants.CHAR_UPPERS,
					newPassword.charAt(i)) >= 0) {
				charsets++;
				break;
			}
		}
		for (int i = 0; i < newPassword.length(); i++) {
			if (Arrays.binarySearch(EncoderConstants.CHAR_DIGITS,
					newPassword.charAt(i)) >= 0) {
				charsets++;
				break;
			}
		}
		for (int i = 0; i < newPassword.length(); i++) {
			if (Arrays.binarySearch(EncoderConstants.CHAR_SPECIALS,
					newPassword.charAt(i)) >= 0) {
				charsets++;
				break;
			}
		}

		// calculate and verify password strength
		int strength = newPassword.length() * charsets;
		if (strength < 16) {
			throw new AuthenticationCredentialsException("Invalid password",
					"New password is not long and complex enough");
		}
		if (accountName != null && accountName.equalsIgnoreCase(newPassword)) {
			// password can't be account name
			throw new AuthenticationCredentialsException("Invalid password",
					"Password matches account name, irrespective of case");
		}
	}

}
