package org.passvault.core.entry.item.items;

import org.passvault.core.entry.item.ItemType;
import org.passvault.core.entry.item.TextItemBase;

/**
 * An item that is used to store an account's email address.
 *
 * @author john@chav.is 11/14/2024
 */
public class EmailItem extends TextItemBase {
	
	public EmailItem(String name, String value) {
		super(ItemType.EMAIL, name, value);
	}
}
