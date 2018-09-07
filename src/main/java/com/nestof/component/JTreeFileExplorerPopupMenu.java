/**
 * 
 */
package com.nestof.component;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.nestof.action.PictureOrganizerLaunchAction;

/**
 * @author csailly
 * @creationDate 7 juil. 2011
 * @Description
 */
public class JTreeFileExplorerPopupMenu extends JPopupMenu {

	public JTreeFileExplorerPopupMenu() {
		super();
		build();
	}

	public JTreeFileExplorerPopupMenu(String label) {
		super(label);
		build();
	}

	private void build() {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setAction(new PictureOrganizerLaunchAction(
				"Organiser le dossier"));
		add(menuItem);
	}
}
