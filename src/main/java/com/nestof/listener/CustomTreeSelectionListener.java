package com.nestof.listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.nestof.component.JThumbPane;
import com.nestof.ihm.MainWindow;
import com.nestof.io.PathUtils;

public class CustomTreeSelectionListener implements TreeSelectionListener {

	public CustomTreeSelectionListener() {

	}

	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getPath();
		Path file = ((File) path.getLastPathComponent()).toPath();

		if (Files.isRegularFile(file)) {

			String s = "Can read : \n   " + Files.isReadable(file) + "\n";
			s += "Can write : \n   " + Files.isWritable(file) + "\n";
			s += "Parent : \n   " + file.getParent() + "\n";
			s += "Name : \n   " + file.getFileName() + "\n";
			try {
				s += "Length : \n   " + Files.size(file) + "\n";
				s += "Last modified : \n   "
						+ new Date(Files.getLastModifiedTime(file).toMillis())
						+ "\n";
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			try {
				Calendar calendar = PathUtils.getExifDateTimeOriginal(file);
				if (calendar != null) {
					s += "Exif dateTimeOriginal : \n     " + calendar.getTime()
							+ "\n";
					MainWindow.getInstance().getjPictureTab()
							.add(new JThumbPane(file, 150, 150));
				}
				calendar = PathUtils.getExifDateTimeDigitized(file);
				if (calendar != null) {
					s += "Exif dateTimeDigitized : \n     "
							+ calendar.getTime() + "\n";
					MainWindow.getInstance().getjPictureTab()
							.add(new JThumbPane(file, 150, 150));
				}
			} catch (ImageProcessingException e1) {
				e1.printStackTrace();
			} catch (MetadataException e1) {
				e1.printStackTrace();
			}

			MainWindow.getInstance().getInfos().setText(s);
		} else if (Files.isDirectory(file)) {
			MainWindow.getInstance().getjPictureTab().removeAll();
			try {
				DirectoryStream<Path> stream = Files.newDirectoryStream(file);
				try {
					Iterator<Path> iterator = stream.iterator();
					while (iterator.hasNext()) {
						Path child = iterator.next();
						if (Files.isRegularFile(child)) {
							try {
								Calendar calendar = PathUtils
										.getExifDateTimeOriginal(child);
								if (calendar != null) {
									// MainWindow.getInstance().getjPictureTab()
									// .add(new JThumbPane(child, 150, 150));
								}
							} catch (ImageProcessingException e1) {
								e1.printStackTrace();
							} catch (MetadataException e1) {
								e1.printStackTrace();
							}
						}
					}
				} finally {
					stream.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}