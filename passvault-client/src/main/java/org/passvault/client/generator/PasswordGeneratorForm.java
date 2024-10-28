package org.passvault.client.generator;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.passvault.client.Main;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * ASCII Password Generator
 *
 * @author john@chav.is 9/30/2024
 */
public class PasswordGeneratorForm extends JFrame {
	
	/**
	 * The current parameters for the password generator
	 */
	private GeneratorParameters.Builder params;
	
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
	
	public static PasswordGeneratorForm open(GeneratorParameters.Builder params) {
		final PasswordGeneratorForm frame = new PasswordGeneratorForm(params);
		
		//TODO: center of screen
		
		frame.setVisible(true);
		return frame;
	}
	
	private PasswordGeneratorForm(GeneratorParameters.Builder params) {
		super("Password Generator");
		this.params = params;
		
		$$$setupUI$$$();
		this.setContentPane(this.rootPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setResizable(false);
		
		this.initializeComponents(this.params);
		this.updateValues(this.params);
		this.regeneratePassword();
		
		//TODO: save config here at end of init
	}
	
	private void initializeComponents(GeneratorParameters.Builder params) {
		
		//button actions
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
			this.initializeComponents(this.params);
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
	
	//TODO: save params to config
	public GeneratorParameters.Builder getParams() {
		return this.params;
	}
	
	private void regeneratePassword() {
		try {
			this.passwordTextArea.setText(PasswordGenerator.generatePassword(this.params.build()));
		} catch(GeneratorException e) {
			Main.LOGGER.severe("Error generating password: " + e.getMessage());
			e.printStackTrace();
			this.passwordTextArea.setText("ERROR!!!! " + e.getMessage());
		}
	}
	
	/**
	 * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		rootPanel = new JPanel();
		rootPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		rootPanel.setMinimumSize(new Dimension(450, 300));
		rootPanel.setName("Password Generator");
		rootPanel.setPreferredSize(new Dimension(450, 350));
		generatorPanel = new JPanel();
		generatorPanel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:d:grow"));
		generatorPanel.setName("");
		rootPanel.add(generatorPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		generatorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Generator", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		regenerateButton = new JButton();
		regenerateButton.setText("Regenerate");
		CellConstraints cc = new CellConstraints();
		generatorPanel.add(regenerateButton, cc.xy(5, 1));
		copyButton = new JButton();
		copyButton.setText("Copy");
		generatorPanel.add(copyButton, cc.xy(3, 1));
		passwordTextArea = new JTextArea();
		passwordTextArea.setColumns(0);
		passwordTextArea.setEditable(true);
		passwordTextArea.setLineWrap(true);
		passwordTextArea.setOpaque(true);
		passwordTextArea.setRows(5);
		generatorPanel.add(passwordTextArea, cc.xy(1, 1));
		optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayoutManager(8, 1, new Insets(5, 15, 10, 15), -1, -1));
		optionsPanel.setName("Options");
		rootPanel.add(optionsPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		optionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Options", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		characterSetLabel = new JLabel();
		characterSetLabel.setText("Characters");
		optionsPanel.add(characterSetLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		passwordLengthPanel = new JPanel();
		passwordLengthPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:9dlu:noGrow,fill:d:grow", "center:d:grow"));
		optionsPanel.add(passwordLengthPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		passwordLengthLabel = new JLabel();
		passwordLengthLabel.setText("Password Length");
		passwordLengthPanel.add(passwordLengthLabel, cc.xy(1, 1));
		passwordLengthSlider = new JSlider();
		passwordLengthSlider.setMajorTickSpacing(8);
		passwordLengthSlider.setMaximum(128);
		passwordLengthSlider.setMinimum(1);
		passwordLengthSlider.setMinorTickSpacing(4);
		passwordLengthSlider.setPaintLabels(false);
		passwordLengthSlider.setPaintTicks(false);
		passwordLengthSlider.setValue(32);
		passwordLengthPanel.add(passwordLengthSlider, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		passwordLengthSpinner = new JSpinner();
		passwordLengthPanel.add(passwordLengthSpinner, cc.xy(3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		specialCharactersPanel = new JPanel();
		specialCharactersPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:noGrow,left:4dlu:noGrow,fill:d:noGrow,left:15dlu:noGrow,fill:d:grow", "center:d:grow"));
		optionsPanel.add(specialCharactersPanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		specialCharactersLabel = new JLabel();
		specialCharactersLabel.setText("Special Characters");
		specialCharactersPanel.add(specialCharactersLabel, cc.xy(1, 1));
		specialCharactersTextArea = new JTextArea();
		specialCharactersTextArea.setAlignmentX(0.5f);
		specialCharactersTextArea.setLineWrap(false);
		specialCharactersTextArea.setMargin(new Insets(0, 0, 0, 0));
		specialCharactersTextArea.setMinimumSize(new Dimension(100, 18));
		specialCharactersTextArea.setPreferredSize(new Dimension(-1, -1));
		specialCharactersPanel.add(specialCharactersTextArea, cc.xy(7, 1, CellConstraints.FILL, CellConstraints.FILL));
		specialCharactersSpinner = new JSpinner();
		specialCharactersPanel.add(specialCharactersSpinner, cc.xy(3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		alphaNumericCharactersPanel = new JPanel();
		alphaNumericCharactersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		optionsPanel.add(alphaNumericCharactersPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		lowercaseAlphabetCheckBox = new JCheckBox();
		lowercaseAlphabetCheckBox.setText("Lowercase (a-z)");
		alphaNumericCharactersPanel.add(lowercaseAlphabetCheckBox);
		uppercaseAlphabetCheckBox = new JCheckBox();
		uppercaseAlphabetCheckBox.setText("Uppercase (A-Z)");
		alphaNumericCharactersPanel.add(uppercaseAlphabetCheckBox);
		numbersCheckBox = new JCheckBox();
		numbersCheckBox.setText("Numbers (0-9)");
		alphaNumericCharactersPanel.add(numbersCheckBox);
		separator1 = new JSeparator();
		optionsPanel.add(separator1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		ambiguousCharactersCheckBox = new JCheckBox();
		ambiguousCharactersCheckBox.setText("Avoid Ambiguous Characters (0 and O, I and l, etc.)");
		optionsPanel.add(ambiguousCharactersCheckBox, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		optionsPanel.add(spacer1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		resetToDefaultsButton = new JButton();
		resetToDefaultsButton.setText("Reset to Defaults");
		optionsPanel.add(resetToDefaultsButton, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}
	
	/** @noinspection ALL */
	public JComponent $$$getRootComponent$$$() {return rootPanel;}
	
	private void createUIComponents() {
		// TODO: place custom component creation code here
	}
	
	
}
