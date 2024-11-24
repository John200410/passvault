import org.junit.jupiter.api.*;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.EntryMetadata;
import org.passvault.core.entry.item.items.PasswordItem;
import org.passvault.core.entry.item.items.UrlItem;
import org.passvault.core.entry.item.items.UsernameItem;
import org.passvault.core.exception.VaultInvalidPasswordException;
import org.passvault.core.vault.FileVault;

import java.io.File;

/**
 * @author john@chav.is 11/23/2024
 */
class VaultTests {
	
	private static File vaultFile;
	private static FileVault vault;
	private static String vaultPassword;
	
	private static Entry testEntry;
	
	@BeforeAll
	static void createTestEntry() {
		final EntryMetadata entryMetadata = new EntryMetadata();
		entryMetadata.name = "instagram.com";
		entryMetadata.timeCreated = System.currentTimeMillis();
		entryMetadata.timeModified = System.currentTimeMillis();
		entryMetadata.favorite = false;
		
		final UsernameItem usernameItem = new UsernameItem("john");
		final PasswordItem passwordItem = new PasswordItem("password");
		final UrlItem urlItem = new UrlItem(new String[]{"https://www.instagram.com"});
		
		testEntry = new Entry(entryMetadata, null, usernameItem, passwordItem, urlItem);
	}
	
	@AfterAll
	static void cleanup() {
		vault.getFile().delete();
	}
	
	@AfterEach
	void saveVault() throws Exception {
		vault.save();
	}
	
	@BeforeAll
	static void createFileVault() throws Exception {
		
		vaultFile = new File(TestUtils.generateRandomString(10) + ".pv");
		if(vaultFile.exists()) {
			vaultFile.delete();
		}
		
		vaultPassword = TestUtils.generateRandomString(32);
		vault = FileVault.createNewVault(vaultFile, vaultPassword.toCharArray());
	}
	
	@Test
	void unlockVault() throws Exception {
		final FileVault vault = new FileVault(vaultFile);
		
		//test with wrong password, it should throw an exception
		try {
			vault.unlock("wrong password".toCharArray());
			Assertions.fail("Unlocking vault with wrong password should throw an exception");
		} catch(VaultInvalidPasswordException e) {
			//passed
		}
		
		//unlock with correct password
		vault.unlock(vaultPassword.toCharArray());
	}
	
	@Test
	void entryAddition() throws Exception {
		vault.getEntries().clear();
		
		vault.save();
		
		vault.commitEntry(testEntry);
		
		Assertions.assertTrue(vault.getEntries().contains(testEntry));
		
		vault.save();
	}
	
	@Test
	void mutateEntry() throws Exception {
		vault.getEntries().clear();
		
		vault.save();
		
		//add the entry
		vault.commitEntry(testEntry);
		
		Assertions.assertTrue(vault.getEntries().contains(testEntry));
		Assertions.assertEquals(1, vault.getEntries().size());
		
		vault.save();
		
		//update the entry
		testEntry.getMetadata().favorite = true;
		vault.commitEntry(testEntry);
		
		//check if the entry was updated
		Assertions.assertTrue(vault.getEntries().contains(testEntry));
		Assertions.assertEquals(1, vault.getEntries().size());
		
		vault.save();
	}
	
	@Test
	void entryDeletion() throws Exception {
		vault.getEntries().clear();
		
		vault.save();
		
		//add the entry
		vault.commitEntry(testEntry);
		
		Assertions.assertTrue(vault.getEntries().contains(testEntry));
		Assertions.assertEquals(1, vault.getEntries().size());
		
		vault.save();
		
		//delete the entry
		vault.deleteEntry(testEntry);
		
		//check if the entry was deleted
		Assertions.assertFalse(vault.getEntries().contains(testEntry));
		Assertions.assertEquals(0, vault.getEntries().size());
	}
	
}
