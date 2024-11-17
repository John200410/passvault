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
		final EntryMetadataComponent metadataComponent = new EntryMetadataComponent(this, entry);
		this.add(metadataComponent, c.xy(2, 2));
		
		layout.insertRow(3, gapRowSpec);
		layout.insertRow(4, rowSpec);
		
		
		this.add( new EntryMetadataComponent(this, entry), c.xy(2, 4));
	}
	
	class EntryMetadataComponent extends JPanel {
		
		private final JTextArea nameTextArea;
		
		public EntryMetadataComponent(EntryPanel parent, Entry entry) {
			
			//this.setBackground(new Color(40, 42, 51));
			this.setBackground(parent.getBackground().brighter());
			
			this.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(5, 5, 5, 5);
			c.weightx = 1;
			c.gridx = 0;
			
			final JLabel nameLabel = new JLabel("Name", SwingConstants.LEFT);
			this.nameTextArea = new JTextArea(entry.getMetadata().name);
			
			this.add(nameLabel, c);
			this.add(nameTextArea, c);
		}
	}
	
}
