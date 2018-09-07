package com.nestof.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.nestof.ihm.plugin.PictureOrganizerSettingsWindow;
import com.nestof.plugin.PictureOrganizer;

public class PictureOrganizerDisplaySettingsWindowAction extends AbstractAction {

	public PictureOrganizerDisplaySettingsWindowAction(String texte) {
		super(texte);
	}

	public void actionPerformed(ActionEvent e) {
		PictureOrganizer pictureOrganizer = PictureOrganizer.getInstance();
		PictureOrganizerSettingsWindow pictureOrganizerSettingsWindow = pictureOrganizer
				.getPictureOrganizerSettingsWindow();

		if (pictureOrganizerSettingsWindow == null) {
			pictureOrganizerSettingsWindow = new PictureOrganizerSettingsWindow(
					pictureOrganizer);
		}

		pictureOrganizerSettingsWindow.initialize();
		pictureOrganizerSettingsWindow.setVisible(true);
	}
}
