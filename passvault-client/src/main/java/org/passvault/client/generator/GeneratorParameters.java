package org.passvault.client.generator;

import org.passvault.core.utils.MathUtils;

/**
 * Parameters used for password generation algorithm
 *
 * @author john@chav.is 10/2/2024
 */
public record GeneratorParameters(char[] characters, char[] specialChars, int passwordLength, int specialCharCount, boolean avoidAmbiguousChars) {
	
	/**
	 * Constants
	 */
	public static final int MAX_PASSWORD_LENGTH = 128;
	
	public static final char[] LOWERCASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	public static final char[] UPPERCASE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	public static final char[] NUMBERS = "0123456789".toCharArray();
	public static final char[] DEFAULT_SPECIAL_CHARS = "!@#$%^&*?".toCharArray();
	
	/**
	 * Mask for ambiguous characters in ASCII
	 */
	private static final boolean[] AMBIGUOUS_CHARACTER_MASK = new boolean[128];
	
	/**
	 * Array of ambiguous characters
	 */
	private static final char[] AMBIGUOUS_CHARACTERS = new char[]{
			'0', 'O', 'o',
			'I', 'l', '1', '|',
			'(', ')', '[', ']', '{', '}', '<', '>',
			'+', '-', '_', '=',
			';', ':', ',', '.'
	};
	
	static {
		for(char c : AMBIGUOUS_CHARACTERS) {
			AMBIGUOUS_CHARACTER_MASK[c] = true;
		}
	}
	
	/**
	 * Builder for GenerateParameters
	 */
	public static class Builder {
		
		boolean lowercaseAlphabet = true;
		boolean uppercaseAlphabet = true;
		boolean numbers = true;
		char[] specialChars = DEFAULT_SPECIAL_CHARS.clone();
		int passwordLength = 16;
		int specialCharCount = 1;
		boolean avoidAmbiguousChars = true;
		
		public GeneratorParameters build() {
			//clamp values
			this.passwordLength = MathUtils.clamp(passwordLength, 1, MAX_PASSWORD_LENGTH);
			this.specialCharCount = MathUtils.clamp(specialCharCount, 0, this.getMaxSpecialChars());
			
			//add alphanumberic characters to one char array
			final char[] alphaNumericChars = new char[
					(lowercaseAlphabet ? LOWERCASE_ALPHABET.length : 0) +
					(uppercaseAlphabet ? UPPERCASE_ALPHABET.length : 0) +
					(numbers ? NUMBERS.length : 0)
			];
			
			int index = 0;
			if(lowercaseAlphabet) {
				System.arraycopy(LOWERCASE_ALPHABET, 0, alphaNumericChars, index, LOWERCASE_ALPHABET.length);
				index += LOWERCASE_ALPHABET.length;
			}
			
			if(uppercaseAlphabet) {
				System.arraycopy(UPPERCASE_ALPHABET, 0, alphaNumericChars, index, UPPERCASE_ALPHABET.length);
				index += UPPERCASE_ALPHABET.length;
			}
			
			if(numbers) {
				System.arraycopy(NUMBERS, 0, alphaNumericChars, index, NUMBERS.length);
			}
			
			//clean char arrays
			final boolean stripAmbiguous = this.avoidAmbiguousChars;
			final char[] chars = cleanChars(alphaNumericChars, stripAmbiguous);
			final char[] specialChars = cleanChars(this.specialChars, stripAmbiguous);
			
			return new GeneratorParameters(chars, specialChars, passwordLength, specialCharCount, avoidAmbiguousChars);
		}
		
		public int getMaxSpecialChars() {
			return this.passwordLength / 3;
		}
		
		public Builder lowercaseChars(boolean lowercase) {
			this.lowercaseAlphabet = lowercase;
			return this;
		}
		
		public Builder uppercaseChars(boolean uppercase) {
			this.uppercaseAlphabet = uppercase;
			return this;
		}
		
		public Builder numbers(boolean numbers) {
			this.numbers = numbers;
			return this;
		}
		
		public Builder specialChars(char... specialChars) {
			this.specialChars = specialChars;
			return this;
		}
		
		public Builder passwordLength(int passwordLength) {
			this.passwordLength = passwordLength;
			return this;
		}
		
		public Builder specialCharCount(int minSpecialChars) {
			this.specialCharCount = minSpecialChars;
			return this;
		}
		
		public Builder avoidAmbiguousChars(boolean avoidAmbiguousChars) {
			this.avoidAmbiguousChars = avoidAmbiguousChars;
			return this;
		}
		
		public int getPasswordLength() {
			return this.passwordLength = MathUtils.clamp(this.passwordLength, 1, MAX_PASSWORD_LENGTH);
		}
		
		/**
		 * Strips whitespace and non-ascii characters from the possible characters.
		 * <p>
		 * Also strips ambiguous characters (0, O, I, l, 1, etc) if avoidAmbiguousChars is set to true.
		 */
		private static char[] cleanChars(char[] chars, boolean stripAmbiguous) {
			chars = chars.clone();
			
			int stripCount = 0;
			
			for(int i = 0; i < chars.length; i++) {
				final char c = chars[i];
				
				//strip whitespace and non-ascii
				if(c < 33 || c > 126) {
					//set to null character (will be ignored by password generator)
					chars[i] = 0;
					stripCount++;
				}
				//strip ambiguous characters
				else if(stripAmbiguous && AMBIGUOUS_CHARACTER_MASK[c]) {
					//set to null character (will be ignored by password generator)
					chars[i] = 0;
					stripCount++;
				}
			}
			
			//if we stripped characters, create a new array and copy over the non-null characters
			if(stripCount > 0) {
				final char[] newArray = new char[chars.length - stripCount];
				
				int i = 0;
				for(char c : chars) {
					if(c != 0) {
						newArray[i++] = c;
					}
				}
				
				chars = newArray;
			}
			return chars;
		}
	}
	
}
