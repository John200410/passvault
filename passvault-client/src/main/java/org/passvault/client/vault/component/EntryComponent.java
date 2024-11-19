package org.passvault.client.vault.component;

import org.passvault.core.entry.Entry;

import javax.swing.*;
import java.awt.*;

/**
 * Base component to be rendered in an {@link EntryPanel}
 *
 * @author john@chav.is 11/17/2024
 */
public abstract class EntryComponent extends JPanel {
	
	protected final EntryPanel parent;
	protected final Entry entry;
	
	public EntryComponent(EntryPanel parent, Entry entry) {
		this.parent = parent;
		this.entry = entry;
		
		//this.setBackground(new Color(40, 42, 51));
		this.setBackground(parent.getBackground().brighter());
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 1;
		c.gridx = 0;
		
		this.addComponents(c);
	}
	
	public abstract void addComponents(GridBagConstraints c);
	
	public abstract void updateComponents();
	
	public abstract void enableEditMode();
	
	public abstract void disableEditMode(boolean apply) throws Exception;
	
}
