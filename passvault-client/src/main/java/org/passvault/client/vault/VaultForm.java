package org.passvault.client.vault;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.passvault.client.Main;
import org.passvault.client.vault.component.EntryPanel;
import org.passvault.core.Globals;
import org.passvault.core.entry.AccountEntry;
import org.passvault.core.entry.EntryMetadata;
import org.passvault.core.entry.item.items.PasswordItem;
import org.passvault.core.entry.item.items.UsernameItem;
import org.passvault.core.vault.IVault;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @author john@chav.is 11/3/2024
 */
public class VaultForm extends JFrame {
	
	private JPanel rootPanel;
	private JList list1;
	private JTextField searchBarTextField;
	private JScrollPane backgroundPane;
	private JLabel backgroundLabel;
	private JSplitPane splitPane;
	
	private JScrollPane entryPane;
	
	public static VaultForm open(IVault vault) {
		final VaultForm frame = new VaultForm(vault);
		
		//TODO: center of screen
		
		frame.setVisible(true);
		return frame;
	}
	
	private VaultForm(IVault vault) {
		super("PassVault");
		
		$$$setupUI$$$();
		this.setContentPane(this.rootPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		
		this.setMinimumSize(new Dimension(600, 300));
		this.setIconImage(Main.ICON);
		
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
		
		final EntryMetadata metadata = new EntryMetadata();
		metadata.name = "instagram.com";
		metadata.created = LocalDateTime.of(2004, 7, 4, 0, 0);
		metadata.lastModified = LocalDateTime.now();
		metadata.favorite = true;
		
		
		final AccountEntry testEntry = new AccountEntry(
				metadata,
				new UsernameItem("Username", "i.ned.hep2"),
				new PasswordItem("Password", "password123")
		);
		
		this.entryPane = new JScrollPane();
		this.entryPane.setViewportView(new EntryPanel(testEntry));
		
		this.splitPane.setRightComponent(this.entryPane);
	}
	
	/**
	 * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		rootPanel = new JPanel();
		rootPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		rootPanel.setMinimumSize(new Dimension(600, 300));
		rootPanel.setPreferredSize(new Dimension(600, 500));
		splitPane = new JSplitPane();
		rootPanel.add(splitPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		splitPane.setLeftComponent(panel1);
		final JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setEnabled(true);
		scrollPane1.setInheritsPopupMenu(false);
		scrollPane1.setVerticalScrollBarPolicy(22);
		panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Items", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		list1 = new JList();
		final DefaultListModel defaultListModel1 = new DefaultListModel();
		defaultListModel1.addElement("Instagram.link");
		defaultListModel1.addElement("Canvas.link");
		defaultListModel1.addElement("Walmart.link");
		defaultListModel1.addElement("BestBuy.link");
		defaultListModel1.addElement("SECU.link");
		defaultListModel1.addElement("Outlook.link");
		defaultListModel1.addElement("Amazon.link");
		defaultListModel1.addElement("Target.link");
		defaultListModel1.addElement("Supreme.link");
		defaultListModel1.addElement("Instagram.link");
		defaultListModel1.addElement("Canvas.link");
		defaultListModel1.addElement("Walmart.link");
		defaultListModel1.addElement("BestBuy.link");
		defaultListModel1.addElement("SECU.link");
		defaultListModel1.addElement("Outlook.link");
		defaultListModel1.addElement("Amazon.link");
		defaultListModel1.addElement("Target.link");
		defaultListModel1.addElement("Supreme.link");
		defaultListModel1.addElement("Instagram.link");
		defaultListModel1.addElement("Canvas.link");
		defaultListModel1.addElement("Walmart.link");
		defaultListModel1.addElement("BestBuy.link");
		defaultListModel1.addElement("SECU.link");
		defaultListModel1.addElement("Outlook.link");
		defaultListModel1.addElement("Amazon.link");
		defaultListModel1.addElement("Target.link");
		defaultListModel1.addElement("Supreme.link");
		defaultListModel1.addElement("Instagram.link");
		defaultListModel1.addElement("Canvas.link");
		defaultListModel1.addElement("Walmart.link");
		defaultListModel1.addElement("BestBuy.link");
		defaultListModel1.addElement("SECU.link");
		defaultListModel1.addElement("Outlook.link");
		defaultListModel1.addElement("Amazon.link");
		defaultListModel1.addElement("Target.link");
		defaultListModel1.addElement("Supreme.link");
		list1.setModel(defaultListModel1);
		scrollPane1.setViewportView(list1);
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		panel2.setBorder(BorderFactory.createTitledBorder(null, "Search", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		searchBarTextField = new JTextField();
		searchBarTextField.setName("Search");
		searchBarTextField.setText("");
		panel2.add(searchBarTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
		backgroundPane = new JScrollPane();
		splitPane.setRightComponent(backgroundPane);
		backgroundLabel = new JLabel();
		backgroundLabel.setBackground(new Color(-16371712));
		backgroundPane.setViewportView(backgroundLabel);
	}
	
	/** @noinspection ALL */
	public JComponent $$$getRootComponent$$$() {return rootPanel;}
	
	private void createUIComponents() {
		// TODO: place custom component creation code here
	}
}
