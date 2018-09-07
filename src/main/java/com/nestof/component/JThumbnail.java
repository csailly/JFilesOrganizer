package com.nestof.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.nio.file.Path;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class JThumbnail extends JPanel {

	static Logger logger = Logger.getLogger(JThumbnail.class);

	private Image image;

	private final Path file;

	private final int width;

	private final int height;

	/**
	 * 
	 * @param file
	 *            :The file to load
	 */
	public JThumbnail(Path file) {
		this.file = file;
		this.width = 200;
		this.height = 200;
		this.setPreferredSize(new Dimension(this.width, this.height));
		this.setMinimumSize(new Dimension(this.width, this.height));
	}

	/**
	 * 
	 * @param file
	 *            : The file to load
	 * @param width
	 *            : Prefered width
	 * @param height
	 *            : Prefered height
	 */
	public JThumbnail(Path file, int width, int height) {
		this.file = file;
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(this.width, this.height));
		this.setMinimumSize(new Dimension(this.width, this.height));
	}

	public Path getFile() {
		return this.file;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.image == null) {
			return;
		}

		int originalWidth = this.image.getWidth(this);// Largeur de l'image
		// d'origine
		int originalHeight = this.image.getHeight(this);// Hauteur de l'image
		// d'origine

		int newWidth;// Largeur de la vignette
		int newHeight;// Hauteur de la vignette

		int posX = 0;// Position x de la vignette
		int posY = 0;// Position y de la vignette

		// Calcul des dimensions de la vignette
		if ((originalHeight <= this.getHeight())
				&& (originalWidth <= this.getWidth())) {
			// Cas 1 : Image plus petite que le container
			newWidth = originalWidth;
			newHeight = originalHeight;
		} else if (originalHeight > originalWidth) {
			// Cas 2 : Image plus grande que le container et plus haute que
			// large
			newWidth = (this.getHeight() * originalWidth) / originalHeight;
			newHeight = this.getHeight();
		} else {
			// Cas 3 : Image plus grande que le container et plus large que
			// haute
			newWidth = this.getWidth();
			newHeight = (this.getWidth() * originalHeight) / originalWidth;
		}

		// Calcul des positions de la vignette
		posX = (this.getWidth() - newWidth) / 2;
		posY = (this.getHeight() - newHeight) / 2;

		// Dessin de la vignette
		g.drawImage(this.image, posX, posY, newWidth, newHeight, this);
	}

	// /* (non-Javadoc)
	// * @see
	// java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	// */
	// public void componentShown(ComponentEvent e) {
	// if (MainWindow.getInstance().getThumbnailsCache()
	// .containsKey(file.getAbsolutePath())) {
	// image = MainWindow.getInstance().getThumbnailsCache()
	// .get(file.getAbsolutePath());
	// } else {
	// try {
	// this.image = new Thumbnail().getThumbnail(
	// this.file.getAbsolutePath(), width, height);
	// MainWindow.getInstance().getThumbnailsCache()
	// .put(file.getAbsolutePath(), image);
	// } catch (Exception ex) {
	// // TODO Auto-generated catch block
	// ex.printStackTrace();
	// }
	// }
	//
	// this.repaint();
	//
	// }

}