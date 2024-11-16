package org.passvault.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import org.passvault.core.entry.item.IEntryItem;

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
			.registerTypeHierarchyAdapter(
					IEntryItem.class,
					(JsonSerializer<IEntryItem<?>>) (item, type, jsonSerializationContext) -> item.serialize()
			)
			.create();
	
}
