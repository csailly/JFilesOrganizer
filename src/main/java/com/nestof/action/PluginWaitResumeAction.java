package com.nestof.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.nestof.ihm.MainWindow;

public class PluginWaitResumeAction extends AbstractAction {

	public PluginWaitResumeAction(String texte) {
		super(texte);
	}

	public void actionPerformed(ActionEvent e) {

		if (MainWindow.getInstance().getCurrentPlugin() != null) {

			if (!MainWindow.getInstance().getCurrentPlugin().isPaused()) {
				MainWindow.getInstance().getCurrentPlugin().pause();
			} else {
				MainWindow.getInstance().getCurrentPlugin().resume();
			}
		}
	}
}
