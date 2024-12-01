package org.passvault.client.vault.component.item;

import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.item.TextItemBase;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * Simple item that can be used for usernames, emails, passwords, etc
 *
 * @author john@chav.is 11/14/2024
 */
public class SimpleTextItemComponent extends EntryItemComponentBase<TextItemBase> {
	
	/**
	 * Text component for the value of the item
	 */
	protected ValueTextComponent valueTextComponent;
	
	/**
	 * Button to copy the value of the item to the clipboard
	 */
	protected JButton copyButton;
	
	/**
	 * Used for secret item types
	 */
	private final JButton hideShowButton;
	
	public SimpleTextItemComponent(EntryPanel parent, Entry entry, TextItemBase item, boolean copyable) {
		super(parent, entry, item);
		
		this.valueTextComponent = this.createValueTextComponent();
		this.valueTextComponent.setEditable(false);
		this.valuePanel.add(this.valueTextComponent.getComponent());
		
		if(copyable) {
			this.copyButton = new JButton(new AbstractAction("Copy") {
				@Override
				public void actionPerformed(ActionEvent e) {
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
							new StringSelection(item.getDisplayValue()),
							null
					);
				}
			});
			this.valuePanel.add(this.copyButton);
		}
		
		this.hideShowButton = new JButton("Show");
		
		if(item.getType().isSecret()) {
			this.hideShowButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(SimpleTextItemComponent.this.hideShowButton.getText().equals("Show")) {
						((ValueSecretTextField) SimpleTextItemComponent.this.valueTextComponent).setEchoChar((char) 0);
						SimpleTextItemComponent.this.hideShowButton.setText("Hide");
					} else {
						((ValueSecretTextField) SimpleTextItemComponent.this.valueTextComponent).setEchoChar('•');
						SimpleTextItemComponent.this.hideShowButton.setText("Show");
					}
				}
			});
			this.valuePanel.add(this.hideShowButton, 1);
		}
	}
	
	
	protected ValueTextComponent createValueTextComponent() {
		if(this.item.getType().isSecret()) {
			return new ValueSecretTextField();
		}
		return new ValueTextField();
	}
	
	
	@Override
	protected void attemptApply() throws Exception {
		this.item.setName(this.itemNameTextComponent.getText());
		this.item.setValue(this.valueTextComponent.getText());
	}
	
	@Override
	public void updateComponents() {
		super.updateComponents();
		
		if(this.valueTextComponent != null && !this.valueTextComponent.isEditable()) {
			this.valueTextComponent.setText(this.item.getDisplayValue());
		}
	}
	
	@Override
	public void enableEditMode() {
		super.enableEditMode();
		
		this.valueTextComponent.setEditable(true);
		
		if(this.copyButton != null) {
			this.copyButton.setVisible(false);
		}
	}
	
	@Override
	public void disableEditMode(boolean apply) throws Exception {
		super.disableEditMode(apply);
		
		this.valueTextComponent.setEditable(false);
		
		if(this.copyButton != null) {
			this.copyButton.setVisible(true);
		}
	}
	
	@Override
	protected JPanel createValuePanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		return panel;
	}
	
	public interface ValueTextComponent {
		
		String getText();
		
		void setText(String text);
		
		boolean isEditable();
		
		void setEditable(boolean editable);
		
		JComponent getComponent();
		
	}
	public static class ValueTextArea extends JTextArea implements ValueTextComponent {
		
		public ValueTextArea() {
			super();
		}
		
		@Override
		public void setEditable(boolean b) {
			super.setEditable(b);
			
			//get rid of carets cuz they still render for some reason
			if(!b) {
				this.setFocusable(false);
				this.setFocusable(true);
			}
		}
		
		@Override
		public void setText(String text) {
			if(this.getText().equals(text)) {
				return;
			}
			super.setText(text);
		}
		
		@Override
		public JComponent getComponent() {
			return this;
		}
	}
	public static class ValueTextField extends JTextField implements ValueTextComponent {
		
		public ValueTextField() {
			super();
			this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		}
		
		@Override
		public void setText(String text) {
			if(this.getText().equals(text)) {
				return;
			}
			super.setText(text);
		}
		
		@Override
		public JComponent getComponent() {
			return this;
		}
	}
	public static class ValueSecretTextField extends JPasswordField implements ValueTextComponent {
		
		public ValueSecretTextField() {
			super();
			this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		}
		
		@Override
		public void setText(String text) {
			if(Arrays.equals(this.getPassword(), text.toCharArray())) {
				return;
			}
			super.setText(text);
		}
		
		@Override
		public JComponent getComponent() {
			return this;
		}
	}
}
