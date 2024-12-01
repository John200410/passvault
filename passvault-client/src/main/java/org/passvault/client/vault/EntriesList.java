package org.passvault.client.vault;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.passvault.client.vault.component.EntryContainer;
import org.passvault.core.Globals;
import org.passvault.core.entry.Entry;
import org.passvault.core.utils.MathUtils;
import org.passvault.core.vault.IVault;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This is a list for the entries in the vault.
 * <p>
 * Favorites will be at the top, followed by the rest of the entries in alphabetical order.
 *
 * @author john@chav.is 11/17/2024
 */
public class EntriesList extends JList<Entry> {
	
	private final VaultForm vaultForm;
	
	/**
	 * A map of entries to their corresponding panels.
	 */
	private final HashMap<Entry, EntryContainer> entryContainers = new HashMap<>();
	
	public EntriesList(VaultForm vaultForm) {
		super();
		this.vaultForm = vaultForm;
		
		this.setCellRenderer(new EntryListCellRenderer());
		
		//selection model that lets you toggle the selection
		/*
		this.setSelectionModel(new DefaultListSelectionModel() {
			
			boolean gestureStarted = false;
			
			@Override
			public void setSelectionInterval(int index0, int index1) {
				if(!this.gestureStarted) {
					if(index0 == index1) {
						if(this.isSelectedIndex(index0)) {
							this.removeSelectionInterval(index0, index0);
							return;
						}
					}
					super.setSelectionInterval(index0, index1);
				}
				this.gestureStarted = true;
			}
			
			@Override
			public void addSelectionInterval(int index0, int index1) {
				if(index0 == index1) {
					if(this.isSelectedIndex(index0)) {
						this.removeSelectionInterval(index0, index0);
						return;
					}
					super.addSelectionInterval(index0, index1);
				}
			}
			
			@Override
			public void setValueIsAdjusting(boolean isAdjusting) {
				if(!isAdjusting) {
					this.gestureStarted = false;
				}
			}
		}); */
		this.setSelectionModel(new EntryListSelectionModel());
		
		this.getModel().addListDataListener(new ListDataListener() {
			@Override
			public void intervalAdded(ListDataEvent e) {
				EntriesList.this.repaint();
			}
			
			@Override
			public void intervalRemoved(ListDataEvent e) {
				EntriesList.this.repaint();
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {
				EntriesList.this.repaint();
			}
		});
	}
	
	public void updateEntries(String filter) {
		final Entry entry = this.getSelectedValue();
		((EntriesListModel)this.getModel()).updateEntries(filter);
		
		//keep the same entry selected
		if(entry != null) {
			this.setSelectedValue(entry, true);
		}
	}
	
	public EntryContainer getEntryContainer(Entry entry) {
		return this.entryContainers.computeIfAbsent(entry, e -> new EntryContainer(this.vaultForm, entry));
	}
	
	public static class EntriesListModel extends AbstractListModel<Entry> {
		
		private final VaultForm parent;
		private final IVault vault;
		private final ArrayList<Entry> entries = new ArrayList<>();
		
		public EntriesListModel(VaultForm vaultForm, IVault vault) {
			this.parent = vaultForm;
			this.vault = vault;
			this.updateEntries("");
		}
		
		private void updateEntries(String filter) {
			this.entries.clear();
			try {
				this.entries.addAll(this.vault.getEntries());
			} catch(Exception e) {
				JOptionPane.showMessageDialog(this.parent, "Error getting entries from vault: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				throw new RuntimeException("Error getting entries from vault: " + e.getMessage(), e);
			}
			
			//filter
			this.entries.removeIf(entry -> !entry.getMetadata().name.toLowerCase().contains(filter.toLowerCase()));
			
			//sort
			Collections.sort(this.entries);
			
			this.fireContentsChanged(this, 0, this.entries.size());
		}
		
		@Override
		public Entry getElementAt(int index) {
			return this.entries.get(index);
		}
		
		@Override
		public int getSize() {
			return this.entries.size();
		}
	}
	
	/**
	 * Custom cell renderer that displays the entry's icon and name, and a favorite icon if the entry is favorited.
	 */
	private class EntryListCellRenderer extends DefaultListCellRenderer {
		
		private static ImageIcon favoritedIcon;
		private static final HashMap<Image, ImageIcon> iconCache = new HashMap<>();
		
		static {
			try {
				favoritedIcon = new FlatSVGIcon(EntryListCellRenderer.class.getResourceAsStream("/favorite.svg"));
			} catch(IOException e) {
				Globals.LOGGER.warning("Error loading favorite icon: " + e.getMessage());
			}
			
			try {
				final FlatSVGIcon icon = new FlatSVGIcon(EntryListCellRenderer.class.getResourceAsStream("/account-default.svg"));
				iconCache.put(null, icon.derive(24, 24));
			} catch(IOException e) {
				Globals.LOGGER.warning("Error loading default account icon: " + e.getMessage());
			}
		}
		
		private final JLabel favoriteLabel = new JLabel(favoritedIcon, LEFT);
		private final JPanel container = new JPanel();
		private final Component filler = Box.createRigidArea(new Dimension(16, 16));
		
		public EntryListCellRenderer() {
			this.container.setLayout(new BoxLayout(this.container, BoxLayout.X_AXIS));
		}
		
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			if(value instanceof Entry entry) {
				this.container.removeAll();
				
				this.setIcon(iconCache.computeIfAbsent(entry.getIcon(), ImageIcon::new));
				this.setText(entry.getMetadata().name);
				
				final EntryContainer entryContainer = EntriesList.this.entryContainers.get(entry);
				if(entryContainer != null && entryContainer.isEditMode()) {
					this.setText("* " + this.getText());
				}
				
				if(entry.getMetadata().favorite) {
					this.container.add(this.favoriteLabel);
				} else {
					this.container.add(this.filler);
				}
				
				this.container.add(this);
				
				return this.container;
			}
			
			return this;
		}
	}
	
	private class EntryListSelectionModel extends DefaultListSelectionModel {
		
		@Override
		public void setAnchorSelectionIndex(final int anchorIndex) {
			Globals.LOGGER.info("setAnchorSelectionIndex: " + anchorIndex);
			super.setAnchorSelectionIndex(anchorIndex);
		}
		
		@Override
		public void setLeadAnchorNotificationEnabled(final boolean flag) {
			Globals.LOGGER.info("setLeadAnchorNotificationEnabled: " + flag);
			super.setLeadAnchorNotificationEnabled(flag);
		}
		
		@Override
		public void setLeadSelectionIndex(final int leadIndex) {
			Globals.LOGGER.info("setLeadSelectionIndex: " + leadIndex);
			super.setLeadSelectionIndex(leadIndex);
		}
		
		@Override
		public void setSelectionInterval(int index0, int index1) {
			final int size = EntriesList.this.getModel().getSize();
			
			if(size > 0 && index0 >= 0) {
				index0 = MathUtils.clamp(index0, 0, size);
				index1 = index0;
				
				final Entry entry = EntriesList.this.getModel().getElementAt(index0);
				EntriesList.this.vaultForm.viewEntry(entry);
			} else {
				EntriesList.this.vaultForm.viewEntry(null);
				this.clearSelection();
				return;
			}
			
			Globals.LOGGER.info("setSelectionInterval: " + index0 + ", " + index1);
			
			super.setSelectionInterval(index0, index1);
		}
	}
	
}
