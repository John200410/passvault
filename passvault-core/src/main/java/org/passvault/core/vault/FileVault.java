package org.passvault.core.vault;

import org.passvault.core.entry.IEntry;
import org.passvault.core.exception.VaultException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The file vault implementation.
 * <p>
 * The file at it's core will be a ZIP archive, AES encrypted with the master password being the secret key.
 * <p>
 * The ZIP archive will contain a folder for each entry.
 * Each entry will be a folder that contains a JSON file with the entry metadata. The folder will also contain any resources associated with the entry.
 *
 * @author john@chav.is 9/30/2024
 */
public class FileVault implements IVault {
	
	/**
	 * The file location of the vault.
	 */
	private final File file;
	
	/**
	 * A list containing entries of this vault.
	 */
	private final ArrayList<IEntry> entries = new ArrayList<>();

	
	public FileVault(File file) {
		this.file = file;
	}
	
	@Override
	public boolean unlock(char[] password) throws VaultException {
		
		if(!this.isLocked()) {
		
		}
		
		return false;
	}
	
	@Override
	public boolean commitEntry(IEntry entry) throws VaultException {
		return false;
	}
	
	@Override
	public boolean deleteEntry(IEntry entry) throws VaultException {
		return false;
	}
	
	@Override
	public Collection<IEntry> getEntries() throws VaultException {
		return this.entries;
	}
	
	@Override
	public boolean isLocked() {
		return false;
	}
	
	private boolean save() {
		return true;
	}
	
}
