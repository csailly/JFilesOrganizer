package com.nestof.component;

import java.awt.Dimension;

import javax.swing.JLabel;

public class JStatusBar extends JLabel {

	/** Creates a new instance of StatusBar */
	public JStatusBar() {
		super();
		super.setPreferredSize(new Dimension(100, 16));
		setMessage("Ready");
	}

	public void setMessage(String message) {
		setText(" " + message);
	}
}