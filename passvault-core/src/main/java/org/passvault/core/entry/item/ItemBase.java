package org.passvault.core.entry.item;

/**
 * A base class for {@link IEntryItem} implementations.
 *
 * @author john@chav.is 11/12/2024
 */
public abstract class ItemBase<T> implements IEntryItem<T> {
	
	/**
	 * The name of this item
	 */
	protected String name;
	
	/**
	 * The item type
	 */
	protected final ItemType type;
	
	/**
	 * The item value
	 */
	protected T value;
	
	/**
	 * Flag variable that indicates if this item has been modified since it was last updated on the UI
	 */
	protected boolean dirty = true;
	
	public ItemBase(ItemType type, T value) {
		this.type = type;
		this.name = type.getDefaultName();
		this.value = value;
	}
	
	public ItemBase(ItemType type, String name, T value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public ItemType getType() {
		return this.type;
	}
	
	@Override
	public T getValue() {
		return this.value;
	}
	
	@Override
	public boolean isDirty(boolean clean) {
		final boolean dirty = this.dirty;
		if (clean) {
			this.dirty = false;
		}
		return dirty;
	}
}
