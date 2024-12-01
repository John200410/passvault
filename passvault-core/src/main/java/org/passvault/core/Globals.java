package org.passvault.core;

import com.google.gson.*;
import org.passvault.core.entry.item.IEntryItem;
import org.passvault.core.entry.item.ItemType;
import org.passvault.core.entry.item.items.*;

import java.util.logging.Logger;

/**
 * Class that stores useful global constants.
 *
 * @author john@chav.is 11/16/2024
 */
public class Globals {
	
	public static final Logger LOGGER = Logger.getLogger("PassVault");
	
	public static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.serializeNulls()
			
			//item serializer
			.registerTypeHierarchyAdapter(
					IEntryItem.class,
					(JsonSerializer<IEntryItem<?>>) (item, type, jsonSerializationContext) -> item.serialize()
			)
			//item deserializer
			.registerTypeHierarchyAdapter(
					IEntryItem.class,
					(JsonDeserializer<IEntryItem<?>>) (json, typeOfT, context) -> {
						
						if(!(json instanceof JsonObject obj)) {
							throw new JsonParseException("Expected JsonObject, got " + json.getClass().getSimpleName());
						}
						
						if(!obj.has("type")) {
							throw new JsonParseException("Item does not have a type");
						}
						
						if(!obj.has("name")) {
							throw new JsonParseException("Item does not have a name");
						}
						
						if(!obj.has("value")) {
							throw new JsonParseException("Item does not have a value");
						}
						
						final ItemType type = ItemType.valueOf(obj.get("type").getAsString());
						final String name = obj.get("name").getAsString();
						
						//TODO: complete
						switch(type) {
							case USERNAME:
								return new UsernameItem(name, obj.get("value").getAsString());
							case EMAIL:
								return new EmailItem(name, obj.get("value").getAsString());
							case PASSWORD:
								return new PasswordItem(name, obj.get("value").getAsString());
							case AUTHENTICATOR:
								return new AuthenticatorItem(name, obj.get("value").getAsString());
							case URL:
								
								final JsonArray array = obj.get("value").getAsJsonArray();
								
								final String[] urls = new String[array.size()];
								for(int i = 0; i < array.size(); i++) {
									urls[i] = array.get(i).getAsString();
								}
								
								return new UrlItem(name, urls);
							case NOTE:
								return new NoteItem(name, obj.get("value").getAsString());
							default:
								throw new JsonParseException("Unknown item type: " + type);
						}
					}
			)
			
			.create();
	
}
