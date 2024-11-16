package org.passvault.client.vault.component.item;

import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.entry.item.IEntryItem;

import javax.swing.*;
import java.util.StringJoiner;

/**
 * @author john@chav.is 11/14/2024
 */
public abstract class EntryItemComponentBase<T extends IEntryItem> extends JPanel {
	
	protected final EntryPanel parent;
	
	protected final T item;
	protected final JComponent labelComponent;
	protected final JComponent valueComponent;
	
	public EntryItemComponentBase(EntryPanel parent, T item) {
		this.parent = parent;
		this.item = item;
		
		this.add(this.labelComponent = this.createLabelComponent(item));
		this.add(this.valueComponent = this.createValueComponent(item));
	}
	
	public void update() {
		this.item.update();
		
		if (this.item.isDirty(true)) {
			this.updateComponents();
		}
	}
	
	protected void updateComponents() {
		if(this.labelComponent instanceof JLabel label) {
			label.setText(this.item.getName());
		}
	};
	
	protected JComponent createLabelComponent(T item) {
		//TODO: icon?
		final JLabel label = new JLabel(item.getName());
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		return label;
	}
	
	protected JComponent createValueComponent(T item) {
		final JTextArea displayValueTextArea = new JTextArea();
		
		final StringJoiner joiner = new StringJoiner("\n");
		for(String s : item.getDisplayValue()) {
			joiner.add(s);
		}
		
		displayValueTextArea.setText(joiner.toString());
		
		return displayValueTextArea;
	}
	
}
