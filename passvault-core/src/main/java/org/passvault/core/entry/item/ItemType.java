package org.passvault.core.entry.item;

/**
 * Enum that represents all possible types of {@link IEntryItem}s.
 *
 * @author john@chav.is 11/12/2024
 */
public enum ItemType {
	
	USERNAME("Username"),
	EMAIL("Email"),
	PASSWORD("Password", true),
	AUTHENTICATOR("2FA"),
	URL("URLs"),
	FILE("Attachment"),
	NOTE("Notes"),
	CUSTOM(""),
	CUSTOM_SECRET("", true);
	
	/**
	 * The default name assigned to new items of this type
	 */
	private final String defaultName;
	
	/**
	 * Flag variable to determine if this item type is considered a "secret"
	 * <p>
	 * Secret items will be hideable in the UI
	 */
	private final boolean secret;
	
	ItemType(String defaultName) {
		this(defaultName, false);
	}
	
	ItemType(String defaultName, boolean secret) {
		this.defaultName = defaultName;
		this.secret = secret;
	}
	
	/**
	 * @return the default name assigned to new items of this type
	 */
	public String getDefaultName() {
		return this.defaultName;
	}
	
	/**
	 * @return true if this item type is considered a "secret"
	 */
	public boolean isSecret() {
		return this.secret;
	}
	
	public static ItemType fromString(String type) {
		for (ItemType t : ItemType.values()) {
			if (t.name().equalsIgnoreCase(type)) {
				return t;
			}
		}
		return null;
	}
	
}
