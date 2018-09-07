/**
 * 
 */
package com.nestof.component;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * @author csailly
 * @creationDate 4 juil. 2011
 * @Description
 */
public class JPictureTab extends JPanel {

	private final int width;

	private final List<String> picuresList;

	public JPictureTab(int width) {
		this.width = width;
		this.picuresList = new ArrayList<String>();
		this.setLayout(new GridBagLayout());
	}

	@Override
	public Component add(Component component) {
		int posX = this.picuresList.size() % this.width;
		int posY = this.picuresList.size() / this.width;

		if (this.picuresList.contains(((JThumbPane) component).getImage()
				.getFile().toAbsolutePath().toString())) {
			return null;
		} else {
			this.picuresList.add(((JThumbPane) component).getImage().getFile()
					.toAbsolutePath().toString());
		}

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = posX;
		gbc.gridy = posY;
		gbc.weightx = 1;
		gbc.weighty = 1;

		super.add(component, gbc);
		this.getParent().paintAll(this.getParent().getGraphics());

		// System.out.println(getVisibleRect().contains(new
		// Rectangle((int)component.getBounds().getX(),
		// (int)component.getBounds().getY(), 1, 1)));

		return component;
	}

	@Override
	public void removeAll() {
		super.removeAll();
		this.picuresList.clear();
		this.getParent().paintAll(this.getParent().getGraphics());
	}

}
