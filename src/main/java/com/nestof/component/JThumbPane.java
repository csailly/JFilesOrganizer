package com.nestof.component;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.nio.file.Path;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class JThumbPane extends JPanel {

	static Logger logger = Logger.getLogger(JThumbPane.class);

	private final JThumbnail image;

	private final JLabel label;

	private final JCheckBox checkBox;

	public JThumbPane(Path file, int width, int height) {
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

		this.image = new JThumbnail(file, width, height);
		this.checkBox = new JCheckBox();
		this.label = new JLabel(file.getFileName().toString());

		this.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		/* Ajout de l'image */
		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(3, 3, 1, 3);

		this.add(this.image, gbc);

		/* Ajout de la checkBox */
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;

		this.add(this.checkBox, gbc);

		/* Ajout du label */
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		this.add(this.label, gbc);
	}

	public JThumbnail getImage() {
		return this.image;
	}

}