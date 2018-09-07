package com.nestof.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreePath;

import com.nestof.ihm.MainWindow;
import com.nestof.plugin.PictureOrganizer;

public class PictureOrganizerLaunchAction extends AbstractAction {

	private static final long serialVersionUID = -3134336268299929679L;

	public PictureOrganizerLaunchAction(String texte) {
		super(texte);
	}

	public void actionPerformed(ActionEvent e) {
		PictureOrganizer pictureOrganizer = PictureOrganizer.getInstance();
		MainWindow.getInstance().setCurrentPlugin(pictureOrganizer);

		if (pictureOrganizer.getInitialSourceDirectory() == null) {
			FileSystemView vueSysteme = FileSystemView.getFileSystemView();
			File home = vueSysteme.getHomeDirectory();

			// création et affichage des JFileChooser
			JFileChooser homeChooser = new JFileChooser(home);
			homeChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			homeChooser.setDialogTitle("Choose directory to treat...");
			int returnVal = homeChooser.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				pictureOrganizer.setInitialSourceDirectory(homeChooser
						.getSelectedFile().toPath());
			}
		}

		if (pictureOrganizer.getInitialSourceDirectory() != null) {
			Thread pluginThread = new Thread() {
				@Override
				public void run() {
					PictureOrganizer pictureOrganizer = PictureOrganizer
							.getInstance();
					pictureOrganizer.launch();
					// Mise à jour IHM
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JOptionPane.showMessageDialog(MainWindow
									.getInstance(), PictureOrganizer
									.getInstance().getReport());
							TreePath currentTreePath = MainWindow.getInstance()
									.getTree().getSelectionPath();
							MainWindow.getInstance().getTree().updateUI();
							MainWindow.getInstance().getTree()
									.setSelectionPath(currentTreePath);
						}
					});
				}
			};
			MainWindow.getInstance().setPluginThread(pluginThread);
			MainWindow.getInstance().getPluginThread().start();
		}
	}

}
