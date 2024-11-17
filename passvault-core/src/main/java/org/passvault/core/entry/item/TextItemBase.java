package org.passvault.core.entry.item;

import com.google.gson.JsonObject;

/**
 * A base {@link IEntryItem} that contains a text value.
 *
 * @author john@chav.is 9/30/2024
 */
public abstract class TextItemBase extends ItemBase<String> {
	
	/**
	 * Cache so that we dont need to create a new array every time
	 */
	protected final String[] cachedDisplayValue = new String[1];
	
	public TextItemBase(ItemType type, String value) {
		super(type, value);
	}
	
	public TextItemBase(ItemType type, String name, String value) {
		super(type, name, value);
	}
	
	@Override
	public String[] getDisplayValue() {
		return this.cachedDisplayValue;
	}
	
	@Override
	public void update() {
		this.cachedDisplayValue[0] = this.value;
	}
	
	@Override
	public JsonObject serialize() {
		final JsonObject obj = new JsonObject();
		obj.addProperty("type", this.type.name());
		obj.addProperty("name", this.name);
		obj.addProperty("value", this.value);
		return obj;
	}
	
}
