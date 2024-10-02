package org.passvault.core.api.entry.item;

import org.passvault.core.api.entry.IEntry;

/**
 * An item that is to be contained within an {@link IEntry}.
 * <p>
 * This can be a text item, hidden text item, TOTP item, etc.
 *
 * @author john@chav.is 9/18/2024
 */
public interface IEntryItem<T> {
	
	/**
	 * @return the name of this item
	 */
	String getName();
	
	/**
	 * @return the value of this item
	 */
	T getValue();
	
	/**
	 * @return a String representation of the value
	 */
	String getDisplayValue();
	
}