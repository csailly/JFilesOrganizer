/**
 * 
 */
package com.nestof.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.tree.TreePath;

import com.nestof.component.JTreeFileExplorer;
import com.nestof.component.JTreeFileExplorerPopupMenu;
import com.nestof.plugin.PictureOrganizer;

/**
 * @author csailly
 * @creationDate 7 juil. 2011
 * @Description
 */
public class CustomTreeMouseListener implements MouseListener {

	/**
	 * Contruit et affiche le menu contextuel à l'endroit du clic droit.
	 * 
	 * @param e
	 */
	private void displayContextMenu(MouseEvent e) {
		// Récupération du noeud de l'arbre où se fait le clic droit
		JTreeFileExplorer treeFileExplorer = (JTreeFileExplorer) e
				.getComponent();
		// Sélection du noeud de l'arbre où se fait le clic droit
		TreePath treePath = treeFileExplorer.getPathForLocation(e.getX(),
				e.getY());
		if ((treePath != null)
				&& ((File) treePath.getLastPathComponent()).isDirectory()) {
			treeFileExplorer.setSelectionPath(treePath);
			// Mise à jour du dossier source du plugin PictureOrganizer
			PictureOrganizer.getInstance().setInitialSourceDirectory(
					((File) treePath.getLastPathComponent()).toPath());
			// Affichage du menu contextuel
			JTreeFileExplorerPopupMenu menu = new JTreeFileExplorerPopupMenu();
			menu.show(e.getComponent(), e.getX(), e.getY());
		} else {
			PictureOrganizer.getInstance().setInitialSourceDirectory(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			this.displayContextMenu(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			this.displayContextMenu(e);
		}
	}

}
