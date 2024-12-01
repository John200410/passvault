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
	
	protected EntryMetadata metadata;
	protected BufferedImage icon;
	protected final ArrayList<IEntryItem<?>> items = new ArrayList<>();
	
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
	public ArrayList<IEntryItem<?>> getItems() {
		return this.items;
	}
	
	/**
	 * @return an image representing this entry
	 */
	public Image getIcon() {
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
		
		final String thisName = this.metadata.name.toLowerCase();
		final String otherName = o.metadata.name.toLowerCase();
		
		//special rule for numbered entries
		final int tp1 = thisName.lastIndexOf("(");
		final int tp2 = thisName.lastIndexOf(")");
		final int op1 = otherName.lastIndexOf("(");
		final int op2 = otherName.lastIndexOf(")");
		if(tp1 != -1 && tp2 != -1 && tp2 > tp1 && op1 != -1 && op2 != -1 && op2 > op1) {
			
			
			final String thisNum = thisName.substring(tp1 + 1, tp2);
			final String otherNum = otherName.substring(op1 + 1, op2);
			try {
				final int thisInt = Integer.parseInt(thisNum);
				final int otherInt = Integer.parseInt(otherNum);
				if(thisInt != otherInt) {
					return thisInt - otherInt;
				}
			} catch(NumberFormatException e) {
				//do nothing
			}
		}
		
		return thisName.compareTo(otherName);
	}
}
