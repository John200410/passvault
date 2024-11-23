package org.passvault.client.vault;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.passvault.client.Main;
import org.passvault.client.generator.GeneratorParameters;
import org.passvault.client.generator.PasswordGeneratorForm;
import org.passvault.client.vault.component.EntryContainer;
import org.passvault.core.Globals;
import org.passvault.core.entry.Entry;
import org.passvault.core.entry.EntryMetadata;
import org.passvault.core.vault.IVault;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO: document
 *
 * @author john@chav.is 11/3/2024
 */
public class VaultForm extends JFrame {
	
	/**
	 * The vault that this form is displaying
	 */
	private final IVault vault;
	
	/**
	 * The currently selected entry
	 */
	private EntryContainer entryContainer = null;
	
	/**
	 * Timer to update the selected entry panel components frequently
	 */
	private final Timer updateTimer = new Timer();
	
	private JPanel rootPanel;
	
	private EntriesList entriesList;
	private JTextField searchBarTextField;
	private JScrollPane backgroundPane;
	private JLabel backgroundLabel;
	private JSplitPane splitPane;
	private JButton newEntryButton;
	private JButton openButton;
	private JButton passwordGeneratorButton;
	private JButton saveButton;
	private JButton saveAsButton;
	
	public static VaultForm open(IVault vault) {
		final VaultForm frame = new VaultForm(vault);
		
		frame.setLocationRelativeTo(null); //center of screen
		frame.setVisible(true);
		return frame;
	}
	
	private VaultForm(IVault vault) {
		super("PassVault");
		this.vault = vault;
		
		//$$$setupUI$$$();
		this.setContentPane(this.rootPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		
		this.setMinimumSize(new Dimension(800, 300));
		this.setIconImage(Main.ICON);
		
		this.entriesList.setModel(new EntriesList.EntriesListModel(vault));
		
		//this.openButton.setIcon(new FlatFileChooserUI());
		
		//setup search bar
		this.searchBarTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateEntryList();
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateEntryList();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateEntryList();
			}
		});
		
		
		
		//setup new entry button
		this.newEntryButton.addActionListener(e -> {
			
			try {
				
				final List<String> entryNames = this.vault.getEntries().stream().map(Entry::getMetadata).map(m -> m.name).toList();
				String name = "Untitled";
				int i = 1;
				while(entryNames.contains(name)) {
					name = "Untitled (" + i++ + ")";
				}
				
				final EntryMetadata metadata = new EntryMetadata();
				metadata.name = name;
				metadata.timeCreated = metadata.timeModified = System.currentTimeMillis();
				metadata.favorite = false;
				
				final Entry entry = new Entry(metadata, null);
				this.vault.commitEntry(entry);
				
				this.updateEntryList();
				
				this.entriesList.setSelectedValue(entry, true);
				
				//enable editing mode when creating new entry
				this.entryContainer.enableEditingMode();
			} catch(Exception ex) {
				//TODO: exception handle
				Globals.LOGGER.warning("Error creating new entry: " + ex.getMessage());
				ex.printStackTrace();
			}
		});
		
		this.passwordGeneratorButton.addActionListener((l) -> {
			PasswordGeneratorForm.open(new GeneratorParameters.Builder());
		});
		
		//setup background image
		try {
			final BufferedImage backgroundImage = ImageIO.read(this.getClass().getResourceAsStream("/logo-long.png"));
			
			final JViewport viewport = new JViewport() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					
					final int imageWidth = backgroundImage.getWidth();
					final int imageHeight = backgroundImage.getHeight();
					
					double imageScale = ((double) backgroundPane.getHeight() / (imageHeight * 5)) / 2d;
					int scaledImageWidth = (int) (imageWidth * imageScale);
					int scaledImageHeight = (int) (imageHeight * imageScale);
					
					//clamp
					if(scaledImageWidth > backgroundPane.getWidth()) {
						final double scale = (double) scaledImageWidth / backgroundPane.getWidth();
						scaledImageWidth = backgroundPane.getWidth();
						scaledImageHeight = (int) (scaledImageHeight / scale);
					}
					
					if(g instanceof Graphics2D g2d) {
						final AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f);
						g2d.setComposite(ac);
					}
					
					//draw background image
					g.drawImage(backgroundImage,
								backgroundPane.getWidth() / 2 - scaledImageWidth / 2,
								backgroundPane.getHeight() / 2 - scaledImageHeight / 2,
								scaledImageWidth, scaledImageHeight, this
					);
				}
			};
			
			this.backgroundLabel.setIcon(new ImageIcon(backgroundImage));
			this.backgroundPane.setViewport(viewport);
		} catch(Exception e) {
			Globals.LOGGER.warning("Failed to load background image: " + e.getMessage());
		}
		
		
		//setup entry panel update timer
		this.updateTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(entryContainer != null) {
					SwingUtilities.invokeLater(() -> {
						try {
							entryContainer.update();
						} catch(Throwable ignored) {
						}
					});
				}
			}
		}, 100, 100);
	}
	
	public void viewEntry(Entry entry) {
		
		if(entry == null) {
			this.splitPane.setRightComponent(this.backgroundPane);
			this.entryContainer = null;
			return;
		}
		
		this.entryContainer = this.entriesList.getEntryContainer(entry);
		this.splitPane.setRightComponent(this.entryContainer);
	}
	
	public void updateEntryList() {
		this.entriesList.updateEntries(this.searchBarTextField.getText());
	}
	
	public IVault getVault() {
		return this.vault;
	}
	
	public EntriesList getEntriesList() {
		return this.entriesList;
	}
	
	private void createUIComponents() {
		// TODO: place custom component creation code here
		
		
		this.entriesList = new EntriesList(this);
		this.entriesList.addListSelectionListener(e -> {
			if(e.getValueIsAdjusting()) {
				return;
			}
			
			final Entry entry = this.entriesList.getSelectedValue();
			this.viewEntry(entry);
		});
	}
	
}
