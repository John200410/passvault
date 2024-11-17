package org.passvault.core.entry;

import org.passvault.core.Globals;
import org.passvault.core.entry.item.IEntryItem;
import org.passvault.core.vault.IVault;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * An entry is a collection of items that are stored together.
 * <p>
 * Entries can be stored in a {@link IVault}
 *
 * @author john@chav.is 9/18/2024
 */
public class Entry implements Comparable<Entry> {
	
	/**
	 * The default icon for an account entry.
	 */
	public static Image DEFAULT_ICON = null;
	static {
		try {
			final BufferedImage image = ImageIO.read(Entry.class.getResourceAsStream("/default-account-icon.png"));
			DEFAULT_ICON = image.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		} catch(IOException e) {
			Globals.LOGGER.warning("Error loading default account icon: " + e.getMessage());
		}
	}
	
	protected EntryMetadata metadata;
	protected BufferedImage icon;
	protected final List<IEntryItem<?>> items = new ArrayList<>();
	
	public Entry() {}
	
	public Entry(EntryMetadata metadata, BufferedImage icon, IEntryItem<?>... items) {
		this.metadata = metadata;
		this.icon = icon;
		this.items.addAll(Arrays.asList(items));
	}
	
	/**
	 * Saves this entry to a ZipOutputStream
		 *
		 * @param zos
	 * @throws IOException
		 */
	public void saveTo(ZipOutputStream zos) throws Exception {
		
		final String encodedName = this.metadata.name;//URLEncoder.encode(this.metadata.name, StandardCharsets.UTF_8);
		
		//save metadata.json
		final ZipEntry metadataEntry = new ZipEntry(encodedName + "/metadata.json");
		zos.putNextEntry(metadataEntry);
		zos.write(Globals.GSON.toJson(this.metadata).getBytes());
		zos.closeEntry();
		
		//save items.json
		final ZipEntry itemsEntry = new ZipEntry(encodedName + "/items.json");
		zos.putNextEntry(itemsEntry);
		zos.write(Globals.GSON.toJson(this.items).getBytes());
		zos.closeEntry();
		
		//save icon.png
		if(this.icon != null) {
			final ZipEntry iconEntry = new ZipEntry(encodedName + "/icon.png");
			zos.putNextEntry(iconEntry);
			ImageIO.write(this.icon, "png", zos);
			zos.closeEntry();
		}
		
		//TODO: create something that saves additional resources, like file attachments
	}
	
	/**
	 * @return the metadata of this entry
	 */
	public EntryMetadata getMetadata() {
		return this.metadata;
	}
	
	/**
	 * @return an array of items in this entry
	 */
	public List<IEntryItem<?>> getItems() {
		return this.items;
	}
	
	/**
	 * @return an image representing this entry
	 */
	public Image getIcon() {
		if(this.icon == null) {
			return DEFAULT_ICON;
		}
		return this.icon;
	}
	
	/**
	 * Sets the metadata of this entry
	 *
	 * @param metadata the entry metadata
	 */
	public void setMetadata(EntryMetadata metadata) {
		this.metadata = metadata;
	}
	
	/**
	 * Sets the icon of this entry
	 *
	 * @param icon the entry icon OR null to use the default icon
	 */
	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}
	
	@Override
	public int compareTo(Entry o) {
		if(this.metadata.favorite && !o.metadata.favorite) {
			return -1;
		} else if(!this.metadata.favorite && o.metadata.favorite) {
			return 1;
		}
		return this.metadata.name.compareTo(o.metadata.name);
	}
}
