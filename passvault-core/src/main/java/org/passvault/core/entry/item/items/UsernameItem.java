package org.passvault.core.entry.item.items;

import org.passvault.core.entry.item.ItemType;
import org.passvault.core.entry.item.TextItemBase;

/**
 * An item that is used to store an account's username.
 *
 * @author john@chav.is 11/11/2024
 */
public class UsernameItem extends TextItemBase {
	
	public UsernameItem(String name, String value) {
		super(ItemType.USERNAME, name, value);
	}
	
}
