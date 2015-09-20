package com.pool.esapi;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;

public class Validator {

	public static String validate(String fieldName, String RegExpName, String s) {

		String str = null;
		try {
			str = ESAPI.validator().getValidInput(fieldName, s, RegExpName,
					200, false);
		} catch (ValidationException e) {
			e.printStackTrace();
			throw new FieldValidationException(fieldName, e);
		} catch (IntrusionException e) {
			e.printStackTrace();
			throw new IntrusionDetectedException(fieldName, e);
		}

		return str;
	}

	public static String validateString(String fieldName, String s) {

		String str = null;
		try {
			str = ESAPI.validator().getValidInput(fieldName, s, "SafeString",
					200, false);
		} catch (ValidationException e) {
			e.printStackTrace();
			throw new FieldValidationException(fieldName, e);
		} catch (IntrusionException e) {
			e.printStackTrace();
			throw new IntrusionDetectedException(fieldName, e);
		}

		return str;
	}

	public static String validateEmail(String fieldName, String s) {

		String str = null;
		try {
			str = ESAPI.validator().getValidInput(fieldName, s, "Email", 200,
					false);
		} catch (ValidationException e) {
			e.printStackTrace();
			throw new FieldValidationException(fieldName, e);
		} catch (IntrusionException e) {
			e.printStackTrace();
			throw new IntrusionDetectedException(fieldName, e);
		}

		return str;
	}

	public static String validatePassword(String fieldName, String s) {

		String str = null;
		try {
			str = ESAPI.validator().getValidInput(fieldName, s, "Password",
					200, false);
		} catch (ValidationException e) {
			e.printStackTrace();
			throw new FieldValidationException(fieldName, e);
		} catch (IntrusionException e) {
			e.printStackTrace();
			throw new IntrusionDetectedException(fieldName, e);
		}

		return str;
	}

	public static String validateName(String fieldName, String s) {

		String str = null;
		try {
			str = ESAPI.validator().getValidInput(fieldName, s, "Name", 200,
					false);
		} catch (ValidationException e) {
			e.printStackTrace();
			throw new FieldValidationException(fieldName, e);
		} catch (IntrusionException e) {
			e.printStackTrace();
			throw new IntrusionDetectedException(fieldName, e);
		}

		return str;
	}

	public static String validateUserName(String fieldName, String s) {

		String str = null;
		try {
			str = ESAPI.validator().getValidInput(fieldName, s, "UserName",
					200, false);
		} catch (ValidationException e) {
			e.printStackTrace();
			throw new FieldValidationException(fieldName, e);
		} catch (IntrusionException e) {
			e.printStackTrace();
			throw new IntrusionDetectedException(fieldName, e);
		}

		return str;
	}

	public static String validateGender(String fieldName, String s) {

		String str = null;
		try {
			str = ESAPI.validator().getValidInput(fieldName, s, "Gender", 200,
					false);
		} catch (ValidationException e) {
			e.printStackTrace();
			throw new FieldValidationException(fieldName, e);
		} catch (IntrusionException e) {
			e.printStackTrace();
			throw new IntrusionDetectedException(fieldName, e);
		}

		return str;
	}

	public static String validatePin(String fieldName, String s) {
		try {
			if (s == null || s.length() < 6) {
				throw new FieldValidationException(fieldName);
			}
			Integer.parseInt(s);

		} catch (Exception e) {
			e.printStackTrace();
			throw new FieldValidationException(fieldName, e);
		}
		return s;
	}

}
