package com.nestof.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.nestof.action.PluginWaitResumeAction;
import com.nestof.component.JPictureTab;
import com.nestof.component.JTreeFileExplorer;
import com.nestof.listener.CustomTreeMouseListener;
import com.nestof.listener.CustomTreeSelectionListener;
import com.nestof.plugin.PictureOrganizer;
import com.nestof.plugin.Plugin;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = -4743176359489192326L;

	private final boolean modeDebugOn = true;

	private static MainWindow instance;

	public static MainWindow getInstance() {
		if (instance == null) {
			instance = new MainWindow();
			instance.loadPlugins();
		}
		return instance;
	}

	private JTextArea infos;// Zone d'informations

	private JPictureTab jPictureTab;// zone d'affichage pour les images

	private JMenuBar menuBar;// Barre de menu

	private JMenu menuPlugins;// Menu de configuration de l'application

	private JToolBar toolBar;// Barre d'outil

	private Plugin currentPlugin;// L'instance de picture
	// oraganizer

	private JTreeFileExplorer tree;// Arborescence des répertoires

	private JLabel statusBarText;// Barre de statut

	private JProgressBar jProgressBar;// Progress Bar

	private Thread pluginThread;// Thread permettant de lancer le plugin

	private HashMap<String, Image> thumbnailsCache;// Cache pour stoker les

	private MainWindow() {
		super();
		this.buildAll();
	}

	private void build() {
		this.setTitle("Explorer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(900, 700);
		this.setLocationRelativeTo(null);
		this.setContentPane(this.buildContentPane());
		this.setJMenuBar(this.buildMenuBar());
		this.thumbnailsCache = new HashMap<String, Image>();
	}

	private void buildAll() {
		this.build();// On initialise notre fenêtre
	}

	/**
	 * Construct the panel.
	 * 
	 * @return a jPanel
	 */
	private JPanel buildContentPane() {
		JPanel panelPrincipal = new JPanel();

		// Barre d'outil
		this.toolBar = new JToolBar();
		this.toolBar.setFloatable(false);
		JButton btnPauseResume = new JButton();
		btnPauseResume.setAction(new PluginWaitResumeAction("Pause/Resume"));
		this.toolBar.add(btnPauseResume);

		// Zone de texte
		this.infos = new JTextArea();
		this.infos.setEditable(false);
		this.infos.setLineWrap(true);
		this.infos.setWrapStyleWord(true);

		// zone d'affichage pour les images
		this.jPictureTab = new JPictureTab(3);

		// Explorateur
		this.tree = new JTreeFileExplorer();
		this.tree.addTreeSelectionListener(new CustomTreeSelectionListener());
		this.tree.addMouseListener(new CustomTreeMouseListener());

		panelPrincipal.setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		// Toolbar
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		panelPrincipal.add(this.toolBar, gridBagConstraints);

		// Arborescence
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.3;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		JScrollPane jScrollPaneTree = new JScrollPane(this.tree);
		jScrollPaneTree.setMinimumSize(new Dimension(200, 400));
		jScrollPaneTree.setPreferredSize(new Dimension(400, 700));
		if (this.modeDebugOn) {
			jScrollPaneTree.setBorder(BorderFactory.createLineBorder(Color.RED,
					1));
		}
		panelPrincipal.add(jScrollPaneTree, gridBagConstraints);

		// Zone d'infos
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 0.3;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		JScrollPane jScrollPaneInfos = new JScrollPane(this.infos);
		jScrollPaneInfos.setMinimumSize(new Dimension(160, 200));
		jScrollPaneInfos.setPreferredSize(new Dimension(300, 500));
		if (this.modeDebugOn) {
			jScrollPaneInfos.setBorder(BorderFactory.createLineBorder(
					Color.YELLOW, 1));
		}
		panelPrincipal.add(jScrollPaneInfos, gridBagConstraints);

		// Zone d'images
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.7;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.LINE_END;
		JScrollPane jScrollPanePictureTab = new JScrollPane(this.jPictureTab);
		jScrollPanePictureTab.setMinimumSize(new Dimension(160, 200));
		jScrollPanePictureTab.setPreferredSize(new Dimension(499, 600));
		if (this.modeDebugOn) {
			jScrollPanePictureTab.setBorder(BorderFactory.createLineBorder(
					Color.GREEN, 1));
		}
		jScrollPanePictureTab.getVerticalScrollBar().setUnitIncrement(150);
		panelPrincipal.add(jScrollPanePictureTab, gridBagConstraints);

		// Barre de progression
		this.jProgressBar = new JProgressBar();
		this.jProgressBar.setStringPainted(true);
		this.jProgressBar.setVisible(false);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = 1;
		panelPrincipal.add(this.jProgressBar, gridBagConstraints);

		// Barre d'état
		this.statusBarText = new JLabel("Status bar");
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		panelPrincipal.add(this.statusBarText, gridBagConstraints);

		return panelPrincipal;
	}

	/**
	 * Construct the menu bar
	 * 
	 * @return
	 */
	private JMenuBar buildMenuBar() {
		// Création de la barre de menu
		this.menuBar = new JMenuBar();

		JMenu menu = new JMenu();
		menu.setText("Fichier");

		this.menuPlugins = new JMenu();
		this.menuPlugins.setText("Plugins");

		this.menuBar.add(menu);
		this.menuBar.add(this.menuPlugins);
		return this.menuBar;
	}

	public Plugin getCurrentPlugin() {
		return this.currentPlugin;
	}

	public JTextArea getInfos() {
		return this.infos;
	}

	public JPictureTab getjPictureTab() {
		return this.jPictureTab;
	}

	public JProgressBar getjProgressBar() {
		return this.jProgressBar;
	}

	public JMenu getMenuPlugins() {
		return this.menuPlugins;
	}

	public Thread getPluginThread() {
		return this.pluginThread;
	}

	public JLabel getStatusBarText() {
		return this.statusBarText;
	}

	public HashMap<String, Image> getThumbnailsCache() {
		return this.thumbnailsCache;
	}

	public JTreeFileExplorer getTree() {
		return this.tree;
	}

	private void loadPlugins() {
		PictureOrganizer.getInstance();
	}

	public void setCurrentPlugin(Plugin currentPlugin) {
		this.currentPlugin = currentPlugin;
	}

	public void setPluginThread(Thread pluginThread) {
		this.pluginThread = pluginThread;
	}

}