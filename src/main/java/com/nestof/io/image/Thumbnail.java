package com.nestof.io.image;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;

public class Thumbnail {

	public void createThumbnail(String imgFilePath, String thumbPath,
			int thumbWidth, int thumbHeight) throws Exception {

		BufferedImage thumbImage = (BufferedImage) this.getThumbnail(
				imgFilePath, thumbWidth, thumbHeight);

		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(thumbPath));
		// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		// JPEGEncodeParam param =
		// encoder.getDefaultJPEGEncodeParam(thumbImage);
		// int quality = 100;
		// param.setQuality(quality / 100.0f, false);
		// encoder.setJPEGEncodeParam(param);
		// encoder.encode(thumbImage);
		out.close();
	}

	/**
	 * 
	 * @param imgFilePath
	 *            : The path to access to the image
	 * @param thumbWidth
	 *            : The max width of the thumbnail.
	 * @param thumbHeight
	 *            : The max height of the thumbnail.
	 * @return a thumbnail of the image in a Image object.
	 * @throws Exception
	 */
	public Image getThumbnail(String imgFilePath, int thumbWidth,
			int thumbHeight) throws Exception {

		// Récupération du thumbnail embarqué
		IImageMetadata metadata = Sanselan.getMetadata(new File(imgFilePath));
		if (metadata instanceof JpegImageMetadata) {
			JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			BufferedImage exifThumbnail = jpegMetadata.getEXIFThumbnail();
			if (exifThumbnail != null) {
				return exifThumbnail;
			}
		}

		// Construction du thumbnail s'il n'est pas dans le fichier
		Image image = Toolkit.getDefaultToolkit().getImage(imgFilePath);
		MediaTracker mediaTracker = new MediaTracker(new Container());
		mediaTracker.addImage(image, 0);
		mediaTracker.waitForID(0);

		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;
		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		} else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}
		BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

		return thumbImage;
	}
}