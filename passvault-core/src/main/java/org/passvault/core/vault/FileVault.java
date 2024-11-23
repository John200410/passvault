package org.passvault.core.vault;

import org.passvault.core.Globals;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.EntryMetadata;
import org.passvault.core.entry.item.IEntryItem;
import org.passvault.core.exception.VaultException;
import org.passvault.core.exception.VaultLockedException;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * The file vault implementation.
 * <p>
 * The file at it's core will be a ZIP archive, AES-256 encrypted with the master password being the secret key.
 * <p>
 * The ZIP archive will contain a directory for each entry. Each entry directory will contain metadata.json, items.json, and possibly other resources
 *
 * @author john@chav.is 9/30/2024
 */
public class FileVault implements IVault {
	
	/**
	 * Constants
	 */
	public static final SecureRandom SECURE_RANDOM = new SecureRandom();
	public static final SecretKeyFactory SECRET_KEY_FACTORY;
	private static final int PBKDF2_ITERATION_COUNT = 300_000; //the number of times that the password is hashed during the derivation of the symmetric key
	private static final int PBKDF2_SALT_LENGTH = 16;
	private static final int AES_KEY_LENGTH_BITS = 256;
	private static final int GCM_NONCE_LENGTH = 12;
	private static final int GCM_TAG_LENGTH_BITS = 128; //authentication tag size in bits
	
	static {
		SecretKeyFactory factory = null;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		} catch(Throwable e) {
			Globals.LOGGER.severe("Error creating key generator");
			e.printStackTrace();
		}
		SECRET_KEY_FACTORY = factory;
	}
	
	/**
	 * The file location of the vault.
	 */
	private final File file;
	
	/**
	 * The master password
	 */
	private char[] password = null;
	
	/**
	 * Flag variable for whether the vault has been modified since the last save
	 */
	private boolean dirty = false;
	
	/**
	 * Random salt and nonce generated for encrypting this vault
	 */
	private final byte[] salt = new byte[PBKDF2_SALT_LENGTH];
	private final byte[] nonce = new byte[GCM_NONCE_LENGTH];
	
	/**
	 * A collection of entries of this vault.
	 */
	private HashSet<Entry> entries = null;
	
	public FileVault(File file) {
		this.file = file;
	}
	
	@Override
	public void unlock(char[] password) throws VaultException {
		
		if(!this.isLocked()) {
			throw new VaultException("Vault is already unlocked");
		}
		
		if(SECRET_KEY_FACTORY == null) {
			throw new VaultException("Error initializing encryption");
		}
		
		try(FileInputStream fis = new FileInputStream(this.file)) {
			
			//extract salt and nonce
			if(fis.read(this.salt) == -1) {
				throw new VaultException("Error reading vault file.");
			}
			if(fis.read(this.nonce) == -1) {
				throw new VaultException("Error reading vault file.");
			}
			
			final byte[] decryptionSecret = SECRET_KEY_FACTORY.generateSecret(
					new PBEKeySpec(password, this.salt, PBKDF2_ITERATION_COUNT, AES_KEY_LENGTH_BITS)
			).getEncoded();
			final SecretKey decryptionKey = new SecretKeySpec(decryptionSecret, "AES");
			
			final Cipher decryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
			decryptCipher.init(Cipher.DECRYPT_MODE, decryptionKey, new GCMParameterSpec(GCM_TAG_LENGTH_BITS, this.nonce));
			
			try(final CipherInputStream cis = new CipherInputStream(fis, decryptCipher)) {
				
				//read entries
				final HashMap<String, Entry> entries = new HashMap<>();
				
				try(final ZipInputStream zis = new ZipInputStream(cis)) {
					
					ZipEntry zipEntry;
					while((zipEntry = zis.getNextEntry()) != null) {
						final String[] split = zipEntry.getName().split("/");
						final String entryName = split[0];
						final String file = split[1];
						
						entries.putIfAbsent(entryName, new Entry());
						
						final Entry entry = entries.get(entryName);
						
						if(file.equals("metadata.json")) {
							final EntryMetadata metadata = Globals.GSON.fromJson(new InputStreamReader(zis), EntryMetadata.class);
							entry.setMetadata(metadata);
						} else if(file.equals("items.json")) {
							final IEntryItem<?>[] entryItems = Globals.GSON.fromJson(new InputStreamReader(zis), IEntryItem[].class);
							for(IEntryItem<?> entryItem : entryItems) {
								entry.getItems().add(entryItem);
							}
						}
						
						//TODO: read other files as resources
						
						zis.closeEntry();
					}
				}
				
				this.entries = new HashSet<>(entries.values());
				
				//store password, so that we can use it for encrypting the vault later
				this.password = password;
			}
		} catch(FileNotFoundException e) {
			throw new VaultException("Vault file not found", e);
		} catch(IOException e) {
			throw new VaultException("Error reading vault file", e);
		} catch(InvalidKeySpecException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException e) {
			throw new VaultException("Error initializing encryption", e);
		}
	}
	
	@Override
	public void commitEntry(Entry entry) throws VaultException {
		
		if(this.isLocked()) {
			throw new VaultLockedException();
		}
		
		synchronized(this.file) {
			this.entries.add(entry);
			this.dirty = true;
		}
	}
	
	@Override
	public void deleteEntry(Entry entry) throws VaultException {
		
		if(this.isLocked()) {
			throw new VaultLockedException();
		}
		
		synchronized(this.file) {
			this.entries.remove(entry);
			this.dirty = true;
		}
	}
	
	@Override
	public HashSet<Entry> getEntries() throws VaultException {
		return this.entries;
	}
	
	@Override
	public boolean isLocked() {
		return this.password == null;
	}
	
	@Override
	public void save() throws VaultException {
		
		if(this.isLocked()) {
			throw new VaultLockedException();
		}
		
		//setup encryption
		Cipher encryptionCipher = null;
		try {
			//generate salt and nonce for encryption
			SECURE_RANDOM.nextBytes(this.salt);
			SECURE_RANDOM.nextBytes(this.nonce);
			
			//generate secret key for encryption
			final byte[] encryptionSecret = SECRET_KEY_FACTORY.generateSecret(
					new PBEKeySpec(this.password, this.salt, PBKDF2_ITERATION_COUNT, AES_KEY_LENGTH_BITS)
			).getEncoded();
			final SecretKey encryptionKey = new SecretKeySpec(encryptionSecret, "AES");
			
			//setup cipher
			encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
			encryptionCipher.init(Cipher.ENCRYPT_MODE, encryptionKey, new GCMParameterSpec(GCM_TAG_LENGTH_BITS, this.nonce));
		} catch(Throwable e) {
			throw new VaultException("Error initializing encryption", e);
		}
		
		try {
			
			//TODO: save to temp file instead of overwriting, just incase an error occurs
			
			synchronized(this.file) {
				try(final FileOutputStream fos = new FileOutputStream(this.file)) {
					
					//write salt and nonce so the vault can be decrypted
					fos.write(this.salt);
					fos.write(this.nonce);
					
					
					//encrypt output
					try(final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
						try(final ZipOutputStream zos = new ZipOutputStream(baos)) {
							
							//write entries
							for(Entry entry : this.entries) {
								entry.saveTo(zos);
							}
							
						}
						
						final byte[] encrypted = encryptionCipher.doFinal(baos.toByteArray());
						fos.write(encrypted);
					}
				}
			}
		} catch(Throwable e) {
			throw new VaultException("Error saving vault", e);
		}
	}
	
	public File getFile() {
		return this.file;
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	public boolean isDirty() {
		return this.dirty;
	}
	
	public static FileVault createNewVault(File file, char[] password) throws Exception {
		
		final byte[] salt = new byte[PBKDF2_SALT_LENGTH];
		final byte[] nonce = new byte[GCM_NONCE_LENGTH];
		
		//generate salt and nonce for encryption
		SECURE_RANDOM.nextBytes(salt);
		SECURE_RANDOM.nextBytes(nonce);
		
		//generate secret key for encryption
		final byte[] encryptionSecret = SECRET_KEY_FACTORY.generateSecret(
				new PBEKeySpec(password, salt, PBKDF2_ITERATION_COUNT, AES_KEY_LENGTH_BITS)
		).getEncoded();
		final SecretKey encryptionKey = new SecretKeySpec(encryptionSecret, "AES");
		
		//setup cipher
		final Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
		encryptionCipher.init(Cipher.ENCRYPT_MODE, encryptionKey, new GCMParameterSpec(GCM_TAG_LENGTH_BITS, nonce));
		
		try(final FileOutputStream fos = new FileOutputStream(file)) {
			
			//write salt and nonce so the vault can be decrypted
			fos.write(salt);
			fos.write(nonce);
			
			//encrypt output
			try(final CipherOutputStream cos = new CipherOutputStream(fos, encryptionCipher)) {
				try(ZipOutputStream zos = new ZipOutputStream(cos)) {
				}
			}
		}
		
		final FileVault vault = new FileVault(file);
		vault.unlock(password);
		return vault;
	}
	
}
