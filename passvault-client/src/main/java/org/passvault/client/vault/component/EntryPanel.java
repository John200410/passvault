package org.passvault.client.vault.component;

import com.jgoodies.forms.layout.*;
import org.passvault.core.entry.Entry;

import javax.swing.*;
import java.awt.*;

/**
 * TODO: document
 *
 * @author john@chav.is 11/14/2024
 */
public class EntryPanel extends JPanel {
	
	/**
	 * The entry that this pane is displaying
	 */
	private final Entry entry;
	
	public EntryPanel(Entry entry) {
		this.entry = entry;
		
		//darker background
		this.setBackground(new Color(35, 37, 46));
		
		final ColumnSpec columnGapSpec = ColumnSpec.createGap(new ConstantSize(10, ConstantSize.DLUX));
		final ColumnSpec columnSpec = new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW);
		final RowSpec gapRowSpec = RowSpec.createGap(new ConstantSize(10, ConstantSize.DLUY));
		final RowSpec rowSpec = new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.NO_GROW);
		
		final FormLayout layout = new FormLayout(
				new ColumnSpec[] {columnGapSpec, columnSpec, columnGapSpec},
				new RowSpec[] {gapRowSpec, rowSpec, gapRowSpec}
				);
		this.setLayout(layout);
		
		final CellConstraints c = new CellConstraints();
		//c.insets = new Insets(10, 10, 10, 10);
		
		/*
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10);
		c.ipadx = c.ipady = 5;
		c.weightx = 1;
		c.gridx = 0;
		 */
		
		//metadata panel
		final EntryNameComponent metadataComponent = new EntryNameComponent(this, entry);
		this.add(metadataComponent, c.xy(2, 2));
		
		layout.insertRow(3, gapRowSpec);
		layout.insertRow(4, rowSpec);
		
		//this.add(new EntryNameComponent(this, entry), c.xy(2, 4));
	}

	public Entry getEntry() {
		return this.entry;
	}
	
	public void update() {
		for(Component component : this.getComponents()) {
			if(component instanceof EntryComponent entryComponent) {
				entryComponent.updateComponents();
			}
		}
	}
	
	public void enableEditingMode() {
		for(Component component : this.getComponents()) {
			if(component instanceof EntryComponent entryComponent) {
				entryComponent.enableEditMode();
			}
		}
	}
	
	public void disableEditingMode(boolean save) throws Exception {
		for(Component component : this.getComponents()) {
			if(component instanceof EntryComponent entryComponent) {
				entryComponent.disableEditMode(save);
			}
		}
	}
	
	static class EntryNameComponent extends EntryComponent {
		
		private JTextArea nameTextArea;
		
		public EntryNameComponent(EntryPanel parent, Entry entry) {
			super(parent, entry);
			this.nameTextArea.setEditable(false);
		}
		
		@Override
		public void addComponents(GridBagConstraints c) {
			
			this.nameTextArea = new JTextArea();
			
			final JLabel nameLabel = new JLabel("Name", SwingConstants.LEFT);
			this.add(nameLabel, c);
			this.add(this.nameTextArea, c);
		}
		
		@Override
		public void updateComponents() {
			if(!this.nameTextArea.isEditable()) {
				this.nameTextArea.setText(this.entry.getMetadata().name);
			}
		}
		
		@Override
		public void enableEditMode() {
			this.nameTextArea.setEditable(true);
		}
		
		@Override
		public void disableEditMode(boolean apply) throws Exception {
			
			if(apply) {
				if(this.nameTextArea.getText().isBlank()) {
					throw new Exception("Name cannot be blank");
				}
				
				//TODO: check for duplicate name
				
				this.entry.getMetadata().name = this.nameTextArea.getText();
			}
			
			this.nameTextArea.setEditable(false);
		}
	}
	
}
