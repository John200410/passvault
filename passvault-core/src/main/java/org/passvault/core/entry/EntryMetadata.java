package org.passvault.core.entry;

import java.time.LocalDateTime;

/**
 * Object containing metadata for an {@link IEntry}.
 *
 * @author john@chav.is 11/8/2024
 */
public class EntryMetadata {
	
	public String name;
	public LocalDateTime created;
	public LocalDateTime lastModified;
	public boolean favorite;
	
}
