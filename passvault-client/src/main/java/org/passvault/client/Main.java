package org.passvault.client;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import org.passvault.client.vault.VaultForm;
import org.passvault.core.Globals;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.EntryMetadata;
import org.passvault.core.entry.item.items.PasswordItem;
import org.passvault.core.entry.item.items.UsernameItem;
import org.passvault.core.vault.FileVault;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
	
	public static Image ICON = null;

	static {
		try {
			ICON = ImageIO.read(Main.class.getResourceAsStream("/logo-icon.png"))
						  .getScaledInstance(32, 32, BufferedImage.SCALE_SMOOTH);
		} catch(IOException e) {
			Globals.LOGGER.warning("Error loading icon: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Globals.LOGGER.info("Starting PassVault");
		
		//setup flatlaf theme
		FlatAtomOneDarkIJTheme.setup();
		
		/*
		final GeneratorParameters params = new GeneratorParameters.Builder().build();
		
		try {
			final String pass = PasswordGenerator.generatePassword(params);
			LOGGER.info("Generated password: " + pass);
		} catch (Exception e) {
			LOGGER.severe("Error generating password: " + e.getMessage());
			e.printStackTrace();
		}
		
		 */
		
		//PasswordGeneratorForm.open(new GeneratorParameters.Builder());
		
		
		final EntryMetadata metadata = new EntryMetadata();
		metadata.name = "instagram.com";
		metadata.timeCreated = 1088913600;
		metadata.timeModified = System.currentTimeMillis();
		metadata.favorite = true;
		
		final Entry testEntry = new Entry(
				metadata,
				null,
				new UsernameItem("Username", "i.ned.hep2"),
				new PasswordItem("Password", "password123")
		);
		
		
		
		final FileVault vault = new FileVault(new File("test.zip"));
		
		try {
			vault.unlock("password".toCharArray());
			
			//vault.commitEntry(testEntry);
		} catch(Exception e) {
			Globals.LOGGER.severe("Error saving entry: " + e.getMessage());
			e.printStackTrace();
		}
		
		
		VaultForm.open(vault);
		//LoginForm.open();
	}
}