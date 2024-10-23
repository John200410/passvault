package org.passvault.client.generator;

import java.security.SecureRandom;

/**
 * Utility class for generating passwords
 *
 * @author john@chav.is 10/4/2024
 */
public class PasswordGenerator {
	
	/**
	 * SecureRandom for random password generator
	 */
	private static final SecureRandom RANDOM = new SecureRandom();
	
	/**
	 * Generates a random password based on the given parameters.
	 *
	 * @param params
	 * @return the generated password
	 */
	public static String generatePassword(GeneratorParameters params) throws GeneratorException {
		
		//check lengths and stuff
		if(params.passwordLength() < 1) {
			throw new GeneratorException("Password length must be at least 1");
		}
		if(params.characters().length < 1) {
			throw new GeneratorException("Must have at least one character to generate a password");
		}
		if(params.specialCharCount() > 0 && params.specialChars().length < 1) {
			throw new GeneratorException("Must have at least one special character to generate a password");
		}
		if(params.specialCharCount() > params.passwordLength()) {
			throw new GeneratorException("Cannot generate a password with so many special characters");
		}
		
		final char[] password = new char[params.passwordLength()];
		
		//first, populate password array with random alphanumeric characters
		for(int i = 0; i < password.length; i++) {
			password[i] = params.characters()[RANDOM.nextInt(params.characters().length)];
		}
		
		//mask array for special characters
		final char[] passwordMask = new char[password.length];
		
		//populate mask with special characters
		for(int i = 0; i < params.specialCharCount(); i++) {
			int index = RANDOM.nextInt(password.length);
			while(passwordMask[index] != 0) {
				index = RANDOM.nextInt(password.length);
			}
			passwordMask[index] = params.specialChars()[RANDOM.nextInt(params.specialChars().length)];
		}
		
		//apply mask to password
		for(int i = 0; i < password.length; i++) {
			if(passwordMask[i] != 0) {
				password[i] = passwordMask[i];
			}
		}
		
		//create string from password char array
		return new String(password);
	}
	
	
	
}
