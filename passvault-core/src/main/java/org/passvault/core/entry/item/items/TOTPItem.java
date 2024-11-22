package org.passvault.core.entry.item.items;

import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.passvault.core.Globals;
import org.passvault.core.entry.item.ItemException;
import org.passvault.core.entry.item.ItemType;
import org.passvault.core.entry.item.TextItemBase;

/**
 * Item used to store and generate Time-based One-Time Passwords (TOTP).
 *
 * @author john@chav.is 11/21/2024
 */
public class TOTPItem extends TextItemBase {
	
	public static final DefaultCodeGenerator TOTP = new DefaultCodeGenerator();
	
	private String currentCode = "";
	private long lastTimeUpdated = 0;
	
	public TOTPItem(String name, String value) {
		super(ItemType.TOTP, name, value);
	}
	
	@Override
	public void update() {
		if(this.getValue().isBlank()) {
			this.currentCode = "Authentication key not set!";
			return;
		}
		
		final long epochSeconds = System.currentTimeMillis() / 1000;
		final long currentBucket = Math.floorDiv(epochSeconds, 30);
		
		if(currentBucket != lastTimeUpdated) {
			try {
				this.currentCode = TOTP.generate(this.getValue(), currentBucket);
			} catch(CodeGenerationException e) {
				Globals.LOGGER.severe("Error generating TOTP code: " + e.getMessage());
				e.printStackTrace();
				this.currentCode = "Error generating code: " + e.getMessage();
			}
			
			this.lastTimeUpdated = currentBucket;
		}
	}
	
	@Override
	public String getDisplayValue() {
		return this.currentCode;
	}
	
	@Override
	public void setValue(String value) throws ItemException {
		
		//ensure code gets updated
		this.currentCode = "";
		this.lastTimeUpdated = 0;
		
		super.setValue(value);
	}
}