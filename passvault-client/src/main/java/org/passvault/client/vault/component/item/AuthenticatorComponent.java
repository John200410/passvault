package org.passvault.client.vault.component.item;

import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.items.AuthenticatorItem;

import javax.swing.*;

/**
 * @author john@chav.is 11/22/2024
 */
public class AuthenticatorComponent extends SimpleTextItemComponent {
	
	private final JLabel timeLabel;
	
	public AuthenticatorComponent(EntryPanel parent, Entry entry, AuthenticatorItem item) {
		super(parent, entry, item, true);
		
		this.timeLabel = new JLabel();
		this.add(this.timeLabel, this.constraints);
	}
	
	@Override
	protected void attemptApply() throws Exception {
		this.item.setName(this.itemNameTextComponent.getText());
		this.item.setValue(this.valueTextComponent.getText());
	}
	
	@Override
	public void updateComponents() {
		super.updateComponents();
		
		//update time until next code
		final long epochSeconds = System.currentTimeMillis() / 1000;
		final long timeUntilNextCode = 30 - (epochSeconds % 30);
		
		this.timeLabel.setText("Next code in: " + timeUntilNextCode + " seconds");
	}
	
	@Override
	public void enableEditMode() {
		super.enableEditMode();
		
		this.timeLabel.setVisible(false);
		this.valueTextComponent.setText(this.item.getValue());
	}
	
	@Override
	public void disableEditMode(boolean apply) throws Exception {
		super.disableEditMode(apply);
		
		this.timeLabel.setVisible(true);
		this.item.update();
		this.valueTextComponent.setText(this.item.getDisplayValue());
	}
}
