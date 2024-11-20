package org.passvault.client.vault.component.item;

import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.items.UsernameItem;

/**
 * @author john@chav.is 11/14/2024
 */
public class UsernameComponent extends EntryItemComponentBase<UsernameItem> {
	
	public UsernameComponent(EntryPanel parent, Entry entry, UsernameItem item) {
		super(parent, entry, item);
	}
	
	@Override
	protected void attemptApply() throws Exception {
		this.item.setName(this.itemNameTextArea.getText());
		this.item.setValue(this.valueTextComponent.getText());
	}
}
