/**
 * 
 */
package com.nestof.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;

/**
 * 
 * @author nestof
 * 
 */
public class PathUtils {
	static Logger logger = Logger.getLogger(PathUtils.class);

	/**
	 * Check if the file is an image file.
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isImage(Path file) {

		String mimeType = null;
		try {
			mimeType = Files.probeContentType(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (StringUtils.startsWith(mimeType, "image/"));
	}

	/**
	 * Check if the file is a video file.
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isVideo(Path file) {
		String mimeType = null;
		try {
			mimeType = Files.probeContentType(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (StringUtils.startsWith(mimeType, "video/"));
	}

	public static Calendar getLastModified(Path file) {
		FileTime fileTime = null;
		try {
			fileTime = Files.getLastModifiedTime(file,
					LinkOption.NOFOLLOW_LINKS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fileTime != null) {

			Calendar date = Calendar.getInstance();
			date.setTimeInMillis(fileTime.toMillis());
			return date;
		} else {
			return Calendar.getInstance();
		}
	}

	/**
	 * Renvoie la date de prise de vue.
	 * 
	 * @param file
	 * @return
	 * @throws ImageProcessingException
	 * @throws MetadataException
	 */
	public static Calendar getExifDateTimeOriginal(Path file)
			throws ImageProcessingException, MetadataException {
		if (!Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS)) {
			return null;
		}

		// Vérification du type mime
		if (!PathUtils.isImage(file)) {
			String type = null;
			try {
				type = Files.probeContentType(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info(" Fichier ignoré, MimeType non pris en charge :  "
					+ type);
			return null;
		}

		// Lecture des metadata du fichier
		Metadata metadata = ImageMetadataReader.readMetadata(file.toFile());

		// obtain the Exif directory
		Directory directory = metadata.getDirectory(ExifDirectory.class);

		if (!directory.containsTag(ExifDirectory.TAG_DATETIME_ORIGINAL)) {
			logger.info(" Fichier ignoré, Tag non présent dans Metadata :  "
					+ ExifDirectory.TAG_DATETIME_ORIGINAL);
			return null;
		}

		// query the tag's value
		Calendar calendar = Calendar.getInstance();

		try {
			Date date = directory.getDate(ExifDirectory.TAG_DATETIME_ORIGINAL);
			calendar.setTime(date);
		} catch (MetadataException e) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
			try {
				Date date = dateFormat.parse(directory
						.getString(ExifDirectory.TAG_DATETIME_ORIGINAL));
				calendar.setTime(date);
			} catch (ParseException e1) {
				throw e;
			}
		}

		return calendar;
	}

	/**
	 * Renvoie la date de traitement.
	 * 
	 * @param file
	 * @return
	 * @throws ImageProcessingException
	 * @throws MetadataException
	 */
	public static Calendar getExifDateTimeDigitized(Path file)
			throws ImageProcessingException, MetadataException {
		if (!Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS)) {
			return null;
		}

		// Vérification du type mime
		if (!PathUtils.isImage(file)) {
			String type = null;
			try {
				type = Files.probeContentType(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info(" Fichier ignoré, MimeType non pris en charge :  "
					+ type);
			return null;
		}

		// Lecture des metadata du fichier
		Metadata metadata = ImageMetadataReader.readMetadata(file.toFile());

		// obtain the Exif directory
		Directory directory = metadata.getDirectory(ExifDirectory.class);

		if (!directory.containsTag(ExifDirectory.TAG_DATETIME_DIGITIZED)) {
			logger.info(" Fichier ignoré, Tag non présent dans Metadata :  "
					+ ExifDirectory.TAG_DATETIME_ORIGINAL);
			return null;
		}

		// query the tag's value
		Calendar calendar = Calendar.getInstance();

		try {
			Date date = directory.getDate(ExifDirectory.TAG_DATETIME_DIGITIZED);
			calendar.setTime(date);
		} catch (MetadataException e) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
			try {
				Date date = dateFormat.parse(directory
						.getString(ExifDirectory.TAG_DATETIME_DIGITIZED));
				calendar.setTime(date);
			} catch (ParseException e1) {
				throw e;
			}
		}

		return calendar;
	}

	public static boolean fileAreSame(Path file1, Path file2) {
		try {
			return Files.isSameFile(file1, file2);// TODO tester longueur
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println(FilenameUtils.getExtension("test.JPS"));

	}

}
