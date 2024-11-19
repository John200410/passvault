package org.passvault.client.vault;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.passvault.client.Main;
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
		
		//TODO: center of screen
		
		frame.setVisible(true);
		return frame;
	}
	
	private VaultForm(IVault vault) {
		super("PassVault");
		this.vault = vault;
		
		$$$setupUI$$$();
		this.setContentPane(this.rootPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		
		this.setMinimumSize(new Dimension(800, 300));
		this.setIconImage(Main.ICON);
		
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
				
				this.viewEntry(entry);
			} catch(Exception ex) {
				//TODO: exception handle
				Globals.LOGGER.warning("Error creating new entry: " + ex.getMessage());
				ex.printStackTrace();
			}
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
					SwingUtilities.invokeLater(() -> entryContainer.update());
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
	
	/**
	 * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		rootPanel = new JPanel();
		rootPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 5, 5, 5), -1, -1));
		rootPanel.setMinimumSize(new Dimension(600, 300));
		rootPanel.setPreferredSize(new Dimension(600, 500));
		splitPane = new JSplitPane();
		rootPanel.add(splitPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel1.setMinimumSize(new Dimension(250, 148));
		splitPane.setLeftComponent(panel1);
		final JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setEnabled(true);
		scrollPane1.setInheritsPopupMenu(false);
		scrollPane1.setVerticalScrollBarPolicy(22);
		panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Entries", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		entriesList.setDragEnabled(false);
		entriesList.setSelectionMode(0);
		scrollPane1.setViewportView(entriesList);
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		panel2.setBorder(BorderFactory.createTitledBorder(null, "Search", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		searchBarTextField = new JTextField();
		searchBarTextField.setName("Search");
		searchBarTextField.setText("");
		panel2.add(searchBarTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		newEntryButton = new JButton();
		newEntryButton.setText("New Entry");
		panel1.add(newEntryButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		backgroundPane = new JScrollPane();
		splitPane.setRightComponent(backgroundPane);
		backgroundLabel = new JLabel();
		backgroundLabel.setBackground(new Color(-16371712));
		backgroundPane.setViewportView(backgroundLabel);
		final JToolBar toolBar1 = new JToolBar();
		rootPanel.add(toolBar1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
		openButton = new JButton();
		openButton.setText("Open");
		toolBar1.add(openButton);
		saveButton = new JButton();
		saveButton.setText("Save");
		toolBar1.add(saveButton);
		saveAsButton = new JButton();
		saveAsButton.setText("Save As");
		toolBar1.add(saveAsButton);
		final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
		toolBar1.add(toolBar$Separator1);
		passwordGeneratorButton = new JButton();
		passwordGeneratorButton.setText("Password Generator");
		toolBar1.add(passwordGeneratorButton);
	}
	
	/** @noinspection ALL */
	public JComponent $$$getRootComponent$$$() {return rootPanel;}
	
	private void createUIComponents() {
		// TODO: place custom component creation code here
		
		
		this.entriesList = new EntriesList(this, this.vault);
		this.entriesList.addListSelectionListener(e -> {
			if(e.getValueIsAdjusting()) {
				return;
			}
			
			final Entry entry = this.entriesList.getSelectedValue();
			this.viewEntry(entry);
		});
	}
	
}
