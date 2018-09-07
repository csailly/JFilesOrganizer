package com.nestof.action;

import java.awt.event.ActionEvent;
import java.nio.file.Paths;

import javax.swing.AbstractAction;

import org.apache.commons.lang.StringUtils;

import com.nestof.ihm.plugin.PictureOrganizerSettingsWindow;
import com.nestof.plugin.PictureOrganizer;

public class PictureOrganizerSaveSettingsAction extends AbstractAction {

	private static final long serialVersionUID = -8445634593294857930L;

	/**
	 * 
	 * @param texte
	 *            : le texte affiché dans le composant auquel l'action est
	 *            associée
	 * @param pictureOrganizer
	 *            : l'instance de pictureOrganizer
	 */
	public PictureOrganizerSaveSettingsAction(String texte) {
		super(texte);
	}

	public void actionPerformed(ActionEvent e) {
		PictureOrganizer pictureOrganizer = PictureOrganizer.getInstance();
		PictureOrganizerSettingsWindow pictureOrganizerSettingsWindow = pictureOrganizer
				.getPictureOrganizerSettingsWindow();

		pictureOrganizer.setOptKeepOriginalFile(pictureOrganizerSettingsWindow
				.getCheckBoxKeepOriginalFile().isSelected());
		pictureOrganizer
				.setOptOverwriteExistingFile(pictureOrganizerSettingsWindow
						.getCheckBoxOverwriteExistingFile().isSelected());
		pictureOrganizer.setOptRecursive(pictureOrganizerSettingsWindow
				.getCheckBoxRecursive().isSelected());
		pictureOrganizer
				.setOptKeepOriginalDirectoryStructure(pictureOrganizerSettingsWindow
						.getCheckBoxKeepOriginalDirectoryStructure()
						.isSelected());

		pictureOrganizer.setOptRename(pictureOrganizerSettingsWindow
				.getCheckBoxRename().isSelected());
		pictureOrganizer
				.setIndexExistingFilename(pictureOrganizerSettingsWindow
						.getCheckBoxIndexExistingFilename().isSelected());
		pictureOrganizer.setOptOrganize(pictureOrganizerSettingsWindow
				.getCheckBoxOrganize().isSelected());

		pictureOrganizer
				.setOptUseSpecificDestDirectory(pictureOrganizerSettingsWindow
						.getCheckBoxUseSpecificDestDirectory().isSelected());
		if (pictureOrganizerSettingsWindow
				.getCheckBoxUseSpecificDestDirectory().isSelected()
				&& StringUtils.isNotBlank(pictureOrganizerSettingsWindow
						.getTextFieldSpecificDirectory().getText())) {
			pictureOrganizer.setSpecificDestDirectory(Paths
					.get(pictureOrganizer.getPictureOrganizerSettingsWindow()
							.getTextFieldSpecificDirectory().getText()));
		}

		pictureOrganizer.setOptTreatImageFiles(pictureOrganizerSettingsWindow
				.getCheckBoxTreatImageFiles().isSelected());
		pictureOrganizer.setOptTreatVideoFiles(pictureOrganizerSettingsWindow
				.getCheckBoxTreatVideoFiles().isSelected());

		pictureOrganizer.saveSettings();
		pictureOrganizerSettingsWindow.dispose();
	}
}
