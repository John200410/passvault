package org.passvault.client.vault.component.item;

import org.passvault.core.entry.item.items.UsernameItem;

import javax.swing.*;

/**
 * @author john@chav.is 11/14/2024
 */
public class UsernameComponent extends JPanel {
	
	/**
	 * The label
	 */
	private final JLabel usernameLabel;
	
	/**
	 * The text field
	 */
	private final JTextField usernameField;
	
	public UsernameComponent(UsernameItem item) {
		this.usernameLabel = new JLabel("Username: ");
		this.usernameField = new JTextField(item.getValue());
		
		this.add(this.usernameLabel);
		this.add(this.usernameField);
	}
	
	public String getUsername() {
		return this.usernameField.getText();
	}
	
	public void setUsername(String username) {
		this.usernameField.setText(username);
	}
	
}
