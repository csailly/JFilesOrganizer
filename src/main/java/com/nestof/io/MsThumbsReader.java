package com.nestof.io;

/**
 * This software is free. Use at your own risk.
 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.poifs.property.DocumentProperty;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGDecodeParam;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

public class MsThumbsReader {
	POIFSFileSystem fs;

	int counter = 0;

	String outputDir = ".";

	boolean DEBUG = false;

	public MsThumbsReader(final InputStream stream, final String outDir)
			throws IOException {
		this.fs = new POIFSFileSystem(stream);
		this.outputDir = outDir;
	}

	public String space(final int n) {
		final StringBuffer s = new StringBuffer();
		for (int i = 0; i < n; i++) {
			s.append(' ');
		}
		return s.toString();
	}

	public void handleProperty(final DocumentProperty prop) {
		System.out.println("name=" + prop.getName());
		System.out.println("desc=" + prop.getShortDescription());
		System.out.println("startblock=" + prop.getStartBlock());
		System.out.println("storageclass=" + prop.getStorageClsid());
	}

	public void showDir(final int indent, final DirectoryNode d) {
		if (this.DEBUG) {
			System.out.println(d.getName());
		}

		final ArrayList<CatalogItem> CatalogItems = new ArrayList<CatalogItem>();

		DocumentInputStream is = null;
		try {
			is = this.fs.createDocumentInputStream("Catalog");

			final short nNum1 = is.readShort();
			final short nNum2 = is.readShort();
			final int nThumbCount = is.readInt();
			final int nThumbWidth = is.readInt();
			final int nThumbHeight = is.readInt();

			for (int nIndex = 0; nIndex < nThumbCount; nIndex++) {
				final CatalogItem item = new CatalogItem();
				item.nItemSize = is.readInt();
				item.nItemID = is.readInt();
				item.nNum3 = is.readShort();
				item.nNum4 = is.readShort();
				item.nNum5 = is.readShort();
				item.nNum6 = is.readShort();
				int usChar;
				while ((usChar = is.readUShort()) != 0x0000) {
					final byte[] byChar = new byte[2];
					byChar[0] = (byte) (usChar & 0x00FF);
					;
					byChar[1] = (byte) ((usChar & 0xFF00) >> 8);
					item.strFileName += byChar[1] == 0 ? (char) byChar[0]
							: new String(byChar);
				}
				item.nNum7 = is.readShort();
				CatalogItems.add(item);
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}

		for (final CatalogItem item : CatalogItems) {
			try {
				final String correctFileName = this
						.buildReverseString(item.nItemID);

				is = this.fs.createDocumentInputStream(correctFileName);
				final int header_len = is.read();
				for (int i = 1; i < header_len; i++) {
					is.read();
				}

				final JPEGImageDecoder decoder = JPEGCodec
						.createJPEGDecoder(is);
				final JPEGDecodeParam param = JPEGCodec
						.getDefaultJPEGEncodeParam(4,
								JPEGDecodeParam.COLOR_ID_RGBA);
				decoder.setJPEGDecodeParam(param);
				final BufferedImage originalBufferedImage = decoder
						.decodeAsBufferedImage();

				ImageIO.write(originalBufferedImage, "jpg",
						new File(this.outputDir + File.separator
								+ item.strFileName.replaceAll(".pdf", ".jpg")));

				this.counter++;

			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String buildReverseString(final int nItemID) {
		final String strItem = Integer.toString(nItemID);
		String strReverse = "";
		for (int nIndex = strItem.length() - 1; nIndex >= 0; nIndex--) {
			strReverse += strItem.charAt(nIndex);
		}

		return strReverse;
	}

	public void showFS(final POIFSFileSystem fs) {
		final DirectoryNode d = fs.getRoot(); // TODO: get rid of cast
		this.showDir(0, d);
	}

	public void makeJPG() throws IOException {
		this.showFS(this.fs);
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		String thumbsFile = "d:\\Thumbs.db";
		String outDir = "d:\\";

		if (args.length > 0) {
			thumbsFile = args[0];
		}
		if (args.length > 1) {
			outDir = args[1];
		}
		try {
			final InputStream stream = new FileInputStream(thumbsFile);
			final MsThumbsReader r = new MsThumbsReader(stream, outDir);
			r.makeJPG();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

}

class CatalogItem {
	public int nItemSize;
	public int nItemID;
	public short nNum3;
	public short nNum4;
	public short nNum5;
	public short nNum6;
	public String strFileName = "";
	public short nNum7;

	@Override
	public String toString() {
		return this.nItemID + " - " + this.strFileName;
	}
}
