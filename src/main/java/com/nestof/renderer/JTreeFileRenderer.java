package com.nestof.renderer;

import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

public class JTreeFileRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -6927478378946536244L;

	public JTreeFileRenderer() {
		super();
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		try {
			JLabel label = (JLabel) super.getTreeCellRendererComponent(tree,
					value, selected, expanded, leaf, row, hasFocus);
			File fichier = (File) value;
			if (fichier.exists()) {

				FileSystemView sys = FileSystemView.getFileSystemView();
				label.setText(sys.getSystemDisplayName(fichier));
				label.setIcon(sys.getSystemIcon(fichier));
				return label;
			} else {
				// TODO Voir pour supprimer le noeud ouy la feuille
				TreePath treePath = new TreePath((File) value);
				return new JLabel("!!! DELETED");
			}
		} catch (Exception e) {
			return new JLabel("!!! ERROR");
		}
	}
}