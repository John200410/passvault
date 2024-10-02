package org.passvault.client;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;

import java.util.logging.Logger;

public class Main {
	
	public static final Logger LOGGER = Logger.getLogger("PassVault");
	
	public static void main(String[] args) {
		LOGGER.info("Starting PassVault");
		
		//setup flatlaf theme
		FlatAtomOneDarkIJTheme.setup();
	}
}