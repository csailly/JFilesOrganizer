package com.nestof.component;

import java.awt.Graphics;
import java.io.File;

import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.nestof.model.JTreeFileModel;
import com.nestof.renderer.JTreeFileRenderer;

public class JTreeFileExplorer extends JTree {

	private File root;

	private TreeModel modele;

	public JTreeFileExplorer() {

		FileSystemView vueSysteme = FileSystemView.getFileSystemView();
		File home = vueSysteme.getHomeDirectory();

		this.root = home;
		modele = new JTreeFileModel(this.root);
		setModel(modele);

		setCellRenderer(new JTreeFileRenderer());

		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void updateUI() {
		modele = new JTreeFileModel(this.root);
		setModel(modele);
		super.updateUI();
	}

}