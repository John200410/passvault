package org.passvault.client.vault.component;

import org.passvault.client.vault.VaultForm;
import org.passvault.core.Globals;
import org.passvault.core.entry.Entry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * This container will have a scroll pane with the entry panel inside.
 * <p>
 * It will also have a toolbar at the bottom with buttons for editing the entry.
 *
 * @author john@chav.is 11/17/2024
 */
public class EntryContainer extends Container {
	
	private final Entry entry;
	private final VaultForm vaultForm;
	
	
	
	private final EntryPanel entryPanel;
	
	private final JScrollPane entryScrollPane;
	
	private final JToolBar toolBar;
	
	private final JButton editButton;
	private final JButton cancelButton;
	private final JButton saveButton;
	
	private final JCheckBox favoriteCheckBox;
	
	private final JButton deleteButton;
	
	
	private boolean editMode = false;
	
	public EntryContainer(VaultForm vaultForm, Entry entry) {
		this.vaultForm = vaultForm;
		
		
		this.entryPanel = new EntryPanel(entry);
		this.entry = entry;
		
		this.setLayout(new BorderLayout());
		
		this.entryScrollPane = new JScrollPane(entryPanel);
		this.add(this.entryScrollPane, BorderLayout.CENTER);
		
		this.toolBar = new JToolBar();
		this.add(this.toolBar, BorderLayout.SOUTH);
		
		this.editButton = new JButton(new AbstractAction("Edit") {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableEditingMode();
			}
		});
		this.toolBar.add(this.editButton);
		
		
		this.saveButton = new JButton(new AbstractAction("Save") {
			@Override
			public void actionPerformed(ActionEvent e) {
				disableEditingMode(true);
			}
		});
		this.saveButton.setVisible(false);
		this.toolBar.add(this.saveButton);
		
		this.cancelButton = new JButton(new AbstractAction("Cancel") {
			@Override
			public void actionPerformed(ActionEvent e) {
				disableEditingMode(false);
			}
		});
		this.cancelButton.setVisible(false);
		this.toolBar.add(this.cancelButton);
		
		this.toolBar.add(new JToolBar.Separator());
		
		this.deleteButton = new JButton(new AbstractAction("Delete") {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					vaultForm.getVault().deleteEntry(entry);
					vaultForm.updateEntryList();
					vaultForm.viewEntry(null);
				} catch (Exception ex) {
					//TODO: error dialog
					Globals.LOGGER.severe("Error deleting entry");
					ex.printStackTrace();
				}
			}
		});
		this.toolBar.add(this.deleteButton);
		
		this.toolBar.add(Box.createHorizontalGlue());
		
		this.favoriteCheckBox = new JCheckBox("Favorite", entry.getMetadata().favorite);
		this.favoriteCheckBox.addChangeListener(e -> {
			entry.getMetadata().favorite = this.favoriteCheckBox.isSelected();
			
			try {
				vaultForm.getVault().commitEntry(entry);
			} catch (Exception ex) {
				//TODO: error handling
				Globals.LOGGER.severe("Error committing entry");
				ex.printStackTrace();
			}
			
			vaultForm.updateEntryList();
		});
		this.toolBar.add(this.favoriteCheckBox);
		
	}
	
	public void enableEditingMode() {
		this.editMode = true;
		this.editButton.setVisible(false);
		this.cancelButton.setVisible(true);
		this.saveButton.setVisible(true);
		
		this.entryPanel.enableEditingMode();
	}
	
	public void disableEditingMode(boolean save) {
		
		try {
			this.entryPanel.disableEditingMode(save);
		} catch (Exception e) {
			//TODO: error dialog
			Globals.LOGGER.severe("Error disabling editing mode");
			e.printStackTrace();
			return;
		}
		
		if(save) {
			try {
				this.vaultForm.getVault().commitEntry(this.entry);
			} catch (Exception e) {
				//TODO: error handle
				Globals.LOGGER.severe("Error committing entry");
				e.printStackTrace();
			}
		}
		
		this.editMode = false;
		this.editButton.setVisible(true);
		this.cancelButton.setVisible(false);
		this.saveButton.setVisible(false);
	}
	
	public void update() {
		this.entryPanel.update();
	}
	
}