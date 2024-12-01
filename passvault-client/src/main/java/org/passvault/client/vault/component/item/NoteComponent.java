package org.passvault.client.vault.component.item;

import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.TextItemBase;

/**
 * @author john@chav.is 12/1/2024
 */
public class NoteComponent extends SimpleTextItemComponent {
	
	public NoteComponent(EntryPanel parent, Entry entry, TextItemBase item) {
		super(parent, entry, item, false);
	}
	
	@Override
	protected ValueTextComponent createValueTextComponent() {
		return new ValueTextArea();
	}
}
