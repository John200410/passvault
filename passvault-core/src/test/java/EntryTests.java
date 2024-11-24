import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.EntryMetadata;
import org.passvault.core.entry.item.items.PasswordItem;
import org.passvault.core.entry.item.items.UrlItem;
import org.passvault.core.entry.item.items.UsernameItem;

import java.io.ByteArrayOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author john@chav.is 11/23/2024
 */
public class EntryTests {
	
	@Test
	void entryCreationAndSave() throws Exception {
		
		final EntryMetadata entryMetadata = new EntryMetadata();
		entryMetadata.name = "instagram.com";
		entryMetadata.timeCreated = System.currentTimeMillis();
		entryMetadata.timeModified = System.currentTimeMillis();
		entryMetadata.favorite = false;
		
		
		final UsernameItem usernameItem = new UsernameItem("john");
		final PasswordItem passwordItem = new PasswordItem("password");
		final UrlItem urlItem = new UrlItem(new String[]{"https://www.instagram.com"});
		
		
		//TODO: test with icon
		final Entry entry = new Entry(entryMetadata, null, usernameItem, passwordItem, urlItem);
		
		try(final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try(ZipOutputStream zipOutputStream = new ZipOutputStream(baos)) {
				entry.saveTo(zipOutputStream);
			}
		}
	}
	
	@Test
	void compareEntries() {
		final EntryMetadata entryMetadata = new EntryMetadata();
		entryMetadata.name = "instagram.com";
		entryMetadata.timeCreated = System.currentTimeMillis();
		entryMetadata.timeModified = System.currentTimeMillis();
		entryMetadata.favorite = false;
		
		final Entry instagramEntry = new Entry(entryMetadata, null);
		
		Assertions.assertEquals(0, instagramEntry.compareTo(instagramEntry));
		
		final EntryMetadata entryMetadata2 = new EntryMetadata();
		entryMetadata2.name = "facebook.com";
		entryMetadata2.timeCreated = System.currentTimeMillis();
		entryMetadata2.timeModified = System.currentTimeMillis();
		entryMetadata2.favorite = false;
		
		final Entry facebookEntry = new Entry(entryMetadata2, null);
		
		Assertions.assertTrue(instagramEntry.compareTo(facebookEntry) > 0);
		
		instagramEntry.getMetadata().favorite = true;
		
		Assertions.assertTrue(instagramEntry.compareTo(facebookEntry) < 0);
	}
	
}
