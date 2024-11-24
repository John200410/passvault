package org.passvault.core.entry.item.items;

import org.passvault.core.entry.item.ItemType;
import org.passvault.core.entry.item.TextItemBase;

/**
 * An item that is used to store an account's password.
 *
 * @author john@chav.is 11/12/2024
 */
public class PasswordItem extends TextItemBase {
	
	public PasswordItem(String value) {
		super(ItemType.PASSWORD, "Password", value);
	}
	
	public PasswordItem(String name, String value) {
		super(ItemType.PASSWORD, name, value);
	}
	
}
