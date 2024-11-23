package org.passvault.client.vault.component.item;

import org.passvault.client.PassVaultClient;
import org.passvault.client.generator.PasswordGeneratorForm;
import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.items.PasswordItem;

import javax.swing.*;

/**
 * @author john@chav.is 11/23/2024
 */
public class PasswordComponent extends SimpleTextItemComponent {
	
	private final JButton generateButton;
	
	public PasswordComponent(EntryPanel parent, Entry entry, PasswordItem item) {
		super(parent, entry, item, true);
		
		this.generateButton = new JButton("Generate");
		this.generateButton.addActionListener(e -> {
			PasswordGeneratorForm.open(parent.getContainer().getVaultForm(), PassVaultClient.SETTINGS.preferredGeneratorParams, this);
		});
		
		this.valuePanel.add(this.generateButton);
		
		this.generateButton.setVisible(false);
	}
	
	@Override
	public void enableEditMode() {
		super.enableEditMode();
		
		this.generateButton.setVisible(true);
	}
	
	@Override
	public void disableEditMode(boolean apply) throws Exception {
		super.disableEditMode(apply);
		
		this.generateButton.setVisible(false);
	}
	
	public ValueTextComponent getPasswordField() {
		return this.valueTextComponent;
	}
}
