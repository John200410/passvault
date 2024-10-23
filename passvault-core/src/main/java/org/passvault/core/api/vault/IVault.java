package org.passvault.core.api.vault;

import org.passvault.core.api.entry.IEntry;
import org.passvault.core.api.exception.VaultException;

import java.util.Collection;

/**
 * A vault will securely store PassVault entries. It can be a file, a database, or any other storage mechanism.
 *
 * @author john@chav.is 9/18/2024
 */
public interface IVault {
	
	/**
	 * Unlocks the vault with the given password.
	 *
	 * @param password
	 * @return
	 * @throws VaultException
	 */
	boolean unlock(char[] password) throws VaultException;
	
	/**
	 * Stores an entry into the vault.
	 *
	 * @param entry the entry to store
	 * @return true if the entry was stored successfully, false otherwise
	 */
	boolean storeEntry(IEntry entry) throws VaultException;
	
	/**
	 * Retrieves all entries from the vault.
	 *
	 * @return a collection of entries
	 * @throws Throwable
	 */
	Collection<IEntry> getEntries() throws VaultException;
	
	/**
	 * @return true if the vault is locked, false otherwise
	 */
	boolean isLocked();
	
}
