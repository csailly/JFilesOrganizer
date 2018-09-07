package com.nestof.model;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class JTreeFileModel implements TreeModel {
	private File root;

	public JTreeFileModel(File file) {
		root = file;
	}

	public void addTreeModelListener(TreeModelListener l) {
	}

	public Object getChild(Object parent, int index) {
		return getFichiers(parent).get(index);
	}

	public int getChildCount(Object parent) {
		return getFichiers(parent).size();
	}

	public List getFichiers(Object parent) {
		File fileParent = (File) parent;
		File[] fichiers = fileParent.listFiles();

		Arrays.sort(fichiers, new Comparator<File>() {
			public int compare(File f1, File f2) {
				boolean dirf1 = f1.isDirectory();
				boolean dirf2 = f2.isDirectory();
				if (dirf1 && !dirf2) {
					return -1;
				}
				if (!dirf1 && dirf2) {
					return 1;
				}
				return f1.getPath().compareToIgnoreCase(f2.getPath());
			}
		});
		return Arrays.asList(fichiers);
	}

	public int getIndexOfChild(Object parent, Object child) {
		return getFichiers(parent).indexOf(child);
	}

	public Object getRoot() {
		return root;
	}

	public boolean isLeaf(Object node) {
		File f = (File) node;
		boolean isLeaf = !f.isDirectory() || f.listFiles() == null;
		return isLeaf;
	}

	public void removeTreeModelListener(TreeModelListener l) {
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
	}
}