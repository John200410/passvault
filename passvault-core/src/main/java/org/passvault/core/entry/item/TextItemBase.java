package org.passvault.core.entry.item;

import com.google.gson.JsonObject;

/**
 * A base {@link IEntryItem} that contains a text value.
 *
 * @author john@chav.is 9/30/2024
 */
public abstract class TextItemBase extends ItemBase<String> {
	
	public TextItemBase(ItemType type, String value) {
		super(type, value);
	}
	
	public TextItemBase(ItemType type, String name, String value) {
		super(type, name, value);
	}
	
	@Override
	public String getDisplayValue() {
		return this.value;
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
