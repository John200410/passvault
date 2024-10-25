package org.passvault.client.generator;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.passvault.client.Main;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * ASCII Password Generator
 *
 * @author john@chav.is 9/30/2024
 */
public class PasswordGeneratorForm extends JFrame {
	
	/**
	 * The current parameters for the password generator
	 */
	private final GeneratorParameters.Builder params;
	
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
	private JFormattedTextField passwordLengthTextField;
	private JPanel specialCharactersPanel;
	private JLabel specialCharactersLabel;
	private JFormattedTextField specialCharactersCountField;
	private JPanel alphaNumericCharactersPanel;
	private JCheckBox lowercaseAlphabetCheckBox;
	private JCheckBox uppercaseAlphabetCheckBox;
	private JCheckBox numbersCheckBox;
	private JSeparator separator1;
	private JCheckBox ambiguousCharactersCheckBox;
	
	public static PasswordGeneratorForm open(GeneratorParameters.Builder params) {
		final PasswordGeneratorForm frame = new PasswordGeneratorForm(params);
		frame.setVisible(true);
		return frame;
	}
	
	private PasswordGeneratorForm(GeneratorParameters.Builder params) {
		super("Password Generator");
		$$$setupUI$$$();
		this.setContentPane(this.rootPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setResizable(false);
		
		this.params = params;
		this.regeneratePassword();
		
		//set max character length for characters text area
		/*
		this.charactersTextArea.setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if(this.getLength() + str.length() > 256) {
					super.insertString(offs, str.substring(0, 256 - this.getLength()), a);
					return;
				}
				super.insertString(offs, str, a);
			}
		});
		 */
		
		//set initial values
		this.specialCharactersTextArea.setText(new String(params.specialChars));
		
		
		//TODO: save config here at end of init
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
			this.passwordTextArea.setText("ERROR!!!! Check console");
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
		rootPanel.setPreferredSize(new Dimension(450, 300));
		generatorPanel = new JPanel();
		generatorPanel.setLayout(new GridLayoutManager(1, 3, new Insets(5, 15, 10, 15), -1, -1));
		generatorPanel.setName("");
		rootPanel.add(generatorPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		generatorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Generator", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		regenerateButton = new JButton();
		regenerateButton.setText("Regenerate");
		generatorPanel.add(regenerateButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		copyButton = new JButton();
		copyButton.setText("Copy");
		generatorPanel.add(copyButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		passwordTextArea = new JTextArea();
		passwordTextArea.setEditable(true);
		passwordTextArea.setLineWrap(true);
		passwordTextArea.setOpaque(true);
		generatorPanel.add(passwordTextArea, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 50), null, 0, false));
		optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayoutManager(7, 1, new Insets(5, 15, 10, 15), -1, -1));
		optionsPanel.setName("Options");
		rootPanel.add(optionsPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		optionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Options", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		characterSetLabel = new JLabel();
		characterSetLabel.setText("Characters");
		optionsPanel.add(characterSetLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		passwordLengthPanel = new JPanel();
		passwordLengthPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:9dlu:noGrow,fill:48px:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:d:grow"));
		optionsPanel.add(passwordLengthPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		passwordLengthLabel = new JLabel();
		passwordLengthLabel.setText("Password Length");
		CellConstraints cc = new CellConstraints();
		passwordLengthPanel.add(passwordLengthLabel, cc.xy(1, 1));
		passwordLengthSlider = new JSlider();
		passwordLengthSlider.setMaximum(128);
		passwordLengthSlider.setMinimum(1);
		passwordLengthSlider.setPaintLabels(false);
		passwordLengthSlider.setPaintTicks(false);
		passwordLengthSlider.setValue(32);
		passwordLengthPanel.add(passwordLengthSlider, cc.xy(7, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		passwordLengthTextField = new JFormattedTextField();
		passwordLengthTextField.setText("");
		passwordLengthPanel.add(passwordLengthTextField, cc.xy(3, 1));
		specialCharactersPanel = new JPanel();
		specialCharactersPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:5dlu:noGrow,fill:50px:noGrow,left:0dlu:grow(0.19999999999999998),fill:d:noGrow,left:5dlu:noGrow,fill:d:grow", "center:d:grow"));
		optionsPanel.add(specialCharactersPanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
		specialCharactersCountField = new JFormattedTextField();
		specialCharactersCountField.setText("");
		specialCharactersPanel.add(specialCharactersCountField, cc.xy(3, 1));
		alphaNumericCharactersPanel = new JPanel();
		alphaNumericCharactersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		optionsPanel.add(alphaNumericCharactersPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
	}
	
	/** @noinspection ALL */
	public JComponent $$$getRootComponent$$$() {return rootPanel;}
	
	private void createUIComponents() {
		// TODO: place custom component creation code here
	}
}
