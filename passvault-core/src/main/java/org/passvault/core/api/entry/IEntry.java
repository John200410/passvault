package org.passvault.core.api.entry;

import org.passvault.core.api.entry.item.IEntryItem;
import org.passvault.core.api.vault.IVault;

/**
 * An entry is a collection of items that are stored together.
 * <p>
 * Entries can be stored in a {@link IVault}
 *
 * @author john@chav.is 9/18/2024
 */
public interface IEntry {
	
	/**
	 * @return the name of this entry
	 */
	String getName();
	
	/**
	 * @return an array of items in this entry
	 */
	IEntryItem<?>[] getItems();
	
}
