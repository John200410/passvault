package org.passvault.core.vault;

import org.passvault.core.entry.Entry;
import org.passvault.core.exception.VaultException;

import java.util.HashSet;

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
	 * @throws VaultException
	 */
	void unlock(char[] password) throws VaultException;
	
	/**
	 * Stores an entry into the vault.
	 *
	 * @param entry the entry to store
	 */
	void commitEntry(Entry entry) throws VaultException;
	
	/**
	 * Deletes an entry from the vault.
	 *
	 * @param entry the entry to delete
	 */
	void deleteEntry(Entry entry) throws VaultException;
	
	/**
	 * Saves the vault.
	 *
	 * @throws VaultException
	 */
	void save() throws VaultException;
	
	/**
	 * Retrieves a map containing all the entries in the vault.
	 *
	 * @return map of all entries
	 * @throws Throwable
	 */
	HashSet<Entry> getEntries() throws VaultException;
	
	/**
	 * @return true if the vault is locked, false otherwise
	 */
	boolean isLocked();
	
}
