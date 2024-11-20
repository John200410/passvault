package org.passvault.client.vault.component.item;

import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.items.PasswordItem;

/**
 * @author john@chav.is 11/20/2024
 */
public class PasswordComponent extends EntryItemComponentBase<PasswordItem> {

	public PasswordComponent(EntryPanel parent, Entry entry, PasswordItem item) {
		super(parent, entry, item);
	}

	@Override
	protected void attemptApply() throws Exception {
		this.item.setName(this.itemNameTextComponent.getText());
		this.item.setValue(this.valueTextComponent.getText());
	}
	
}
