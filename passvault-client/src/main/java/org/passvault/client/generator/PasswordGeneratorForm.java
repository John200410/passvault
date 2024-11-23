package org.passvault.client.generator;

import org.passvault.client.PassVaultClient;
import org.passvault.client.vault.VaultForm;
import org.passvault.client.vault.component.item.PasswordComponent;
import org.passvault.core.Globals;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * ASCII Password Generator
 *
 * @author john@chav.is 9/30/2024
 */
public class PasswordGeneratorForm extends JDialog {
	
	/**
	 * The current parameters for the password generator
	 */
	private GeneratorParameters.Builder params;
	private final VaultForm vaultForm;
	
	/**
	 * Generated by GUI Designer
	 */
	private JTextField passwordField;
	private JButton copyButton;
	private JButton regenerateButton;
	private JPanel rootPanel;
	private JPanel optionsPanel;
	private JPanel generatorPanel;
	private JTextArea charactersTextArea;
	private JTextArea passwordTextArea;
	private JLabel characterSetLabel;
	private JTextArea specialCharactersTextArea;
	private JPanel passwordLengthPanel;
	private JLabel passwordLengthLabel;
	private JSlider passwordLengthSlider;
	private JTextField passwordLengthTextField;
	private JPanel specialCharactersPanel;
	private JLabel specialCharactersLabel;
	private JFormattedTextField specialCharactersCountField;
	private JPanel alphaNumericCharactersPanel;
	private JCheckBox lowercaseAlphabetCheckBox;
	private JCheckBox uppercaseAlphabetCheckBox;
	private JCheckBox numbersCheckBox;
	private JSeparator separator1;
	private JCheckBox ambiguousCharactersCheckBox;
	private JSpinner passwordLengthSpinner;
	private JSpinner specialCharactersSpinner;
	private JButton resetToDefaultsButton;
	private JButton applyPasswordButton;
	
	public static PasswordGeneratorForm open(VaultForm parent, GeneratorParameters.Builder params, PasswordComponent component) {
		final PasswordGeneratorForm dialog = new PasswordGeneratorForm(parent, params, component);
		
		dialog.setLocationRelativeTo(null); //center of screen
		dialog.setVisible(true);
		return dialog;
	}
	
	private PasswordGeneratorForm(VaultForm parent, GeneratorParameters.Builder params, PasswordComponent component) {
		super(parent, "Password Generator", component != null);
		this.params = params;
		this.vaultForm = parent;
		
		//$$$setupUI$$$();
		this.setContentPane(this.rootPanel);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.setResizable(false);
		
		this.setIconImage(PassVaultClient.ICON);
		
		this.setAlwaysOnTop(component != null);
		
		this.initializeComponents(this.params, component);
		this.updateValues(this.params);
		this.regeneratePassword();
		
		//TODO: save config here at end of init
	}
	
	@Override
	public void dispose() {
		//update the settings
		PassVaultClient.SETTINGS.preferredGeneratorParams = this.params;
		PassVaultClient.saveSettings();
		super.dispose();
	}
	
	private void initializeComponents(GeneratorParameters.Builder params, PasswordComponent component) {
		
		//button actions
		this.applyPasswordButton.setVisible(component != null);
		this.applyPasswordButton.addActionListener(e -> {
			if(component != null) {
				component.getPasswordField().setText(this.passwordTextArea.getText());
				this.dispose();
			}
		});
		
		this.copyButton.addActionListener(e -> {
			final String password = this.passwordTextArea.getText();
			
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
					new StringSelection(password),
					null
			);
		});
		this.regenerateButton.addActionListener(e -> this.regeneratePassword());
		this.resetToDefaultsButton.addActionListener(e -> {
			this.params = new GeneratorParameters.Builder();
			this.initializeComponents(this.params, component);
			this.updateValues(this.params);
			this.regeneratePassword();
		});
		
		
		/////////////////////////////////////////
		//checkboxes
		/////////////////////////////////////////
		this.lowercaseAlphabetCheckBox.addActionListener(e -> {
			final JCheckBox checkBox = (JCheckBox) e.getSource();
			params.lowercaseAlphabet = checkBox.isSelected();
			regeneratePassword();
		});
		this.uppercaseAlphabetCheckBox.addActionListener(e -> {
			final JCheckBox checkBox = (JCheckBox) e.getSource();
			params.uppercaseAlphabet = checkBox.isSelected();
			regeneratePassword();
		});
		this.numbersCheckBox.addActionListener(e -> {
			final JCheckBox checkBox = (JCheckBox) e.getSource();
			params.numbers = checkBox.isSelected();
			regeneratePassword();
		});
		this.ambiguousCharactersCheckBox.addActionListener(e -> {
			final JCheckBox checkBox = (JCheckBox) e.getSource();
			params.avoidAmbiguousChars = checkBox.isSelected();
			regeneratePassword();
		});
		
		/////////////////////////////////////////
		//password length stuff
		/////////////////////////////////////////
		this.passwordLengthSlider.setValue(params.getPasswordLength());
		this.passwordLengthSlider.setMaximum(GeneratorParameters.MAX_PASSWORD_LENGTH);
		this.passwordLengthSpinner.setModel(new SpinnerNumberModel(
													params.getPasswordLength(),
													1,
													GeneratorParameters.MAX_PASSWORD_LENGTH,
													1
											)
		);
		this.passwordLengthSpinner.addChangeListener(e -> {
			final JSpinner spinner = (JSpinner) e.getSource();
			final int length = (int) spinner.getValue();
			this.updatePasswordLength(length, true, false);
		});
		this.passwordLengthSlider.addChangeListener(e -> {
			final JSlider slider = (JSlider) e.getSource();
			final int length = slider.getValue();
			this.updatePasswordLength(length, false, true);
		});
		/////////////////////////////////////////
		
		/////////////////////////////////////////
		//special characters
		/////////////////////////////////////////
		
		this.specialCharactersSpinner.setModel(new SpinnerNumberModel(
				params.specialCharCount,
				0,
				params.getMaxSpecialChars(),
				1
		));
		this.specialCharactersSpinner.addChangeListener(e -> {
			final JSpinner spinner = (JSpinner) e.getSource();
			params.specialCharCount = (int) spinner.getValue();
			regeneratePassword();
		});
		
		this.specialCharactersTextArea.setText(new String(params.specialChars));
		this.specialCharactersTextArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				params.specialChars = specialCharactersTextArea.getText().toCharArray();
				if(params.specialCharCount > 0) {
					regeneratePassword();
				}
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				params.specialChars = specialCharactersTextArea.getText().toCharArray();
				if(params.specialCharCount > 0 && params.specialChars.length > 0) {
					regeneratePassword();
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				params.specialChars = specialCharactersTextArea.getText().toCharArray();
				if(params.specialCharCount > 0) {
					regeneratePassword();
				}
			}
		});
	}
	
	private void updatePasswordLength(int length, boolean updateSlider, boolean updateSpinner) {
		this.params.passwordLength = length;
		
		if(updateSlider) {
			this.passwordLengthSlider.setValue(length);
		}
		if(updateSpinner) {
			this.passwordLengthSpinner.setValue(length);
		}
		
		((SpinnerNumberModel) specialCharactersSpinner.getModel()).setMaximum(this.params.getMaxSpecialChars());
		regeneratePassword();
	}
	
	public void updateValues(GeneratorParameters.Builder params) {
		this.passwordLengthSlider.setValue(params.getPasswordLength());
		this.passwordLengthSpinner.setValue(params.getPasswordLength());
		
		this.specialCharactersSpinner.setValue(params.specialCharCount);
		this.specialCharactersTextArea.setText(new String(params.specialChars));
		
		this.lowercaseAlphabetCheckBox.setSelected(params.lowercaseAlphabet);
		this.uppercaseAlphabetCheckBox.setSelected(params.uppercaseAlphabet);
		this.numbersCheckBox.setSelected(params.numbers);
		this.ambiguousCharactersCheckBox.setSelected(params.avoidAmbiguousChars);
	}
	
	private void regeneratePassword() {
		try {
			this.passwordTextArea.setText(PasswordGenerator.generatePassword(this.params.build()));
		} catch(GeneratorException e) {
			Globals.LOGGER.severe("Error generating password: " + e.getMessage());
			e.printStackTrace();
			this.passwordTextArea.setText("ERROR!!!! " + e.getMessage());
		}
	}
	
	private void createUIComponents() {
		
		//image icons
		ImageIcon imageCopy = new ImageIcon();
		ImageIcon imageRegen = new ImageIcon();
		
		//buttons
		this.copyButton = new JButton(imageCopy);
		this.regenerateButton = new JButton(imageRegen);
		
		
	}
	
	
}
