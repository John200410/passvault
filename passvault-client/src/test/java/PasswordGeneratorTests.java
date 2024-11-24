import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import org.junit.jupiter.api.Assertions;
import org.passvault.client.generator.GeneratorParameters;
import org.passvault.client.generator.PasswordGenerator;

/**
 * @author john@chav.is 11/23/2024
 */
class PasswordGeneratorTests {
	
	@FuzzTest
	void generatePasswordFuzz(FuzzedDataProvider provider) throws Exception {
		
		final char[] specialChars = new char[provider.consumeInt(1, 100)];
		for(int i = 0; i < specialChars.length; i++) {
			specialChars[i] = provider.consumeChar();
		}
		
		final boolean lowercase = provider.consumeBoolean();
		final boolean uppercase = provider.consumeBoolean();
		final boolean numbers = provider.consumeBoolean();
		
		final boolean noneSelected = !lowercase && !uppercase && !numbers;
		
		final int passwordLength = provider.consumeInt(1, 10000);
		final int specialCharCount = provider.consumeInt(0, passwordLength);
		
		final GeneratorParameters.Builder builder = new GeneratorParameters.Builder()
				.passwordLength(passwordLength)
				.lowercaseChars(noneSelected || lowercase)
				.uppercaseChars(uppercase)
				.numbers(numbers)
				.specialChars(specialChars)
				.specialCharCount(specialCharCount)
				.avoidAmbiguousChars(provider.consumeBoolean());
		
		final GeneratorParameters params = builder.build();
		final String s = PasswordGenerator.generatePassword(params);
		
		Assertions.assertNotNull(s);
		Assertions.assertEquals(passwordLength, s.length());
		
		int specialCount = 0;
		for(int i = 0; i < s.length(); i++) {
			for(char c : specialChars) {
				if(s.charAt(i) == c) {
					specialCount++;
					break;
				}
			}
		}
		
		Assertions.assertEquals(specialCharCount, specialCount);
	}
	
}
