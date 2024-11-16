package org.passvault.core.entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.passvault.core.entry.item.IEntryItem;

/**
 * An {@link IEntry} that is used to store account data.
 *
 * @author john@chav.is 11/11/2024
 */
public class AccountEntry implements IEntry {
	
	private final EntryMetadata metadata;
	private final IEntryItem<?>[] items;
	
	public AccountEntry(EntryMetadata metadata, IEntryItem<?>... items) {
		this.metadata = metadata;
		this.items = items;
	}
	
	@Override
	public EntryMetadata getMetadata() {
		return this.metadata;
	}
	
	@Override
	public IEntryItem<?>[] getItems() {
		return this.items;
	}
	
	@Override
	public JsonElement serialize() {
		
		final JsonObject accountObj = new JsonObject();
		accountObj.addProperty("name", this.metadata.name);
		accountObj.addProperty("created", this.metadata.created.toString());
		accountObj.addProperty("lastModified", this.metadata.lastModified.toString());
		accountObj.addProperty("favorite", this.metadata.favorite);
		
		//TODO:
		return null;
	}
}
