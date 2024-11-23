package org.passvault.client.vault.component;

import com.formdev.flatlaf.ui.FlatRoundBorder;
import org.passvault.core.entry.Entry;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Base component to be rendered in an {@link EntryPanel}
 *
 * @author john@chav.is 11/17/2024
 */
public abstract class EntryComponent extends JPanel {
	
	protected final EntryPanel parent;
	protected final Entry entry;
	protected final GridBagConstraints constraints;
	
	public EntryComponent(EntryPanel parent, Entry entry) {
		this.parent = parent;
		this.entry = entry;
		
		this.setFocusTraversalPolicy(new DefaultFocusTraversalPolicy() {
			@Override
			protected boolean accept(Component c) {
				if(!(c instanceof JTextComponent)) {
					return false;
				}
				return super.accept(c);
			}
		});
		
		//this.setBackground(new Color(40, 42, 51));
		this.setBackground(parent.getBackground().brighter());
		
		this.setBorder(new FlatRoundBorder());
		this.setLayout(new GridBagLayout());
		
		this.constraints = new GridBagConstraints();
		this.constraints.fill = GridBagConstraints.HORIZONTAL;
		this.constraints.insets = new Insets(5, 5, 5, 5);
		this.constraints.weightx = 1;
		this.constraints.gridx = 0;
	}
	
	public abstract void updateComponents();
	
	public abstract void enableEditMode();
	
	public abstract void disableEditMode(boolean apply) throws Exception;
	
}
