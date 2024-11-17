package org.passvault.core.entry.item;

import com.google.gson.JsonObject;
import org.passvault.core.entry.Entry;

/**
 * An item that is to be contained within an {@link Entry}.
 * <p>
 * This can be a text item, hidden text item, TOTP item, etc.
 *
 * @author john@chav.is 9/18/2024
 */
public interface IEntryItem<T> {
	
	/**
	 * @return the item type
	 */
	ItemType getType();
	
	/**
	 * @return the name of this item
	 */
	String getName();
	
	/**
	 * @return the value of this item
	 */
	T getValue();
	
	/**
	 * @return a String representation of the value
	 */
	String[] getDisplayValue();
	
	/**
	 * Updates this item. This gets called frequently
	 */
	default void update() {};
	
	/**
	 * @return true if the item has been modified since it was last saved
	 */
	boolean isDirty(boolean clean);
	
	/**
	 * Serializes the item to a JsonObject
	 *
	 * @return the serialized item
	 */
	JsonObject serialize();
	
}