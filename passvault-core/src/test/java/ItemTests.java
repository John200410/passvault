import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.passvault.core.entry.item.ItemException;
import org.passvault.core.entry.item.ItemType;
import org.passvault.core.entry.item.items.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

/**
 * @author john@chav.is 11/23/2024
 */
class ItemTests {
	
	@Test
	void testUsernameItem() throws ItemException {
		String randomUsername = "username" + new Random().nextInt(1000);
		
		final UsernameItem item = new UsernameItem("Username", randomUsername);
		
		Assertions.assertEquals(randomUsername, item.getValue());
		Assertions.assertEquals("Username", item.getName());
		Assertions.assertEquals(ItemType.USERNAME, item.getType());
		
		randomUsername = TestUtils.generateRandomString(new Random().nextInt(100));
		item.setValue(randomUsername);
		
		Assertions.assertEquals(randomUsername, item.getValue());
		
		try {
			item.setValue(null);
			Assertions.fail("Username value should not be nullable");
		} catch(ItemException e) {
			//passed, it should throw an exception
		}
	}
	
	@Test
	void testPasswordItem() throws ItemException {
		String randomPassword = TestUtils.generateRandomString(10);
		
		final PasswordItem item = new PasswordItem("Password", randomPassword);
		
		Assertions.assertEquals(randomPassword, item.getValue());
		Assertions.assertEquals("Password", item.getName());
		Assertions.assertEquals(ItemType.PASSWORD, item.getType());
		
		randomPassword = TestUtils.generateRandomString(new Random().nextInt(100));
		item.setValue(randomPassword);
		
		Assertions.assertEquals(randomPassword, item.getValue());
		
		try {
			item.setValue(null);
			Assertions.fail("Password value should not be nullable");
		} catch(ItemException e) {
			//passed, it should throw an exception
		}
	}
	
	@Test
	void testEmailItem() throws ItemException {
		String testEmail = "john@chav.is";
		
		final EmailItem item = new EmailItem("Email", testEmail);
		
		Assertions.assertEquals(testEmail, item.getValue());
		Assertions.assertEquals("Email", item.getName());
		Assertions.assertEquals(ItemType.EMAIL, item.getType());
		
		testEmail = TestUtils.generateRandomString(new Random().nextInt(100)) + "@gmail.com";
		item.setValue(testEmail);
		
		Assertions.assertEquals(testEmail, item.getValue());
		
		try {
			item.setValue(null);
			Assertions.fail("Email value should not be nullable");
		} catch(ItemException e) {
			//passed, it should throw an exception
		}
	}
	
	@Test
	void testUrlItem() throws ItemException {
		final String[] urls = {"http://example.com", "https://example.org"};
		final UrlItem item = new UrlItem("URLs", urls);
		
		Assertions.assertArrayEquals(urls, item.getValue());
		Assertions.assertEquals("URLs", item.getName());
		Assertions.assertEquals(ItemType.URL, item.getType());
		Assertions.assertEquals("http://example.com\nhttps://example.org", item.getDisplayValue());
		
		final List<String> randomURLs = new ArrayList<>();
		for(int i = 0; i < 100; i++) {
			randomURLs.add("https://" + TestUtils.generateRandomString(new Random().nextInt(10)) + ".com");
		}
		
		item.setValue(randomURLs.toArray(new String[0]));
		
		Assertions.assertArrayEquals(randomURLs.toArray(new String[0]), item.getValue());
		
		final StringJoiner sj = new StringJoiner("\n");
		for(String url : randomURLs) {
			sj.add(url);
		}
		
		Assertions.assertEquals(sj.toString(), item.getDisplayValue());
		
		try {
			item.setValue(null);
			Assertions.fail("URL value should not be nullable");
		} catch(ItemException e) {
			//passed, it should throw an exception
		}
	}
	
	@Test
	void testTotpItem() throws ItemException {
		
		final DefaultSecretGenerator secretGenerator = new DefaultSecretGenerator();
		final String secret = secretGenerator.generate();
		
		final TOTPItem item = new TOTPItem("TOTP", secret);
		
		Assertions.assertEquals(secret, item.getValue());
		Assertions.assertEquals("TOTP", item.getName());
		Assertions.assertEquals(ItemType.TOTP, item.getType());
		
		item.update();
		
		final DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();
		final DefaultCodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator, new SystemTimeProvider());
		
		Assertions.assertTrue(codeVerifier.isValidCode(secret, item.getDisplayValue()));
		
		try {
			item.setValue(null);
			Assertions.fail("TOTP secret should not be nullable");
		} catch(ItemException e) {
			//passed, it should throw an exception
		}
	}
}
