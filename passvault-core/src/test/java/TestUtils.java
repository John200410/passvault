import java.util.Random;

/**
 * @author john@chav.is 11/23/2024
 */
public class TestUtils {
	
	public static String generateRandomString(int length) {
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		final StringBuilder sb = new StringBuilder();
		final Random random = new Random();
		
		for(int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		
		return sb.toString();
	}
	
}
