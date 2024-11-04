package org.passvault.client;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import org.passvault.client.generator.GeneratorParameters;
import org.passvault.client.generator.PasswordGenerator;
import org.passvault.client.generator.PasswordGeneratorForm;
import org.passvault.client.vault.VaultForm;

import java.util.logging.Logger;

public class Main {
	
	public static final Logger LOGGER = Logger.getLogger("PassVault");
	
	public static void main(String[] args) {
		LOGGER.info("Starting PassVault");
		
		//setup flatlaf theme
		FlatAtomOneDarkIJTheme.setup();
		
		final GeneratorParameters params = new GeneratorParameters.Builder()
				.build();
		
		try {
			final String pass = PasswordGenerator.generatePassword(params);
			LOGGER.info("Generated password: " + pass);
		} catch (Exception e) {
			LOGGER.severe("Error generating password: " + e.getMessage());
			e.printStackTrace();
		}
		
		PasswordGeneratorForm.open(new GeneratorParameters.Builder());
		VaultForm.open(null);
		LoginForm.open();
	}
}