package org.passvault.client.vault.component.item;

import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.Globals;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.items.UrlItem;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author john@chav.is 11/21/2024
 */
public class UrlComponent extends EntryItemComponentBase<UrlItem> {
	
	private final JPanel addPanel;
	private final JButton addButton;
	
	public UrlComponent(EntryPanel parent, Entry entry, UrlItem item) {
		super(parent, entry, item, false);
		
		//modify default value panel
		this.valuePanel.setLayout(new BoxLayout(this.valuePanel, BoxLayout.Y_AXIS));
		this.valuePanel.removeAll();
		
		for(String urls : item.getValue()) {
			UrlPanel urlPanel = new UrlPanel();
			urlPanel.urlField.setText(urls);
			this.valuePanel.add(urlPanel);
		}
		
		this.addPanel = new JPanel();
		this.addPanel.setBackground(this.getBackground());
		this.addPanel.setLayout(new BoxLayout(this.addPanel, BoxLayout.X_AXIS));
		this.addButton = new JButton(new AbstractAction("Add URL") {
			@Override
			public void actionPerformed(ActionEvent e) {
				UrlPanel urlPanel = new UrlPanel();
				urlPanel.enableEditMode();
				UrlComponent.this.valuePanel.add(urlPanel);
				
				UrlComponent.this.valuePanel.remove(UrlComponent.this.addPanel);
				UrlComponent.this.valuePanel.add(UrlComponent.this.addPanel);
				
				UrlComponent.this.valuePanel.revalidate();
				UrlComponent.this.valuePanel.repaint();
			}
		});
		this.addPanel.add(this.addButton);
		this.addPanel.setVisible(false);
		
		this.valuePanel.add(this.addPanel);
	}
	
	@Override
	protected void attemptApply() throws Exception {
		this.item.setName(this.itemNameTextComponent.getText());
		
		final List<String> urls = new ArrayList<>();
		for(Component component : this.valuePanel.getComponents()) {
			if(!(component instanceof UrlPanel panel)) {
				continue;
			}
			
			urls.add(panel.urlField.getText());
		}
		this.item.setValue(urls.toArray(new String[0]));
	}
	
	@Override
	public void enableEditMode() {
		super.enableEditMode();
		
		for(Component component : this.valuePanel.getComponents()) {
			if(!(component instanceof UrlPanel panel)) {
				continue;
			}
			
			panel.enableEditMode();
		}
		
		this.addPanel.setVisible(true);
	}
	
	@Override
	public void disableEditMode(boolean apply) throws Exception {
		super.disableEditMode(apply);
		
		for(Component component : this.valuePanel.getComponents()) {
			if(!(component instanceof UrlPanel panel)) {
				continue;
			}
			
			panel.disableEditMode();
		}
		
		this.addPanel.setVisible(false);
	}
	
	@Override
	protected ValueTextComponent createValueTextComponent() {
		return null;
	}
	
	private class UrlPanel extends JPanel {
		
		private final JTextField urlField;
		private final JButton openButton;
		private final JButton copyButton;
		private final JButton deleteButton;
		
		public UrlPanel() {
			this.setBackground(UrlComponent.this.getBackground());
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			
			this.urlField = new JTextField() {
				@Override
				public void setEditable(boolean b) {
					//Globals.LOGGER.info("Setting editable: " + b);
					super.setEditable(b);
				}
			};
			this.urlField.setEditable(false);
			this.add(this.urlField);
			
			this.openButton = new JButton(new AbstractAction("Open") {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					String urlStr = UrlPanel.this.urlField.getText();
					
					if(!urlStr.contains("://")) {
						urlStr = "https://" + urlStr;
					}
					
					try {
						
						Desktop.getDesktop().browse(URI.create(urlStr));
					} catch(IOException ex) {
						//TODO: error handling with dialog
						
						Globals.LOGGER.severe("Failed to open URL: " + urlStr);
						ex.printStackTrace();
					}
				}
			});
			
			if(Desktop.isDesktopSupported()) {
				this.add(this.openButton);
			}
			
			this.copyButton = new JButton(new AbstractAction("Copy") {
				@Override
				public void actionPerformed(ActionEvent e) {
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
							new StringSelection(UrlPanel.this.urlField.getText()),
							null
					);
				}
			});
			this.add(this.copyButton);
			
			this.deleteButton = new JButton(new AbstractAction("Delete") {
				@Override
				public void actionPerformed(ActionEvent e) {
					UrlComponent.this.valuePanel.remove(UrlPanel.this);
					UrlComponent.this.valuePanel.revalidate();
					UrlComponent.this.valuePanel.repaint();
				}
			});
			this.deleteButton.setVisible(false);
			this.add(this.deleteButton);
		}
		
		public void enableEditMode() {
			this.urlField.setEditable(true);
			this.openButton.setVisible(false);
			this.copyButton.setVisible(false);
			this.deleteButton.setVisible(true);
		}
		
		public void disableEditMode() {
			this.urlField.setEditable(false);
			this.openButton.setVisible(true);
			this.copyButton.setVisible(true);
			this.deleteButton.setVisible(false);
		}
	}
	
}
