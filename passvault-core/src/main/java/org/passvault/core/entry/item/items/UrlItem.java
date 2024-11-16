package org.passvault.core.entry.item.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.passvault.core.entry.item.ItemBase;
import org.passvault.core.entry.item.ItemType;

/**
 * An item that is used to store the URLs that this account can be accessed from.
 *
 * @author john@chav.is 11/14/2024
 */
public class UrlItem extends ItemBase<String[]> {
	
	public UrlItem(String name, String[] value) {
		super(ItemType.URL, name, value);
	}
	
	@Override
	public String[] getDisplayValue() {
		return this.value;
	}
	
	@Override
	public JsonElement serialize() {
		final JsonObject obj = new JsonObject();
		obj.addProperty("type", this.type.name());
		obj.addProperty("name", this.name);
		
		final JsonArray array = new JsonArray();
		for (String url : this.value) {
			array.add(url);
		}
		obj.add("value", array);
		return obj;
	}
	
}
