package org.passvault.client;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import org.passvault.client.vault.VaultForm;
import org.passvault.core.Globals;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
	
	public static BufferedImage ICON = null;

	static {
		try {
			ICON = ImageIO.read(Main.class.getResourceAsStream("/logo-icon.png"));
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
		VaultForm.open(null);
		//LoginForm.open();
	}
}