package org.passvault.client;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import org.passvault.client.generator.GeneratorParameters;
import org.passvault.client.generator.PasswordGeneratorForm;
import org.passvault.client.vault.VaultUnlockForm;
import org.passvault.core.Globals;
import org.passvault.core.vault.FileVault;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class Main {
	
	/**
	 * Icon to use for the application
	 */
	public static Image ICON = null;
	
	/**
	 * The directory where settings are stored.
	 * <p>
	 * Default directory for new vaults
	 */
	public static final File APP_DIR = new File(System.getProperty("user.home"), "PassVault");
	
	static {
		
		//load icon
		try {
			ICON = ImageIO.read(Main.class.getResourceAsStream("/logo-icon.png"))
						  .getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
		} catch(IOException e) {
			Globals.LOGGER.warning("Error loading icon: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Globals.LOGGER.info("Starting PassVault");
		
		
		//TODO: create Settings object for settings and save them to settings.json file in APP_DIR
		if(!APP_DIR.exists()) {
			APP_DIR.mkdirs();
		}
		
		
		//setup flatlaf theme
		FlatAtomOneDarkIJTheme.setup();
		UIManager.put("Component.focusWidth", 0);
		UIManager.put("Button.disabledBackground", UIManager.getColor("Button.disabledBackground").darker());
		
		//PasswordGeneratorForm.open(new GeneratorParameters.Builder());
		
		if(true) {
			//PasswordGeneratorForm.open(new GeneratorParameters.Builder());
			VaultUnlockForm.open(null, false);
			return;
		}
		
		final File testVaultFile = new File("test.zip");
		
		final FileVault vault = new FileVault(testVaultFile);
		
		if(!testVaultFile.exists()) {
			try {
				vault.save();
			} catch(Exception e) {
				Globals.LOGGER.severe("Error saving vault: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		//dummy
		try {
			vault.unlock("password".toCharArray());
			
			//vault.commitEntry(testEntry);
		} catch(Exception e) {
			Globals.LOGGER.severe("Error saving entry: " + e.getMessage());
			e.printStackTrace();
		}
		
		//VaultForm.open(vault);
	}
}