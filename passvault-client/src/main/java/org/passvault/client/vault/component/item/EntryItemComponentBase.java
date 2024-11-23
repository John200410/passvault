package org.passvault.client.vault.component.item;

import org.passvault.client.vault.component.EntryComponent;
import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.IEntryItem;

import javax.swing.*;

/**
 * @author john@chav.is 11/14/2024
 */
public abstract class EntryItemComponentBase<T extends IEntryItem<?>> extends EntryComponent {

	protected final T item;
	protected JLabel itemNameLabel;
	protected SimpleTextItemComponent.ValueTextField itemNameTextComponent;
	
	protected JPanel valuePanel;

	public EntryItemComponentBase(EntryPanel parent, Entry entry, T item) {
		super(parent, entry);
		this.item = item;
		
		this.itemNameLabel = new JLabel();
		
		this.add(this.itemNameLabel, this.constraints);
		
		//TODO character limit
		this.itemNameTextComponent = new SimpleTextItemComponent.ValueTextField();
		this.itemNameTextComponent.setVisible(false);
		this.itemNameTextComponent.setEditable(false);
		this.itemNameTextComponent.setBorder(BorderFactory.createEmptyBorder());
		this.itemNameTextComponent.setBackground(this.getBackground());
		
		
		this.add(this.itemNameTextComponent, this.constraints);
		
		this.valuePanel = this.createValuePanel();
		this.valuePanel.setBackground(this.getBackground());
		
		this.add(this.valuePanel, this.constraints);
	}
	
	@Override
	public void updateComponents() {
		this.item.update();
		
		this.itemNameLabel.setText(this.item.getName());
		
		if(!this.itemNameTextComponent.isEditable()) {
			this.itemNameTextComponent.setText(this.item.getName());
		}
	};
	
	@Override
	public void enableEditMode() {
		this.itemNameLabel.setVisible(false);
		
		this.itemNameTextComponent.setVisible(true);
		this.itemNameTextComponent.setEditable(true);
	}
	
	@Override
	public void disableEditMode(boolean apply) throws Exception {
		if(apply) {
			this.attemptApply();
		}
		
		this.itemNameLabel.setVisible(true);
		
		this.itemNameTextComponent.setVisible(false);
		this.itemNameTextComponent.setEditable(false);
	}
	
	public T getItem() {
		return this.item;
	}
	
	protected abstract JPanel createValuePanel();
	
	protected abstract void attemptApply() throws Exception;
	
}