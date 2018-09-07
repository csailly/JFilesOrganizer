package com.nestof.action;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;

import com.nestof.ihm.MainWindow;
import com.nestof.plugin.PictureOrganizer;

public class PictureOrganizerUndoOrganizeAction extends AbstractAction {

	public PictureOrganizerUndoOrganizeAction(String texte) {
		super(texte);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			PictureOrganizer.getInstance().undoJob();
			MainWindow.getInstance().getTree().updateUI();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
