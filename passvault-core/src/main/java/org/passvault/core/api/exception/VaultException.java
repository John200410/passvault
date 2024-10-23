package org.passvault.core.api.exception;

/**
 * @author john@chav.is
 */
public class VaultException extends Exception {
	
	public VaultException(String message) {
		super(message);
	}
	
	public VaultException(String message, Throwable cause) {
		super(message, cause);
	}
	
}