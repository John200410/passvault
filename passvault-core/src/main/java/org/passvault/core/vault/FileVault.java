package org.passvault.core.vault;

import org.passvault.core.Globals;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.EntryMetadata;
import org.passvault.core.entry.item.IEntryItem;
import org.passvault.core.exception.VaultException;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * The file vault implementation.
 * <p>
 * The file at it's core will be a ZIP archive, AES encrypted with the master password being the secret key.
 * <p>
 * The ZIP archive will contain a directory for each entry. Each entry directory will contain metadata.json, items.json, and possibly other resources
 *
 * @author john@chav.is 9/30/2024
 */
public class FileVault implements IVault {
	
	/**
	 * The file location of the vault.
	 */
	private final File file;
	
	/**
	 * A collection of entries of this vault.
	 */
	private HashMap<String, Entry> entries = null;
	
	public FileVault(File file) {
		this.file = file;
	}
	
	@Override
	public void unlock(char[] password) throws VaultException {
		
		if(!this.isLocked()) {
			//TODO: throw new VaultException("Vault is already unlocked");
		}
		
		try(FileInputStream fis = new FileInputStream(this.file)) {
			
			//TODO: implement encryption
			//final Cipher cipher = createCipher(Cipher.DECRYPT_MODE, password);
			//target = new CipherInputStream(target, createCipher(Cipher.DECRYPT_MODE, password));
			
			final HashMap<String, Entry> entries = new HashMap<>();
			
			
			final ZipInputStream zis = new ZipInputStream(fis);
			
			ZipEntry zipEntry;
			while((zipEntry = zis.getNextEntry()) != null) {
				Globals.LOGGER.info("Found entry: " + zipEntry.getName() + " " + zipEntry.isDirectory());
				
				final String[] split = zipEntry.getName().split("/");
				final String entryName = split[0];
				final String file = split[1];
				
				entries.putIfAbsent(entryName, new Entry());
				
				final Entry entry = entries.get(entryName);
				
				if(file.equals("metadata.json")) {
					final EntryMetadata metadata = Globals.GSON.fromJson(new InputStreamReader(zis), EntryMetadata.class);
					entry.setMetadata(metadata);
				} else if(file.equals("items.json")) {
					final IEntryItem[] entryItems = Globals.GSON.fromJson(new InputStreamReader(zis), IEntryItem[].class);
					for(IEntryItem entryItem : entryItems) {
						entry.getItems().add(entryItem);
					}
				}
				
				zis.closeEntry();
			}
			
			zis.close();
			
			this.entries = entries;
		} catch(FileNotFoundException e) {
			throw new VaultException("Vault file not found", e);
		} catch(IOException e) {
			throw new VaultException("Error reading vault file", e);
		}
	}
	
	@Override
	public void commitEntry(Entry entry) throws VaultException {
		
		if(this.isLocked()) {
			//TODO: throw exception
		}
		
		this.entries.put(entry.getMetadata().name, entry);
		this.save();
	}
	
	@Override
	public void deleteEntry(Entry entry) throws VaultException {
		
		if(this.isLocked()) {
			//TODO: throw exception
		}
		
		this.entries.remove(entry.getMetadata().name);
		this.save();
	}
	
	@Override
	public Collection<Entry> getEntries() throws VaultException {
		return this.entries.values();
	}
	
	@Override
	public boolean isLocked() {
		return this.entries == null;
	}
	
	private boolean save() throws VaultException {
		try {
			
			//TODO: save to temp file instead of overwriting, just incase an error occurs
			
			try(FileOutputStream fos = new FileOutputStream(this.file)) {
				
				//TODO: cipher output stream
				
				final ZipOutputStream zos = new ZipOutputStream(fos);
				for(Entry entry : this.entries.values()) {
					entry.saveTo(zos);
				}
				zos.close();
				
				return true;
			}
		} catch(Throwable e) {
			throw new VaultException("Error saving vault", e);
		}
	}
	
}
