/**
 * 
 */
package com.nestof.plugin;

import javax.swing.SwingUtilities;

import com.nestof.ihm.MainWindow;

/**
 * @author csailly
 * @creationDate 18 août 2011
 * @Description
 */
public abstract class Plugin {

	private boolean paused;

	/**
	 * Met en pause le traitement si demandé.
	 */
	protected synchronized void checkPause() {
		if (this.paused) {
			this.sleep();
		}
	}

	/**
	 * Update the progress bar value.
	 * 
	 * @param value
	 *            : the new value
	 */
	protected void updateProgressBar(final int value) {
		Runnable runnable = new Runnable() {
			public void run() {
				MainWindow.getInstance().getjProgressBar().setValue(value);
			}
		};
		SwingUtilities.invokeLater(runnable);
	}

	/**
	 * Update the status bar text.
	 * 
	 * @param text
	 *            : the text to display into the status bar.
	 */
	protected void updateStatusBar(final String text) {
		Runnable runnable = new Runnable() {
			public void run() {
				MainWindow.getInstance().getStatusBarText().setText(text);
			}
		};
		SwingUtilities.invokeLater(runnable);
	}

	/**
	 * Raz the progress bar.
	 */
	protected void razProgressBar() {
		Runnable runnable = new Runnable() {
			public void run() {
				MainWindow.getInstance().getjProgressBar().setValue(0);
			}
		};
		SwingUtilities.invokeLater(runnable);
	}

	/**
	 * Init the progress bar.
	 * 
	 * @param maxSize
	 */
	protected void initProgressBar(final int maxSize) {
		Runnable runnable = new Runnable() {
			public void run() {
				MainWindow.getInstance().getjProgressBar().setMaximum(maxSize);
				System.out.println("init " + maxSize);
			}
		};
		SwingUtilities.invokeLater(runnable);
	}

	/**
	 * Renvoie le nom du plugin.
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * Renvoie un rapport du traitement;
	 * 
	 * @return
	 */
	public abstract String getReport();

	/**
	 * Is the plugin job paused ?
	 * 
	 * @return
	 */
	public boolean isPaused() {
		return this.paused;
	}

	/**
	 * Lance le traitement;
	 */
	public abstract void launch();

	/**
	 * Charge les options de configurations depuis le fichier.
	 */
	public abstract void loadSettings();

	/**
	 * Demande une pause du traitement.
	 */
	public synchronized void pause() {
		this.paused = true;
	}

	/**
	 * Demande la reprise du traitement.
	 */
	public void resume() {
		synchronized (this) {
			this.paused = false;
			this.notify();
		}
	}

	/**
	 * Sauve les options de configurations dans le fichier.
	 */
	public abstract void saveSettings();

	/**
	 * Met en pause le traitement.
	 */
	protected void sleep() {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
