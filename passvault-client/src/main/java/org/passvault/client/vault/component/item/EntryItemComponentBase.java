package org.passvault.client.vault.component.item;

import org.passvault.client.vault.component.EntryComponent;
import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.IEntryItem;

import javax.swing.*;
import java.awt.*;
import java.util.StringJoiner;

/**
 * @author john@chav.is 11/14/2024
 */
public abstract class EntryItemComponentBase<T extends IEntryItem<?>> extends EntryComponent {

	protected final T item;
	protected JLabel itemNameLabel;
	protected JTextArea itemNameTextArea;
	
	protected JTextArea valueTextArea;
	
	public EntryItemComponentBase(EntryPanel parent, Entry entry, T item) {
		super(parent, entry);
		this.item = item;
		
		this.updateComponents();
		if(this.parent.getContainer().isEditMode()) {
			this.enableEditMode();
		}
	}
	
	@Override
	public void updateComponents() {
		this.item.update();
		
		this.itemNameLabel.setText(this.item.getName());
		
		if(!this.itemNameTextArea.isEditable()) {
			this.itemNameTextArea.setText(this.item.getName());
		}
		
		if(!this.valueTextArea.isEditable()) {
			String[] displayValue = this.item.getDisplayValue();
			if (displayValue != null) {
				final StringJoiner sj = new StringJoiner("\n");
				for (String value : displayValue) {
					sj.add(value);
				}
				this.valueTextArea.setText(sj.toString());
			}
		}
		
		//TODO: do i need this
		if (this.item.isDirty(true)) {
		
		}
	};
	
	@Override
	public void addComponents(GridBagConstraints c) {
		this.itemNameLabel = new JLabel();
		
		this.add(this.itemNameLabel, c);
		
		this.itemNameTextArea = new JTextArea();
		this.itemNameTextArea.setVisible(false);
		this.itemNameTextArea.setEditable(false);
		
		this.add(this.itemNameTextArea, c);
		
		this.valueTextArea = new JTextArea();
		this.valueTextArea.setEditable(false);
		this.add(getValueComponent(), c);
	}
	
	@Override
	public void enableEditMode() {
		this.itemNameLabel.setVisible(false);
		
		this.itemNameTextArea.setVisible(true);
		this.itemNameTextArea.setEditable(true);
		
		this.valueTextArea.setEditable(true);
	}
	
	@Override
	public void disableEditMode(boolean apply) throws Exception {
		if(apply) {
			this.attemptApply();
		}
		
		this.itemNameLabel.setVisible(true);
		
		this.itemNameTextArea.setVisible(false);
		this.itemNameTextArea.setEditable(false);
		
		this.valueTextArea.setEditable(false);
		
		//get rid of carets cuz it still renders for some reason
		for(Component component : this.getComponents()) {
			if(component instanceof JTextArea c) {
				c.setFocusable(false);
				c.setFocusable(true);
			}
		}
	}
	
	public T getItem() {
		return this.item;
	}
	
	protected JComponent getValueComponent() {
		return this.valueTextArea;
	}
	
	protected abstract void attemptApply() throws Exception;
	
}
