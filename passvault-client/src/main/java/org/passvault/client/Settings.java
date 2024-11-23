package org.passvault.client;

import org.passvault.client.generator.GeneratorParameters;

/**
 * Object that stores the client settings
 *
 * @author john@chav.is 11/23/2024
 */
public class Settings {
	
	/**
	 * The path to the last opened vault.
	 */
	public String previousVaultLocation = "";
	
	/**
	 * The last used password generator parameters.
	 */
	public GeneratorParameters.Builder preferredGeneratorParams = new GeneratorParameters.Builder();
	
}
