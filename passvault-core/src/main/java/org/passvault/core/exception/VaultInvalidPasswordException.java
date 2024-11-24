package org.passvault.core.exception;

public class VaultInvalidPasswordException extends VaultException {
	public VaultInvalidPasswordException() {
		super("Invalid Password");
	}
}
