package org.passvault.client.vault;

import org.passvault.client.PassVaultClient;
import org.passvault.core.Globals;
import org.passvault.core.vault.FileVault;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;

public class VaultUnlockForm extends JFrame {
	private JPasswordField masterPasswordTextField;
	private JTextField fileLocationTextField;
	private JButton loginButton;
	private JButton newVaultButton;
	private JPanel rootPanel;
	private JButton openFileButton;
	
	public static VaultUnlockForm open(VaultUnlockForm parent) {
		final VaultUnlockForm frame = new VaultUnlockForm(parent, parent != null);
		
		frame.setLocationRelativeTo(parent); //center of screen
		frame.setVisible(true);
		return frame;
	}
	
	private VaultUnlockForm(VaultUnlockForm parent, boolean createMode) {
		super(String.format("PassVault - %s Vault", createMode ? "Create" : "Unlock"));
		
		//$$$setupUI$$$();
		this.setContentPane(this.rootPanel);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		
		this.setResizable(false);
		this.setSize(350, 211);
		this.setIconImage(PassVaultClient.ICON);
		
		//set default file location
		this.fileLocationTextField.setText(PassVaultClient.APP_DIR.getAbsolutePath() + (createMode ? File.separator + "vault.pv" : ""));
		
		if(PassVaultClient.SETTINGS.previousVaultLocation != null && !PassVaultClient.SETTINGS.previousVaultLocation.isBlank()) {
			this.fileLocationTextField.setText(PassVaultClient.SETTINGS.previousVaultLocation);
		}
		
		if(createMode) {
			this.newVaultButton.setText("Create Vault");
			this.loginButton.setVisible(false);
			
			this.newVaultButton.addActionListener(e -> {
				
				File file = new File(this.fileLocationTextField.getText());
				if(file.isDirectory()) {
					file = new File(file, "vault.pv");
				}
				
				if(file.exists()) {
					JOptionPane.showMessageDialog(this, "File already exists", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try {
					final FileVault vault = FileVault.createNewVault(file, this.masterPasswordTextField.getPassword());
					
					VaultForm.open(vault);
					
					this.dispose();
					parent.dispose();
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(this, "Error creating vault: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					Globals.LOGGER.severe("Error creating vault: " + ex.getMessage());
					ex.printStackTrace();
				}
			});
			
		} else {
			this.fileLocationTextField.setEnabled(false);
			
			//add button listeners
			this.newVaultButton.addActionListener(e -> VaultUnlockForm.open(this));
			
			this.loginButton.addActionListener(e -> {
				
				final File file = new File(this.fileLocationTextField.getText());
				
				try {
					final FileVault vault = new FileVault(file);
					vault.unlock(this.masterPasswordTextField.getPassword());
					
					VaultForm.open(vault);
					
					this.dispose();
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(this, "Error unlocking vault: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					Globals.LOGGER.severe("Error unlocking vault: " + ex.getMessage());
					ex.printStackTrace();
				}
			});
		}
		
		//add text field listeners
		final DocumentListener documentListener = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				VaultUnlockForm.this.updateButtons(createMode);
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				VaultUnlockForm.this.updateButtons(createMode);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				VaultUnlockForm.this.updateButtons(createMode);
			}
		};
		this.fileLocationTextField.getDocument().addDocumentListener(documentListener);
		this.masterPasswordTextField.getDocument().addDocumentListener(documentListener);
		
		this.updateButtons(createMode);
	}
	
	private void updateButtons(boolean createMode) {
		
		final String fileLocation = this.fileLocationTextField.getText();
		
		if(createMode) {
			this.newVaultButton.setEnabled(!fileLocation.isEmpty() && this.masterPasswordTextField.getPassword().length > 0);
		} else {
			final String masterPassword = new String(this.masterPasswordTextField.getPassword());
			this.loginButton.setEnabled(!fileLocation.isEmpty() && !masterPassword.isEmpty());
		}
	}
	
	private void createUIComponents() {
		// TODO: place custom component creation code here
		
		//Creating the buttons
		this.openFileButton = new JButton("Open", UIManager.getIcon("FileView.directoryIcon"));
		this.openFileButton.setHorizontalTextPosition(SwingConstants.LEFT);
		this.openFileButton.addActionListener(e -> {
			
			final File file = new File(this.fileLocationTextField.getText());
			
			final JFileChooser fileChooser = new JFileChooser(file.isDirectory() ? file : file.getParentFile());
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setDialogTitle("Select Vault File Location");
			fileChooser.setFileFilter(new FileNameExtensionFilter("PassVault Files", "pv"));
			
			if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				this.fileLocationTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		});
	}
	
}
