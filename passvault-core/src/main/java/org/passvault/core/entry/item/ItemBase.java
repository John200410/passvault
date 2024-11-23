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
	
	public void setName(String name) throws ItemException {
		if(name.isBlank()) {
			throw new ItemException("Name cannot be blank");
		}
		
		this.name = name;
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
	public void setValue(T value) throws ItemException {
		if (value == null) {
			throw new ItemException("Value cannot be null");
		}
		this.value = value;
	}
	
}
