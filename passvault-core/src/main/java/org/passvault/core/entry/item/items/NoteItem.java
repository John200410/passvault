package org.passvault.core.entry.item.items;

import org.passvault.core.entry.item.ItemType;
import org.passvault.core.entry.item.TextItemBase;

/**
 * @author john@chav.is 12/1/2024
 */
public class NoteItem extends TextItemBase {
	
	public NoteItem(String value) {
		this("Notes", value);
	}
	
	public NoteItem(String name, String value) {
		super(ItemType.NOTE, name, value);
	}
	
	
	
}
