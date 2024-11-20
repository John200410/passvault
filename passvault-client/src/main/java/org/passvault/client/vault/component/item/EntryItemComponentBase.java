package org.passvault.client.vault.component.item;

import org.passvault.client.vault.component.EntryComponent;
import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.IEntryItem;

import javax.swing.*;
import java.awt.*;

/**
 * @author john@chav.is 11/14/2024
 */
public abstract class EntryItemComponentBase<T extends IEntryItem<?>> extends EntryComponent {

	protected final T item;
	protected JLabel itemNameLabel;
	protected ValueTextField itemNameTextComponent;
	
	protected ValueTextComponent valueTextComponent;
	
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
		
		if(!this.itemNameTextComponent.isEditable()) {
			this.itemNameTextComponent.setText(this.item.getName());
		}
		
		if(!this.valueTextComponent.isEditable()) {
			this.valueTextComponent.setText(this.item.getDisplayValue());
		}
		
		//TODO: do i need this
		if (this.item.isDirty(true)) {
		
		}
	};
	
	@Override
	public void addComponents(GridBagConstraints c) {
		this.itemNameLabel = new JLabel();
		
		this.add(this.itemNameLabel, c);
		
		//TODO character limit
		this.itemNameTextComponent = new ValueTextField();
		this.itemNameTextComponent.setVisible(false);
		this.itemNameTextComponent.setEditable(false);
		
		this.add(this.itemNameTextComponent, c);
		
		this.valueTextComponent = this.createValueTextComponent();
		this.valueTextComponent.setEditable(false);
		this.add(getValueComponent(), c);
	}
	
	@Override
	public void enableEditMode() {
		this.itemNameLabel.setVisible(false);
		
		this.itemNameTextComponent.setVisible(true);
		this.itemNameTextComponent.setEditable(true);
		
		this.valueTextComponent.setEditable(true);
	}
	
	@Override
	public void disableEditMode(boolean apply) throws Exception {
		if(apply) {
			this.attemptApply();
		}
		
		this.itemNameLabel.setVisible(true);
		
		this.itemNameTextComponent.setVisible(false);
		this.itemNameTextComponent.setEditable(false);
		
		this.valueTextComponent.setEditable(false);
	}
	
	public T getItem() {
		return this.item;
	}
	
	protected ValueTextComponent createValueTextComponent() {
		return new ValueTextField();
	}
	
	protected JComponent getValueComponent() {
		return this.valueTextComponent.getComponent();
	}
	
	protected abstract void attemptApply() throws Exception;
	
	public static interface ValueTextComponent {
		
		String getText();
		
		void setText(String text);
		
		boolean isEditable();
		
		void setEditable(boolean editable);
		
		JComponent getComponent();
		
	}
	
	public static class ValueTextField extends JTextField implements ValueTextComponent {
		
		public ValueTextField() {
			super();
			this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		}
		
		@Override
		public JComponent getComponent() {
			return this;
		}
	}
	
	public static class ValueTextArea extends JTextArea implements ValueTextComponent {
		
		public ValueTextArea() {
			super();
		}
		
		@Override
		public void setEditable(boolean b) {
			super.setEditable(b);
			
			//get rid of carets cuz they still render for some reason
			if(!b) {
				this.setFocusable(false);
				this.setFocusable(true);
			}
		}
		
		@Override
		public JComponent getComponent() {
			return this;
		}
	}
	
}