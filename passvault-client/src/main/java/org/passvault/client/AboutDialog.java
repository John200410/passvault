package org.passvault.client;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.passvault.client.vault.VaultForm;
import org.passvault.core.Globals;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AboutDialog extends JDialog {
	private JPanel contentPane;
	private JButton closeButton;
	private JLabel iconLabel;
	private JTextPane textPane;
	
	public static AboutDialog open(VaultForm parent) {
		final AboutDialog frame = new AboutDialog(parent);
		
		frame.setLocationRelativeTo(parent); //center of screen
		frame.setVisible(true);
		frame.pack();
		return frame;
	}
	
	private AboutDialog(JFrame parent) {
		super(parent, "PassVault - About", true);
		this.setSize(350, 200);
		this.setResizable(false);
		
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(closeButton);
		
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {onClose();}
		});
		
		this.setIconImage(PassVaultClient.ICON);
		
		
		final StyledDocument doc = this.textPane.getStyledDocument();
		final Style titleStyle = this.textPane.addStyle("Title", null);
		StyleConstants.setFontSize(titleStyle, 24);
		//StyleConstants.setBold(titleStyle, true);
		final Style subTitleStyle = this.textPane.addStyle("SubTitle", null);
		StyleConstants.setFontSize(subTitleStyle, 18);
		
		try {
			doc.insertString(doc.getLength(), "PassVault " + PassVaultClient.VERSION + "\n", titleStyle);
			doc.insertString(doc.getLength(), "Stay Secure\n", subTitleStyle);
			doc.insertString(doc.getLength(), "\n\n", null);
			doc.insertString(doc.getLength(), "Copyright © 2024 J&P Solutions", null);
		} catch(Exception e) {
			Globals.LOGGER.warning("Error setting text: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void onClose() {
		// add your code here
		dispose();
	}
	
	private void createUIComponents() {
		// TODO: place custom component creation code here
		
		ImageIcon icon = null;
		//load icon
		try {
			icon = new FlatSVGIcon(PassVaultClient.class.getResourceAsStream("/icon.svg")).derive(100, 100);
		} catch(IOException e) {
			Globals.LOGGER.warning("Error loading icon: " + e.getMessage());
			e.printStackTrace();
		}
		this.iconLabel = new JLabel(icon);
	}
}
