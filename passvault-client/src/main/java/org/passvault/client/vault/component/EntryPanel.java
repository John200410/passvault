package org.passvault.client.vault.component;

import com.jgoodies.forms.layout.*;
import org.passvault.client.vault.component.item.EntryItemComponentBase;
import org.passvault.client.vault.component.item.SimpleTextItemComponent;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.IEntryItem;
import org.passvault.core.entry.item.items.PasswordItem;
import org.passvault.core.entry.item.items.UsernameItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * TODO: document
 *
 * @author john@chav.is 11/14/2024
 */
public class EntryPanel extends JPanel {
	
	/**
	 * Constants
	 */
	private static final ColumnSpec COLUMN_GAP_SPEC = ColumnSpec.createGap(new ConstantSize(15, ConstantSize.DLUX));
	private static final ColumnSpec COLUMN_SPEC = new ColumnSpec(ColumnSpec.FILL, Sizes.DLUX1, FormSpec.DEFAULT_GROW);
	private static final RowSpec GAP_ROW_SPEC = RowSpec.createGap(new ConstantSize(10, ConstantSize.DLUY));
	private static final RowSpec ROW_SPEC = new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.NO_GROW);
	
	
	private final EntryContainer container;
	
	/**
	 * The entry that this is displaying
	 */
	private final Entry entry;
	
	/**
	 * The components that are currently displayed
	 */
	private final ArrayList<EntryItemComponentBase<? extends IEntryItem<?>>> itemComponents = new ArrayList<>();
	
	/**
	 * A set containing components that are pending to be added to the entry
	 */
	private final HashSet<EntryItemComponentBase<? extends IEntryItem<?>>> pendingComponents = new HashSet<>();
	
	
	private final JButton addButton;
	public boolean shouldRepaint = false;
	
	public EntryPanel(EntryContainer container, Entry entry) {
		this.container = container;
		this.entry = entry;
		
		//darker background
		this.setBackground(new Color(35, 37, 46));
		
		//create add button to add a new item
		this.addButton = new JButton("Add");
		this.addButton.addActionListener(e -> {
			
			/*
			final UsernameItem testItem = new UsernameItem("Username Test", "test username");
			final UsernameComponent testEntry = new UsernameComponent(this, this.entry, testItem);
			
			 */
			
			final PasswordItem testItem = new PasswordItem("Password Test", "password123");
			final PasswordComponent testEntry = new PasswordComponent(this, this.entry, testItem);
			
			//make new entries more visible
			testEntry.setBackground(testEntry.getBackground().brighter());
			
			this.pendingComponents.add(testEntry);
			this.itemComponents.add(testEntry);
			
			SwingUtilities.invokeLater(() -> {
				this.rebuildComponents(false);
			});
		});
		this.addButton.setVisible(false);
		
		this.rebuildComponents(true);
		
		this.addContainerListener(new ContainerListener() {
			@Override
			public void componentAdded(ContainerEvent e) {
				shouldRepaint = true;
			}
			
			@Override
			public void componentRemoved(ContainerEvent e) {
				shouldRepaint = true;
			}
		});
	}
	
	/**
	 * Rebuild the item components in the panel
	 *
	 * @param reset if true, the item components will be cleared and rebuilt from scratch using the current item state
	 */
	public void rebuildComponents(boolean reset) {
		
		if(reset) {
			this.itemComponents.clear();
			this.pendingComponents.clear();
			
			//add initial components
			//populate the entry items
			for(IEntryItem<?> item : this.entry.getItems()) {
				if(item instanceof UsernameItem usernameItem) {
					this.itemComponents.add(new SimpleTextItemComponent(this, entry, usernameItem));
				} else if(item instanceof PasswordItem passwordItem) {
					this.itemComponents.add(new SimpleTextItemComponent(this, entry, passwordItem));
				}
			}
		}
		
		//remove all components
		this.removeAll();
		
		final FormLayout layout = new FormLayout(
				new ColumnSpec[] {COLUMN_GAP_SPEC, COLUMN_SPEC, COLUMN_GAP_SPEC},
				new RowSpec[] {GAP_ROW_SPEC, ROW_SPEC, GAP_ROW_SPEC}
		);
		this.setLayout(layout);
		
		final CellConstraints c = new CellConstraints();
		
		//metadata panel
		final EntryNameComponent metadataComponent = new EntryNameComponent(this, entry);
		this.add(metadataComponent, c.xy(2, 2));
		
		//populate the entry items
		for(EntryItemComponentBase<? extends IEntryItem<?>> item : this.itemComponents) {
			
			//add new row to our layout
			layout.appendRow(ROW_SPEC);
			final int row = layout.getRowCount();
			
			//add the item component
			this.add(item, c.xy(2, row));
			
			//add up and down arrow buttons to move the item up and down
			final ItemAdjustmentPanel buttonPanel = new ItemAdjustmentPanel(item);
			buttonPanel.setVisible(this.container.isEditMode());
			this.add(buttonPanel, c.xy(1, row));
			
			//add delete button
			final DeleteButton deleteButton = new DeleteButton(item);
			deleteButton.setVisible(this.container.isEditMode());
			this.add(deleteButton, c.xy(3, row));
			
			//add gap row
			layout.appendRow(GAP_ROW_SPEC);
		}
		
		this.addButton.setVisible(this.container.isEditMode());
		this.add(this.addButton, c.xy(2, layout.getRowCount()));
		
		this.shouldRepaint = true;
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
			} else if(component instanceof ItemAdjustmentPanel buttonPanel) {
				buttonPanel.setVisible(true);
			} else if(component instanceof DeleteButton deleteButton) {
				deleteButton.setVisible(true);
			}
		}
		this.addButton.setVisible(true);
	}
	
	public void disableEditingMode(boolean save) throws Exception {
		for(Component component : this.getComponents()) {
			if(component instanceof EntryComponent entryComponent) {
				entryComponent.disableEditMode(save);
			}
		}
		
		//if successfully saved, hide these
		for(Component component : this.getComponents()) {
			if(component instanceof ItemAdjustmentPanel buttonPanel) {
				buttonPanel.setVisible(false);
			} else if(component instanceof DeleteButton deleteButton) {
				deleteButton.setVisible(false);
			}
		}
		
		this.addButton.setVisible(false);
		
		if(save) {
			//update entry items
			this.entry.getItems().clear();
			
			for(EntryItemComponentBase<? extends IEntryItem<?>> itemComponent : this.itemComponents) {
				
				if(this.pendingComponents.contains(itemComponent)) {
					this.pendingComponents.remove(itemComponent);
					itemComponent.setBackground(itemComponent.getBackground().darker());
				}
				
				this.entry.getItems().add(itemComponent.getItem());
			}
		} else {
			//remove pending components
			for(EntryItemComponentBase<? extends IEntryItem<?>> itemComponent : this.pendingComponents) {
				this.itemComponents.remove(itemComponent);
			}
			this.pendingComponents.clear();
		}
	}
	
	@Override
	public void repaint() {
		this.revalidate();
		super.repaint();
		for(Component component : this.getComponents()) {
			component.revalidate();
			component.repaint();
		}
	}
	
	public Entry getEntry() {
		return this.entry;
	}
	
	public EntryContainer getContainer() {
		return this.container;
	}
	
	public ArrayList<EntryItemComponentBase<? extends IEntryItem<?>>> getItemComponentsList() {
		return this.itemComponents;
	}
	
	/**
	 * Component for editing the entry name metadata
	 */
	private static class EntryNameComponent extends EntryComponent {
		
		private JTextField nameValueTextField;
		
		public EntryNameComponent(EntryPanel parent, Entry entry) {
			super(parent, entry);
			
			
			this.nameValueTextField = new EntryItemComponentBase.ValueTextField();
			this.nameValueTextField.setEditable(false);
			
			final JLabel nameLabel = new JLabel("Name", SwingConstants.LEFT);
			this.add(nameLabel, this.constraints);
			this.add(this.nameValueTextField, this.constraints);
			
			this.updateComponents();
			if(this.parent.getContainer().isEditMode()) {
				this.enableEditMode();
			}
		}
		
		@Override
		public void updateComponents() {
			if(!this.nameValueTextField.isEditable()) {
				this.nameValueTextField.setText(this.entry.getMetadata().name);
			}
		}
		
		@Override
		public void enableEditMode() {
			this.nameValueTextField.setEditable(true);
		}
		
		@Override
		public void disableEditMode(boolean apply) throws Exception {
			
			if(apply) {
				if(this.nameValueTextField.getText().isBlank()) {
					throw new Exception("Name cannot be blank");
				}
				
				//TODO: check for duplicate name
				
				this.entry.getMetadata().name = this.nameValueTextField.getText();
			}
			
			this.nameValueTextField.setEditable(false);
		}
	}
	
	/**
	 * Panel that contains buttons to adjust the position of an item in the entry
	 *
	 * @author john@chav.is 11/19/2024
	 */
	private class ItemAdjustmentPanel extends JPanel {
		
		private final EntryItemComponentBase<? extends IEntryItem<?>> itemComponent;
		
		public ItemAdjustmentPanel(EntryItemComponentBase<? extends IEntryItem<?>> item) {
			this.itemComponent = item;
			
			this.setBackground(EntryPanel.this.getBackground());
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			//up button
			final JButton upButton = new JButton("▴");
			upButton.addActionListener(e -> {
				final int itemIndex = EntryPanel.this.itemComponents.indexOf(this.itemComponent);
				
				if (itemIndex > 0) {
					EntryPanel.this.itemComponents.remove(itemIndex);
					EntryPanel.this.itemComponents.add(itemIndex - 1, this.itemComponent);
					
					SwingUtilities.invokeLater(() -> {
						EntryPanel.this.rebuildComponents(false);
					});
				}
			});
			this.add(upButton);
			
			//down button
			final JButton downButton = new JButton("▾");
			downButton.addActionListener(e -> {
				
				final int itemIndex = EntryPanel.this.itemComponents.indexOf(this.itemComponent);
				
				if (itemIndex < EntryPanel.this.itemComponents.size() - 1) {
					EntryPanel.this.itemComponents.remove(itemIndex);
					EntryPanel.this.itemComponents.add(itemIndex + 1, this.itemComponent);
					
					SwingUtilities.invokeLater(() -> {
						EntryPanel.this.rebuildComponents(false);
					});
				}
			});
			this.add(downButton);
		}
	}
	
	private class DeleteButton extends JButton {
		
		public DeleteButton(EntryItemComponentBase<? extends IEntryItem<?>> item) {
			super(new AbstractAction("X") {
				@Override
				public void actionPerformed(ActionEvent e) {
					EntryPanel.this.itemComponents.remove(item);
					EntryPanel.this.pendingComponents.remove(item);
					
					SwingUtilities.invokeLater(() -> {
						EntryPanel.this.rebuildComponents(false);
					});
				}
			});
		}
	}
	
}
