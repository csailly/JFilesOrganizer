package com.nestof.ihm.plugin;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import com.nestof.action.PictureOrganizerSaveSettingsAction;
import com.nestof.plugin.PictureOrganizer;

public class PictureOrganizerSettingsWindow extends JDialog implements
		ActionListener, ItemListener {

	private static final long serialVersionUID = -8437751348665169253L;

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PictureOrganizerSettingsWindow pictureOrganizerSettings = new PictureOrganizerSettingsWindow(
						PictureOrganizer.getInstance());
				pictureOrganizerSettings.setVisible(true);
			}
		});
	}

	private JCheckBox checkBoxOrganize;
	private JCheckBox checkBoxRename;
	private JCheckBox checkBoxRecursive;
	private JCheckBox checkBoxIndexExistingFilename;
	private JCheckBox checkBoxOverwriteExistingFile;
	private JCheckBox checkBoxKeepOriginalFile;
	private JCheckBox checkBoxKeepOriginalDirectoryStructure;
	private JCheckBox checkBoxUseSpecificDestDirectory;
	private JCheckBox checkBoxTreatVideoFiles;

	private JCheckBox checkBoxTreatImageFiles;
	private JLabel labelOrganize;
	private JLabel labelRecursive;
	private JLabel labelIndex;
	private JLabel labelOverwrite;
	private JLabel labelKeepOriginal;
	private JLabel labelKeepOriginalStructure;
	private JLabel labelUseSpecificDestDirectory;
	private JLabel labelRename;
	private JLabel labelSelectSpecificDirectory;
	private JLabel labelTreatVideoFiles;

	private JLabel labelTreatImageFiles;

	private JTextField textFieldSpecificDirectory;

	private JButton buttonSelectSpecificDirectory;

	private final PictureOrganizer pictureOrganizer;

	public PictureOrganizerSettingsWindow(PictureOrganizer pictureOrganizer) {
		super();
		this.pictureOrganizer = pictureOrganizer;
		this.build();// On initialise notre fenêtre
		this.initialize();// On initailise les valeurs des composants
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.buttonSelectSpecificDirectory)) {
			FileSystemView vueSysteme = FileSystemView.getFileSystemView();
			File home = vueSysteme.getHomeDirectory();
			if ((this.textFieldSpecificDirectory.getText() != null)
					&& new File(this.textFieldSpecificDirectory.getText())
							.exists()) {
				home = new File(this.textFieldSpecificDirectory.getText());
			}

			// création et affichage des JFileChooser
			JFileChooser homeChooser = new JFileChooser(home);
			homeChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = homeChooser.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				this.textFieldSpecificDirectory.setText(homeChooser
						.getSelectedFile().getAbsolutePath());
			}
		}

	}

	private void build() {
		this.setTitle("PictureOrganizer Settings");
		this.setSize(600, 400);
		// this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setModal(true);
		this.setContentPane(this.buildContentPane());
	}

	private JPanel buildContentPane() {
		JPanel panelPrincipal = new JPanel();

		// Les checkbox
		this.checkBoxOrganize = new JCheckBox();
		this.checkBoxRecursive = new JCheckBox();
		this.checkBoxOverwriteExistingFile = new JCheckBox();
		this.checkBoxKeepOriginalFile = new JCheckBox();
		this.checkBoxKeepOriginalDirectoryStructure = new JCheckBox();
		this.checkBoxRename = new JCheckBox();
		this.checkBoxIndexExistingFilename = new JCheckBox();
		this.checkBoxUseSpecificDestDirectory = new JCheckBox();
		this.checkBoxTreatImageFiles = new JCheckBox();
		this.checkBoxTreatVideoFiles = new JCheckBox();

		// Les labels
		this.labelTreatImageFiles = new JLabel("Traiter les fichiers d'images");
		this.labelTreatVideoFiles = new JLabel("Traiter les fichiers vidéos");
		this.labelOrganize = new JLabel("Classer");
		this.labelRename = new JLabel("Renommer");
		this.labelRecursive = new JLabel("Traiter les sous dossiers");

		this.labelKeepOriginal = new JLabel("Conserver les fichiers traités");
		this.labelKeepOriginalStructure = new JLabel("Conserver l'arborescence");

		this.labelIndex = new JLabel("Indexer les doublons");
		this.labelOverwrite = new JLabel(
				"Ecraser les fichiers en cas de doublons");

		this.labelUseSpecificDestDirectory = new JLabel(
				"Utiliser un dossier de destination spécifique");
		this.labelSelectSpecificDirectory = new JLabel(
				"Sélectionner le dossier de destination");
		this.labelSelectSpecificDirectory.setVisible(false);

		// Champs de saisie
		this.textFieldSpecificDirectory = new JTextField();
		this.textFieldSpecificDirectory
				.setPreferredSize(new Dimension(350, 20));
		this.textFieldSpecificDirectory.setMinimumSize(new Dimension(350, 20));
		this.textFieldSpecificDirectory.setEditable(false);
		this.textFieldSpecificDirectory.setVisible(false);

		// Boutons
		this.buttonSelectSpecificDirectory = new JButton("...");
		this.buttonSelectSpecificDirectory.addActionListener(this);
		this.buttonSelectSpecificDirectory.setVisible(false);
		this.buttonSelectSpecificDirectory.setPreferredSize(new Dimension(20,
				20));

		JButton buttonValider = new JButton();
		JButton buttonAnnuler = new JButton("Annuler");

		// Les listeners
		this.checkBoxUseSpecificDestDirectory.addItemListener(this);
		this.checkBoxOrganize.addItemListener(this);
		this.checkBoxRename.addItemListener(this);
		this.checkBoxIndexExistingFilename.addItemListener(this);
		this.checkBoxOverwriteExistingFile.addItemListener(this);

		buttonValider.setAction(new PictureOrganizerSaveSettingsAction(
				"Valider"));

		panelPrincipal.setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.anchor = GridBagConstraints.LINE_START;

		// Check box Video Files
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(5, 0, 1, 0);
		panelPrincipal.add(this.checkBoxTreatVideoFiles, gridBagConstraints);

		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		panelPrincipal.add(this.labelTreatVideoFiles, gridBagConstraints);

		// Check box Image FIles
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(5, 0, 1, 0);
		panelPrincipal.add(this.checkBoxTreatImageFiles, gridBagConstraints);

		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		panelPrincipal.add(this.labelTreatImageFiles, gridBagConstraints);

		// Check box Organize
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(5, 0, 1, 0);
		panelPrincipal.add(this.checkBoxOrganize, gridBagConstraints);

		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		panelPrincipal.add(this.labelOrganize, gridBagConstraints);

		// Checkbox rename
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = 1;
		panelPrincipal.add(this.checkBoxRename, gridBagConstraints);

		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		panelPrincipal.add(this.labelRename, gridBagConstraints);

		// Check box Recusive
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(5, 0, 1, 0);
		panelPrincipal.add(this.checkBoxRecursive, gridBagConstraints);

		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		panelPrincipal.add(this.labelRecursive, gridBagConstraints);

		// Checkbox overwrite
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		panelPrincipal.add(this.checkBoxOverwriteExistingFile,
				gridBagConstraints);

		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		panelPrincipal.add(this.labelOverwrite, gridBagConstraints);

		// Checkbox index
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		panelPrincipal.add(this.checkBoxIndexExistingFilename,
				gridBagConstraints);

		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		panelPrincipal.add(this.labelIndex, gridBagConstraints);

		// Checkbox keepOriginal
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = 1;
		panelPrincipal.add(this.checkBoxKeepOriginalFile, gridBagConstraints);

		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		panelPrincipal.add(this.labelKeepOriginal, gridBagConstraints);

		// Checkbox keepOriginalStructure
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = 1;
		panelPrincipal.add(this.checkBoxKeepOriginalDirectoryStructure,
				gridBagConstraints);

		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		panelPrincipal.add(this.labelKeepOriginalStructure, gridBagConstraints);

		// Checkbox useSpecificDestDirectory
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = 1;
		panelPrincipal.add(this.checkBoxUseSpecificDestDirectory,
				gridBagConstraints);

		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		panelPrincipal.add(this.labelUseSpecificDestDirectory,
				gridBagConstraints);

		// Zone sélection du dossier de destination
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		panelPrincipal.add(this.labelSelectSpecificDirectory,
				gridBagConstraints);

		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		panelPrincipal.add(this.textFieldSpecificDirectory, gridBagConstraints);

		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;
		panelPrincipal.add(this.buttonSelectSpecificDirectory,
				gridBagConstraints);

		// Bouton annuler
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.weightx = 0.5;
		panelPrincipal.add(buttonAnnuler, gridBagConstraints);

		// Bouton valider
		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		panelPrincipal.add(buttonValider, gridBagConstraints);

		return panelPrincipal;
	}

	public JCheckBox getCheckBoxIndexExistingFilename() {
		return this.checkBoxIndexExistingFilename;
	}

	public JCheckBox getCheckBoxKeepOriginalDirectoryStructure() {
		return this.checkBoxKeepOriginalDirectoryStructure;
	}

	public JCheckBox getCheckBoxKeepOriginalFile() {
		return this.checkBoxKeepOriginalFile;
	}

	public JCheckBox getCheckBoxOrganize() {
		return this.checkBoxOrganize;
	}

	public JCheckBox getCheckBoxOverwriteExistingFile() {
		return this.checkBoxOverwriteExistingFile;
	}

	public JCheckBox getCheckBoxRecursive() {
		return this.checkBoxRecursive;
	}

	public JCheckBox getCheckBoxRename() {
		return this.checkBoxRename;
	}

	public JCheckBox getCheckBoxTreatImageFiles() {
		return this.checkBoxTreatImageFiles;
	}

	public JCheckBox getCheckBoxTreatVideoFiles() {
		return this.checkBoxTreatVideoFiles;
	}

	public JCheckBox getCheckBoxUseSpecificDestDirectory() {
		return this.checkBoxUseSpecificDestDirectory;
	}

	public JTextField getTextFieldSpecificDirectory() {
		return this.textFieldSpecificDirectory;
	}

	public void initialize() {
		this.checkBoxTreatImageFiles.setSelected(this.pictureOrganizer
				.isOptTreatImageFiles());
		this.checkBoxTreatVideoFiles.setSelected(this.pictureOrganizer
				.isOptTreatVideoFiles());

		this.checkBoxKeepOriginalFile.setSelected(this.pictureOrganizer
				.isOptKeepOriginalFile());
		this.checkBoxKeepOriginalDirectoryStructure
				.setSelected(this.pictureOrganizer
						.isOptKeepOriginalDirectoryStructure());
		this.checkBoxOverwriteExistingFile.setSelected(this.pictureOrganizer
				.isOptOverwriteExistingFile());
		this.checkBoxRecursive.setSelected(this.pictureOrganizer
				.isOptRecursive());
		this.checkBoxUseSpecificDestDirectory.setSelected(this.pictureOrganizer
				.isOptUseSpecificDestDirectory());
		if (this.pictureOrganizer.getSpecificDestDirectory() != null) {
			this.textFieldSpecificDirectory.setText(this.pictureOrganizer
					.getSpecificDestDirectory().toAbsolutePath().toString());
		}
		if (this.pictureOrganizer.isOptUseSpecificDestDirectory()) {
			this.labelSelectSpecificDirectory.setVisible(true);
			this.textFieldSpecificDirectory.setVisible(true);
			this.buttonSelectSpecificDirectory.setVisible(true);
		}
		this.checkBoxRename.setSelected(this.pictureOrganizer.isOptRename());

		this.checkBoxIndexExistingFilename.setSelected(this.pictureOrganizer
				.isIndexExistingFilename());
		this.checkBoxOrganize
				.setSelected(this.pictureOrganizer.isOptOrganize());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource().equals(this.checkBoxUseSpecificDestDirectory)) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				this.labelSelectSpecificDirectory.setVisible(true);
				this.textFieldSpecificDirectory.setVisible(true);
				this.buttonSelectSpecificDirectory.setVisible(true);
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				this.labelSelectSpecificDirectory.setVisible(false);
				this.textFieldSpecificDirectory.setVisible(false);
				this.buttonSelectSpecificDirectory.setVisible(false);
			}
		} else if (e.getSource().equals(this.checkBoxOverwriteExistingFile)) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				this.checkBoxIndexExistingFilename.setSelected(false);
			}
		} else if (e.getSource().equals(this.checkBoxIndexExistingFilename)) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				this.checkBoxOverwriteExistingFile.setSelected(false);
			}
		} else if (e.getSource().equals(this.checkBoxOrganize)) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				if (!this.checkBoxRename.isSelected()) {
					this.checkBoxRename.setSelected(true);
				}
			}
		} else if (e.getSource().equals(this.checkBoxRename)) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				if (!this.checkBoxOrganize.isSelected()) {
					this.checkBoxOrganize.setSelected(true);
				}
			}
		}
	}

	public void setCheckBoxIndexExistingFilename(JCheckBox checkBoxIndex) {
		this.checkBoxIndexExistingFilename = checkBoxIndex;
	}

	public void setCheckBoxKeepOriginalDirectoryStructure(
			JCheckBox checkBoxKeepOriginalDirectoryStructure) {
		this.checkBoxKeepOriginalDirectoryStructure = checkBoxKeepOriginalDirectoryStructure;
	}

	public void setCheckBoxKeepOriginalFile(JCheckBox checkBoxKeepOriginalFile) {
		this.checkBoxKeepOriginalFile = checkBoxKeepOriginalFile;
	}

	public void setCheckBoxOrganize(JCheckBox checkBoxOrganize) {
		this.checkBoxOrganize = checkBoxOrganize;
	}

	public void setCheckBoxOverwriteExistingFile(
			JCheckBox checkBoxOverwriteExistingFile) {
		this.checkBoxOverwriteExistingFile = checkBoxOverwriteExistingFile;
	}

	public void setCheckBoxRecursive(JCheckBox checkBoxRecursive) {
		this.checkBoxRecursive = checkBoxRecursive;
	}

	public void setCheckBoxRename(JCheckBox checkBoxRename) {
		this.checkBoxRename = checkBoxRename;
	}

	public void setCheckBoxTreatImageFiles(JCheckBox checkBoxTreatImageFiles) {
		this.checkBoxTreatImageFiles = checkBoxTreatImageFiles;
	}

	public void setCheckBoxTreatVideoFiles(JCheckBox checkBoxTreatVideoFiles) {
		this.checkBoxTreatVideoFiles = checkBoxTreatVideoFiles;
	}

	public void setCheckBoxUseSpecificDestDirectory(
			JCheckBox checkBoxUseSpecificDestDirectory) {
		this.checkBoxUseSpecificDestDirectory = checkBoxUseSpecificDestDirectory;
	}

	public void setTextFieldSpecificDirectory(
			JTextField textFieldSpecificDirectory) {
		this.textFieldSpecificDirectory = textFieldSpecificDirectory;
	}
}