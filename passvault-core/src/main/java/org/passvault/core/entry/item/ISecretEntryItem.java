package org.passvault.core.entry.item;

/**
 * An {@link IEntryItem} that contains a secret value. The value can be hidden so that it is not displayed.
 * 
 * @author john@chav.is 9/30/2024
 */
public interface ISecretEntryItem<T> extends IEntryItem<T> {
	
	/**
	 * @return true if the value should be hidden, false otherwise
	 */
	boolean isHidden();
	
}