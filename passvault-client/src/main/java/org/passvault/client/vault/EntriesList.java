package org.passvault.client.vault;

import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.Globals;
import org.passvault.core.entry.Entry;
import org.passvault.core.vault.IVault;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.image.BufferedImage;
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
	
	private final IVault vault;
	
	/**
	 * A map of entries to their corresponding panels.
	 */
	private final HashMap<Entry, EntryPanel> entryPanels = new HashMap<>();
	
	public EntriesList(IVault vault) {
		super(new EntriesListModel(vault));
		this.vault = vault;
		
		this.setCellRenderer(new EntryListCellRenderer());
		
		this.setSelectionModel(new DefaultListSelectionModel() {
			
			@Override
			public void setSelectionInterval(int index0, int index1) {
				if (index0==index1) {
					if (isSelectedIndex(index0)) {
						removeSelectionInterval(index0, index0);
						return;
					}
				}
				super.setSelectionInterval(index0, index1);
			}
			
			@Override
			public void addSelectionInterval(int index0, int index1) {
				if (index0==index1) {
					if (isSelectedIndex(index0)) {
						removeSelectionInterval(index0, index0);
						return;
					}
					super.addSelectionInterval(index0, index1);
				}
			}
		});
		
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
		((EntriesListModel)this.getModel()).updateEntries(filter);
	}
	
	public EntryPanel getEntryPanel(Entry entry) {
		return this.entryPanels.computeIfAbsent(entry, e -> new EntryPanel(entry));
	}
	
	private static class EntriesListModel extends AbstractListModel<Entry> {
		
		private final IVault vault;
		private final ArrayList<Entry> entries = new ArrayList<>();
		
		public EntriesListModel(IVault vault) {
			this.vault = vault;
			this.updateEntries("");
		}
		
		private void updateEntries(String filter) {
			this.entries.clear();
			try {
				this.entries.addAll(this.vault.getEntries().values());
			} catch(Exception e) {
				//TODO: handle exception
				throw new RuntimeException("Error getting entries from vault: " + e.getMessage(), e);
			}
			
			//filter
			this.entries.removeIf(entry -> !entry.getMetadata().name.toLowerCase().contains(filter.toLowerCase()));
			
			//sort
			Collections.sort(this.entries);
			
			//TODO: do i need this
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
	
	private static class EntryListCellRenderer extends DefaultListCellRenderer {
		
		private static ImageIcon favoritedIcon = null;
		private static final HashMap<Image, ImageIcon> iconCache = new HashMap<>();
		
		static {
			try {
				final Image image = ImageIO.read(EntryListCellRenderer.class.getResourceAsStream("/favorite-icon.png"))
													.getScaledInstance(16, 16, BufferedImage.SCALE_SMOOTH);
				
				favoritedIcon = new ImageIcon(image);
			} catch(IOException e) {
				Globals.LOGGER.warning("Error loading favorite icon: " + e.getMessage());
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
				
				this.setIcon(iconCache.computeIfAbsent(entry.getIcon(), image -> new ImageIcon(image)));
				this.setText(entry.getMetadata().name);
				
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
}
