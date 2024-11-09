package org.passvault.core.entry.item;

/**
 * An entry item that contains a text value.
 *
 * @author john@chav.is 9/30/2024
 */
public class TextItem implements IEntryItem<String> {
	
	/**
	 * The name of this item
	 */
	private String name;
	
	/**
	 * The value of this item
	 */
	private String value;
	
	public TextItem(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public String getDisplayValue() {
		return this.value;
	}
	
}
