package org.passvault.client;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import org.passvault.client.vault.VaultUnlockForm;
import org.passvault.core.Globals;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The main class for initializing the PassVault client.
 *
 * @author john@chav.is
 */
public class PassVaultClient {
	
	/**
	 * The directory where settings are stored.
	 * <p>
	 * Default directory for new vaults
	 */
	public static final File APP_DIR = new File(System.getProperty("user.home"), "PassVault");
	
	/**
	 * The settings file location
	 */
	private static final File SETTINGS_FILE = new File(APP_DIR, "settings.json");
	
	/**
	 * Icon to use for the application
	 */
	public static Image ICON = null;
	
	/**
	 * The settings
	 */
	public static Settings SETTINGS = new Settings();
	
	static {
		
		//load icon
		try {
			ICON = ImageIO.read(PassVaultClient.class.getResourceAsStream("/logo-icon.png"))
						  .getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
		} catch(IOException e) {
			Globals.LOGGER.warning("Error loading icon: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Globals.LOGGER.info("Starting PassVault");
		
		if(!APP_DIR.exists()) {
			APP_DIR.mkdirs();
		}
		
		//load settings
		try(FileReader settingsReader = new FileReader(SETTINGS_FILE)) {
			final Settings loadedSettings = Globals.GSON.fromJson(settingsReader, Settings.class);
			if(loadedSettings != null) {
				SETTINGS = loadedSettings;
			} else {
				throw new IOException("Settings file is empty");
			}
		} catch(IOException e) {
			Globals.LOGGER.warning("Error loading settings: " + e.getMessage());
			saveSettings();
		}
		
		
		//setup flatlaf theme
		FlatAtomOneDarkIJTheme.setup();
		UIManager.put("Component.focusWidth", 0);
		UIManager.put("Button.disabledBackground", UIManager.getColor("Button.disabledBackground").darker());
		
		
		//open the unlock form
		VaultUnlockForm.open(null);
	}
	
	public static void saveSettings() {
		try(FileWriter writer = new FileWriter(SETTINGS_FILE)) {
			Globals.GSON.toJson(SETTINGS, writer);
		} catch(IOException e) {
			Globals.LOGGER.warning("Error saving settings: " + e.getMessage());
		}
	}
	
}