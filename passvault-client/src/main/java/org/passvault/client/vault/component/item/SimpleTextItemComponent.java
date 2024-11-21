package org.passvault.client.vault.component.item;

import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.TextItemBase;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Simple item that can be used for usernames, emails, passwords, etc
 *
 * @author john@chav.is 11/14/2024
 */
public class SimpleTextItemComponent extends EntryItemComponentBase<TextItemBase> {
	
	/**
	 * Used for secret item types
	 */
	private final JButton hideShowButton;
	
	public SimpleTextItemComponent(EntryPanel parent, Entry entry, TextItemBase item) {
		super(parent, entry, item, true);
		
		this.hideShowButton = new JButton("Show");
		
		if(item.getType().isSecret()) {
			this.hideShowButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(SimpleTextItemComponent.this.hideShowButton.getText().equals("Show")) {
						((ValueSecretTextField) SimpleTextItemComponent.this.valueTextComponent).setEchoChar((char) 0);
						SimpleTextItemComponent.this.hideShowButton.setText("Hide");
					} else {
						((ValueSecretTextField) SimpleTextItemComponent.this.valueTextComponent).setEchoChar('•');
						SimpleTextItemComponent.this.hideShowButton.setText("Show");
					}
				}
			});
			this.valuePanel.add(this.hideShowButton, 1);
		}
	}
	
	@Override
	protected void attemptApply() throws Exception {
		this.item.setName(this.itemNameTextComponent.getText());
		this.item.setValue(this.valueTextComponent.getText());
	}
}
