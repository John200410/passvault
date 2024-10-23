package org.passvault.core.api.exception;

/**
 * Exception is thrown whenever the user tries accessing a locked vault
 *
 * @author john@chav.is 10/2/2024
 */
public class VaultLockedException extends VaultException {
	
	public VaultLockedException() {
		super("Vault is locked");
	}
	
}