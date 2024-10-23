package org.passvault.core.impl.vault;

import org.passvault.core.api.entry.IEntry;
import org.passvault.core.api.exception.VaultException;
import org.passvault.core.api.vault.IVault;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author john@chav.is 9/30/2024
 */
public class FileVault implements IVault {
	
	
	/**
	 * A list containing entries of this vault.
	 */
	private final ArrayList<IEntry> entries = new ArrayList<>();
	
	public FileVault(File file) {
	
	}
	
	@Override
	public boolean unlock(char[] password) throws VaultException {
		return false;
	}
	
	@Override
	public boolean storeEntry(IEntry entry) throws VaultException {
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
}
