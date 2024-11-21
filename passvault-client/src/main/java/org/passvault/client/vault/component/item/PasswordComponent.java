package org.passvault.client.vault.component.item;

import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.items.PasswordItem;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author john@chav.is 11/20/2024
 */
public class PasswordComponent extends EntryItemComponentBase<PasswordItem> {
	
	private final JButton hideShowButton;
	
	public PasswordComponent(EntryPanel parent, Entry entry, PasswordItem item) {
		super(parent, entry, item, true);
		
		this.hideShowButton = new JButton("Show");
		
		this.hideShowButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(PasswordComponent.this.hideShowButton.getText().equals("Show")) {
					((ValueSecretTextField)PasswordComponent.this.valueTextComponent).setEchoChar((char) 0);
					PasswordComponent.this.hideShowButton.setText("Hide");
				} else {
					((ValueSecretTextField)PasswordComponent.this.valueTextComponent).setEchoChar('•');
					PasswordComponent.this.hideShowButton.setText("Show");
				}
			}
		});
		
		this.valuePanel.add(this.hideShowButton, 1);
	}

	@Override
	protected void attemptApply() throws Exception {
		this.item.setName(this.itemNameTextComponent.getText());
		this.item.setValue(this.valueTextComponent.getText());
	}
	
}
