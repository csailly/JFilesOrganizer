package com.nestof.plugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.nestof.action.PictureOrganizerDisplaySettingsWindowAction;
import com.nestof.action.PictureOrganizerLaunchAction;
import com.nestof.ihm.MainWindow;
import com.nestof.ihm.plugin.PictureOrganizerSettingsWindow;
import com.nestof.io.PathUtils;
import com.nestof.io.xml.Xml;
import com.nestof.utils.Constants;

/**
 * @author csailly
 * @creationDate 28 juin 2011
 * @Description
 */
public class PictureOrganizer extends Plugin {

	private static PictureOrganizer instance;

	static Logger logger = Logger.getLogger(PictureOrganizer.class);
	private static final String SETTINGS_INDEX_NAME = "index";
	private static final String SETTINGS_KEEPORIGINAL_DATA_NAME = "keepOriginalFile";
	private static final String SETTINGS_KEEPORIGINALSTRUCTURE_DATA_NAME = "keepOriginalDirectoryStructure";
	private static final String SETTINGS_ORGANIZE_NAME = "organize";
	private static final String SETTINGS_OVERWRITE_DATA_NAME = "overwriteExistingFile";
	private static final String SETTINGS_RECURSIVE_DATA_NAME = "recursive";
	private static final String SETTINGS_RENAMEONLY_NAME = "renameOnly";
	private static final String SETTINGS_SPECIFICDESTDIRECTORY_DATA_NAME = "specificDestDirectory";
	private static final String SETTINGS_TREAT_IMAGE_FILES_NAME = "treatImageFiles";
	private static final String SETTINGS_TREAT_VIDEO_FILES_NAME = "treatVideoFiles";

	private static final String SETTINGS_USESPECIFICDESTDIRECTORY_DATA_NAME = "useSpecificDestDirectory";

	public static PictureOrganizer getInstance() {
		if (instance == null) {
			instance = new PictureOrganizer();
		}
		return instance;
	}

	public static void main(String[] args) throws ImageProcessingException,
			MetadataException, IOException {
		// PropertyConfigurator.configure(Exif.class.getClassLoader().getResource("log4j.properties"));

	}

	// Compteur sur les dossiers analysés
	private int cptDossiersAnalyses;
	// Compteur sur les erreurs
	private int cptErreurs;
	// Compteur sur les fichiers analysés
	private int cptFichiersAnalyses;
	// Compteur sur les fichiers copiés
	private int cptFichiersCopies;
	// Compteur sur les fichiers déplacés
	private int cptFichiersDeplaces;
	// Compteur sur les fichiers ignorés
	private int cptFichiersIgnores;
	// Permet de journaliser La liste des tâches effectuées
	private Job currentJob;
	// Dossier source initial;
	private Path initialSourceDirectory;
	// Xml permettant de sauvegarder les différentes modifications apportées
	private Xml jobHistory;
	// Liste des jobs
	private List<Job> jobs;
	// Index les noms en cas de doublons
	private boolean optIndexExistingFilename;
	// Conserve l'arborescence initiale
	private boolean optKeepOriginalDirectoryStructure;
	// Conserve les fichiers originaux
	private boolean optKeepOriginalFile;
	// Classe les fichiers
	private boolean optOrganize;
	// Ecrase les fichiers existants
	private boolean optOverwriteExistingFile;
	// Traite les sous dossiers
	private boolean optRecursive;
	// Renommage des fichiers.
	private boolean optRename;
	// N'effectue pas les actions.
	private final boolean optSimulation = false;
	// Prise en compte des fichiers image
	private boolean optTreatImageFiles;
	// Prise en compte des fichiers videos
	private boolean optTreatVideoFiles;

	// Utilise un dossier destinnation spécifique
	private boolean optUseSpecificDestDirectory;

	// Fenêtre de configuration du plugin;
	private PictureOrganizerSettingsWindow pictureOrganizerSettingsWindow;

	// Dossier spécifique dans lequel seront copiés/déplacés les fichiers
	// traités.
	private Path specificDestDirectory;

	private PictureOrganizer() {
		this.loadSettings();
		this.pictureOrganizerSettingsWindow = new PictureOrganizerSettingsWindow(
				this);
		this.buildUI();
		this.jobs = new ArrayList<Job>();
	}

	/**
	 * Analyse a directory.
	 * 
	 * @param sourceDirectory
	 *            : the directory to analyse.
	 * @throws IOException
	 */
	private void analyseDirectory(Path sourceDirectory) throws IOException {
		// Test l'existance et la lecture du dossier source
		if (!Files.exists(sourceDirectory)
				|| !Files.isReadable(sourceDirectory)) {
			logger.error("  Dossier inexistant ou non lisible");
			this.cptErreurs++;
			return;
		}
		this.updateStatusBar("Analyse de " + sourceDirectory.toAbsolutePath());

		// Liste les fichiers du dossier source
		DirectoryStream<Path> stream = Files
				.newDirectoryStream(sourceDirectory);
		try {
			Iterator<Path> iterator = stream.iterator();
			while (iterator.hasNext()) {
				Path currentFile = iterator.next();
				if (Files.isRegularFile(currentFile)) {
					this.cptFichiersAnalyses++;

				} else if (Files.isDirectory(currentFile) && this.optRecursive) {
					this.analyseDirectory(currentFile);
				}
			}
		} finally {
			stream.close();
		}

		this.updateStatusBar("");
		this.cptDossiersAnalyses++;
	}

	/**
     * 
     */
	public void buildUI() {
		JMenu menu = new JMenu(this.getName());

		JMenuItem menuItemSettings = new JMenuItem();
		menuItemSettings
				.setAction(new PictureOrganizerDisplaySettingsWindowAction(
						"Settings"));
		menu.add(menuItemSettings);

		JMenuItem menuItemLaunch = new JMenuItem();
		menuItemLaunch.setAction(new PictureOrganizerLaunchAction("Launch"));
		menu.add(menuItemLaunch);

		MainWindow.getInstance().getMenuPlugins().add(menu);
	}

	public Path getInitialSourceDirectory() {
		return this.initialSourceDirectory;
	}

	public List<Job> getJobs() {
		return this.jobs;
	}

	@Override
	public String getName() {
		return "PictureOrganizer";
	}

	public PictureOrganizerSettingsWindow getPictureOrganizerSettingsWindow() {
		return this.pictureOrganizerSettingsWindow;
	}

	/**
	 * Affiche le rapport des compteurs
	 * 
	 * @return
	 */
	@Override
	public String getReport() {
		String rapport = "";
		rapport += this.cptDossiersAnalyses + " dossiers analysés \n";
		rapport += this.cptFichiersAnalyses + " fichiers analysés \n";
		rapport += this.cptFichiersDeplaces + " fichiers déplacés \n";
		rapport += this.cptFichiersCopies + " fichiers copiés \n";
		rapport += this.cptFichiersIgnores + " fichiers ignorés \n";
		rapport += this.cptErreurs + " erreurs \n";
		return rapport;
	}

	public Path getSpecificDestDirectory() {
		return this.specificDestDirectory;
	}

	/**
	 * Initialize les différents compteurs.
	 */
	private void initializeCpt() {
		this.cptFichiersAnalyses = 0;
		this.cptFichiersDeplaces = 0;
		this.cptFichiersCopies = 0;
		this.cptFichiersIgnores = 0;
		this.cptDossiersAnalyses = 0;
		this.cptErreurs = 0;
	}

	public boolean isIndexExistingFilename() {
		return this.optIndexExistingFilename;
	}

	public boolean isOptKeepOriginalDirectoryStructure() {
		return this.optKeepOriginalDirectoryStructure;
	}

	public boolean isOptKeepOriginalFile() {
		return this.optKeepOriginalFile;
	}

	public boolean isOptOrganize() {
		return this.optOrganize;
	}

	public boolean isOptOverwriteExistingFile() {
		return this.optOverwriteExistingFile;
	}

	public boolean isOptRecursive() {
		return this.optRecursive;
	}

	public boolean isOptRename() {
		return this.optRename;
	}

	public boolean isOptSimulation() {
		return this.optSimulation;
	}

	public boolean isOptTreatImageFiles() {
		return this.optTreatImageFiles;
	}

	public boolean isOptTreatVideoFiles() {
		return this.optTreatVideoFiles;
	}

	public boolean isOptUseSpecificDestDirectory() {
		return this.optUseSpecificDestDirectory;
	}

	/**
	 * 
	 * @param sourceDirectory
	 *            : Dossier à traiter.
	 * @param destDirectory
	 *            : Dossier où seront déplacés/copiés les fichiers. Si valeur à
	 *            null alors sourceRootPath sera utilisé.
	 */
	@Override
	public void launch() {
		this.initializeCpt();
		this.currentJob = new Job();

		// Analyse du dossier
		try {
			this.analyseDirectory(this.initialSourceDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		// Affichage de l'analyse
		int retour = JOptionPane
				.showConfirmDialog(MainWindow.getInstance(),
						"Lancement du traitement sur le dossier \n"
								+ this.initialSourceDirectory.toAbsolutePath()
								+ "\n" + this.cptFichiersAnalyses
								+ " fichiers à traiter " + "\n dans "
								+ this.cptDossiersAnalyses + " dossier(s)",
						"Picture Organizer Plugin",
						JOptionPane.OK_CANCEL_OPTION);

		if (JOptionPane.CANCEL_OPTION == retour) {
			return;
		}

		// Initialisation
		this.initProgressBar(this.cptFichiersAnalyses);
		this.initializeCpt();
		this.currentJob = new Job();
		this.jobs.add(this.currentJob);

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSS");

		String jobFileName = dateFormat
				.format(Calendar.getInstance().getTime());
		Path jobFile = Paths.get(this.initialSourceDirectory.toString(), "job_"
				+ jobFileName + ".xml");

		this.jobHistory = new Xml(jobFile.toAbsolutePath().toString(),
				"Actions");
		this.jobHistory.addOrUpdateSectionData("Treatment Infos", "date",
				jobFileName);

		// Traitement du dossier
		try {
			this.treatDirectory(this.initialSourceDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.jobHistory.save();
		this.updateStatusBar("");
		this.razProgressBar();

	}

	/**
	 * Charge les paramètres depuis le fichier de configuration ou met des
	 * valeurs par défaut.
	 */
	@Override
	public void loadSettings() {
		Xml xml = new Xml(Constants.SETTINGS_FILENAME,
				Constants.SETTINGS_ROOT_NAME);
		this.optRename = Boolean.parseBoolean(xml.readSectionDataValue(
				this.getName(), SETTINGS_RENAMEONLY_NAME,
				Boolean.FALSE.toString()));
		this.optOrganize = Boolean
				.parseBoolean(xml.readSectionDataValue(this.getName(),
						SETTINGS_ORGANIZE_NAME, Boolean.TRUE.toString()));
		this.optRecursive = Boolean.parseBoolean(xml.readSectionDataValue(
				this.getName(), SETTINGS_RECURSIVE_DATA_NAME,
				Boolean.TRUE.toString()));
		this.optKeepOriginalFile = Boolean.parseBoolean(xml
				.readSectionDataValue(this.getName(),
						SETTINGS_KEEPORIGINAL_DATA_NAME,
						Boolean.TRUE.toString()));
		this.optOverwriteExistingFile = Boolean
				.parseBoolean(xml.readSectionDataValue(this.getName(),
						SETTINGS_OVERWRITE_DATA_NAME, Boolean.FALSE.toString()));
		this.optIndexExistingFilename = Boolean.parseBoolean(xml
				.readSectionDataValue(this.getName(), SETTINGS_INDEX_NAME,
						Boolean.TRUE.toString()));
		this.optKeepOriginalDirectoryStructure = Boolean.parseBoolean(xml
				.readSectionDataValue(this.getName(),
						SETTINGS_KEEPORIGINALSTRUCTURE_DATA_NAME,
						Boolean.TRUE.toString()));
		this.optUseSpecificDestDirectory = Boolean.parseBoolean(xml
				.readSectionDataValue(this.getName(),
						SETTINGS_USESPECIFICDESTDIRECTORY_DATA_NAME,
						Boolean.FALSE.toString()));
		// TODO Ajouter test sur existance du dossier lors du lancement du
		// plugin et afficher message d'info
		this.specificDestDirectory = Paths.get(xml.readSectionDataValue(
				this.getName(), SETTINGS_SPECIFICDESTDIRECTORY_DATA_NAME,
				FileSystemView.getFileSystemView().getHomeDirectory()
						.getAbsolutePath()));
		this.optTreatVideoFiles = Boolean.parseBoolean(xml
				.readSectionDataValue(this.getName(),
						SETTINGS_TREAT_VIDEO_FILES_NAME,
						Boolean.FALSE.toString()));
		this.optTreatImageFiles = Boolean.parseBoolean(xml
				.readSectionDataValue(this.getName(),
						SETTINGS_TREAT_IMAGE_FILES_NAME,
						Boolean.TRUE.toString()));
	}

	/**
	 * Sauvegarde les paramètres dans le fichiers de configuration.
	 */
	@Override
	public void saveSettings() {
		Xml xml = new Xml(Constants.SETTINGS_FILENAME,
				Constants.SETTINGS_ROOT_NAME);
		xml.addOrUpdateSectionData(this.getName(),
				SETTINGS_KEEPORIGINAL_DATA_NAME,
				Boolean.toString(this.isOptKeepOriginalFile()));
		xml.addOrUpdateSectionData(this.getName(),
				SETTINGS_OVERWRITE_DATA_NAME,
				Boolean.toString(this.isOptOverwriteExistingFile()));
		xml.addOrUpdateSectionData(this.getName(),
				SETTINGS_RECURSIVE_DATA_NAME,
				Boolean.toString(this.isOptRecursive()));
		xml.addOrUpdateSectionData(this.getName(),
				SETTINGS_KEEPORIGINALSTRUCTURE_DATA_NAME,
				Boolean.toString(this.isOptKeepOriginalDirectoryStructure()));
		xml.addOrUpdateSectionData(this.getName(),
				SETTINGS_USESPECIFICDESTDIRECTORY_DATA_NAME,
				Boolean.toString(this.isOptUseSpecificDestDirectory()));
		xml.addOrUpdateSectionData(this.getName(), SETTINGS_RENAMEONLY_NAME,
				Boolean.toString(this.isOptRename()));
		xml.addOrUpdateSectionData(this.getName(), SETTINGS_INDEX_NAME,
				Boolean.toString(this.isIndexExistingFilename()));
		xml.addOrUpdateSectionData(this.getName(), SETTINGS_ORGANIZE_NAME,
				Boolean.toString(this.isOptOrganize()));
		xml.addOrUpdateSectionData(this.getName(),
				SETTINGS_SPECIFICDESTDIRECTORY_DATA_NAME,
				this.specificDestDirectory.toAbsolutePath().toString());
		xml.addOrUpdateSectionData(this.getName(),
				SETTINGS_TREAT_IMAGE_FILES_NAME,
				Boolean.toString(this.isOptTreatImageFiles()));
		xml.addOrUpdateSectionData(this.getName(),
				SETTINGS_TREAT_VIDEO_FILES_NAME,
				Boolean.toString(this.isOptTreatVideoFiles()));
		xml.save();
	}

	public void setIndexExistingFilename(boolean index) {
		this.optIndexExistingFilename = index;
	}

	public void setInitialSourceDirectory(Path initialSourceDirectory) {
		this.initialSourceDirectory = initialSourceDirectory;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public void setOptKeepOriginalDirectoryStructure(
			boolean keepOriginalDirectoryStructure) {
		this.optKeepOriginalDirectoryStructure = keepOriginalDirectoryStructure;
	}

	public void setOptKeepOriginalFile(boolean keepOriginalFile) {
		this.optKeepOriginalFile = keepOriginalFile;
	}

	public void setOptOrganize(boolean organize) {
		this.optOrganize = organize;
	}

	public void setOptOverwriteExistingFile(boolean overwriteExistingFile) {
		this.optOverwriteExistingFile = overwriteExistingFile;
	}

	public void setOptRecursive(boolean recursive) {
		this.optRecursive = recursive;
	}

	public void setOptRename(boolean rename) {
		this.optRename = rename;
	}

	public void setOptTreatImageFiles(boolean optTreatImageFiles) {
		this.optTreatImageFiles = optTreatImageFiles;
	}

	public void setOptTreatVideoFiles(boolean optTreatVideoFiles) {
		this.optTreatVideoFiles = optTreatVideoFiles;
	}

	public void setOptUseSpecificDestDirectory(boolean useSpecificDestDirectory) {
		this.optUseSpecificDestDirectory = useSpecificDestDirectory;
	}

	public void setPictureOrganizerSettingsWindow(
			PictureOrganizerSettingsWindow pictureOrganizerSettingsWindow) {
		this.pictureOrganizerSettingsWindow = pictureOrganizerSettingsWindow;
	}

	public void setSpecificDestDirectory(Path specificDestDirectory) {
		this.specificDestDirectory = specificDestDirectory;
	}

	/**
	 * 
	 * @param sourceDirectory
	 *            : Dossier à traiter.
	 * @param destDirectory
	 *            : Dossier où seront déplacés/copiés les fichiers. Si valeur à
	 *            null alors sourceRootPath sera utilisé.
	 * @throws IOException
	 */
	private int treatDirectory(Path sourceDirectory) throws IOException {
		logger.info("+Traitement du dossier " + sourceDirectory);
		// Test l'existance et la lecture du dossier source
		if (!Files.exists(sourceDirectory)
				|| !Files.isReadable(sourceDirectory)) {
			logger.error("  Dossier inexistant ou non lisible");
			this.cptErreurs++;
			return 0;
		}

		this.updateStatusBar("Traitement de "
				+ sourceDirectory.toAbsolutePath());

		// Teste l'existance du fichier exclude
		if (Files.exists(Paths.get(sourceDirectory.toString(), "exclude"))) {
			logger.info("	Dossier ignoré");
			return 0;
		}

		// Liste les fichiers du dossier source
		DirectoryStream<Path> stream = Files
				.newDirectoryStream(sourceDirectory);
		try {
			Iterator<Path> iterator = stream.iterator();
			while (iterator.hasNext()) {
				Path currentFile = iterator.next();
				this.checkPause();
				if (Files.isRegularFile(currentFile)) {
					try {
						this.cptFichiersAnalyses++;
						this.treatFile(currentFile);
						this.updateProgressBar(this.cptFichiersAnalyses);
					} catch (MetadataException e) {
						logger.error(
								"   Pb de lecture des données Exif dans le fichier "
										+ currentFile, e);
						this.cptErreurs++;
					} catch (ImageProcessingException e) {
						logger.error("   Pb de format du fichier "
								+ currentFile, e);
						this.cptErreurs++;
					} catch (IOException e) {
						logger.error("   Pb d'acces du fichier " + currentFile,
								e);
						this.cptErreurs++;
					}
				} else if (Files.isDirectory(currentFile) && this.optRecursive) {
					this.treatDirectory(currentFile);
				}
			}
		} finally {
			stream.close();
		}

		this.cptDossiersAnalyses++;
		
		try {
			Files.deleteIfExists(sourceDirectory);
		} catch (DirectoryNotEmptyException dne) {
			logger.error("   Suppression du dossier impossible car non vide ");
		} catch (Exception e) {
			logger.error("   Suppression du dossier impossible : "
					+ sourceDirectory, e);
		}		
		
		return 1;
	}

	/**
	 * 
	 * @param srcFile
	 * @throws ImageProcessingException
	 * @throws MetadataException
	 * @throws IOException
	 */
	private void treatFile(Path srcFile) throws ImageProcessingException,
			MetadataException, IOException {
		logger.info("+Traitement du fichier " + srcFile.toAbsolutePath());
		
		/** Suppression du fichier Thumbs.db */
		if ("Thumbs.db".equals(srcFile.getFileName().toString())) {
			try {
				Files.delete(srcFile);
			} catch (Exception e) {
				logger.error("Suppression impossible : ", e);
			}
		}		

		/** 1° Récupération de la date de prise de vue. */
		Calendar dateTimeOriginal = null;
		if (this.optTreatImageFiles && PathUtils.isImage(srcFile)) {
			dateTimeOriginal = PathUtils.getExifDateTimeOriginal(srcFile);
			if (dateTimeOriginal == null) {
				logger.info("|-Fichier ignoré (Pas d'information exif)");
				this.cptFichiersIgnores++;
				return;
			}
		} else if (this.optTreatVideoFiles && PathUtils.isVideo(srcFile)) {
			dateTimeOriginal = PathUtils.getLastModified(srcFile);
			if (dateTimeOriginal == null) {
				logger.info("|-Fichier ignoré (Pas d'information de date)");
				this.cptFichiersIgnores++;
				return;
			}
		} else {
			String mimeType = null;
			try {
				mimeType = Files.probeContentType(srcFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("|-Fichier ignoré : Mimetype non pris en charge ("
					+ mimeType + ")");
			this.cptFichiersIgnores++;
			return;
		}

		/** 2° Détermination du nom du fichier de destination */
		String destFileBaseName = FilenameUtils.getBaseName(srcFile
				.toAbsolutePath().toString());
		if (this.optRename) {
			DateFormat dateFormatFileName = new SimpleDateFormat(
					"yyyyMMdd_HHmmssSS");
			destFileBaseName = dateFormatFileName.format(dateTimeOriginal
					.getTime());
		}

		/** 3° Récupération de l'extension du fichier */
		String destFileExtension = FilenameUtils.getExtension(
				srcFile.toAbsolutePath().toString()).toLowerCase();

		/** 4° Détermination du dossier destination */
		Path destPath = null;
		// a) Détermination du dossier racine
		if (this.optKeepOriginalDirectoryStructure) {
			if (this.optUseSpecificDestDirectory) {
				destPath = Paths
						.get(StringUtils.replace(
								srcFile.getParent().toString(),
								this.initialSourceDirectory.toAbsolutePath()
										.toString(), this.specificDestDirectory
										.toAbsolutePath().toString()));
			} else {
				destPath = srcFile.getParent();
			}
		} else {
			if (this.optUseSpecificDestDirectory) {
				destPath = this.specificDestDirectory;
			} else {
				destPath = this.initialSourceDirectory;
			}
		}
		// b) Détermination du nom du dossier parent
		if (this.optOrganize) {
			// Détermination du nom de dossier de destination
			DateFormat dateFormatPathName = new SimpleDateFormat("yyyyMMdd");
			String destPathBaseName = dateFormatPathName
					.format(dateTimeOriginal.getTime());

			// Cas où le fichier est déjà classé dans un dossier
			if (destPathBaseName.equals(srcFile.getParent().getFileName())) {
				if (!this.optUseSpecificDestDirectory
						&& this.optKeepOriginalDirectoryStructure) {
					destPath = srcFile.getParent();// On ne crée pas de
					// sous répertoire
				} else {
					destPath = Paths.get(destPath.toString(), destPathBaseName);
				}
			} else {
				destPath = Paths.get(destPath.toString(), destPathBaseName);
			}
		}

		// c) Création du dossier destination
		if (!Files.exists(destPath, LinkOption.NOFOLLOW_LINKS)) {

			if (Files.createDirectory(destPath) == null) {
				logger.error("|-Impossible de créer le dossier destination "
						+ destPath.toAbsolutePath());
				this.cptFichiersIgnores++;
				this.cptErreurs++;
				return;
			} else {
				this.currentJob.getTasks().add(
						new Task(destPath, null, Task.TASK_CREATE_DIR));
				logger.info("CREATE " + destPath.toAbsolutePath());
			}
		}

		/** 5° Construction du nouveau chemin complet */
		Path destFile = Paths.get(destPath.toString(), destFileBaseName + "."
				+ destFileExtension);

		// Cas où le fichier est déjà renommé
		if (this.optRename) {
			if (destFile.getParent().equals(srcFile.getParent())) {
				if (srcFile.getFileName().startsWith(destFileBaseName)) {
					logger.info("|-Fichier ignoré (Déjà renommé)");
					this.cptFichiersIgnores++;
					return;
				}
			}
		}

		/** 6° Fichiers source et destination identiques */
		if (srcFile.toString().equals(destFile.toString())) {
			logger.info("|-Fichier ignoré (Source et destination identiques)");
			this.cptFichiersIgnores++;
			return;
		}

		/** 7° Gestion des colisions */
		if (Files.exists(destFile, LinkOption.NOFOLLOW_LINKS)) {
			if (this.optOverwriteExistingFile) {
				// On écrase
			} else if (this.optIndexExistingFilename) {
				// Fichiers destination déjà existant
				int idx = 1;
				while (Files.exists(destFile, LinkOption.NOFOLLOW_LINKS)) {
					String destFileNameTmp = destFileBaseName + "_" + idx++;
					destFile = Paths.get(destPath.toString(), destFileNameTmp
							+ "." + destFileExtension);
				}
			} else {
				logger.info("|-Fichier ignoré (Fichier destination existant)");
				this.cptFichiersIgnores++;
				return;
			}
		}

		/** 8° Traitement final */
		if (this.optKeepOriginalFile) {
			if (!this.optSimulation) {
				Files.copy(srcFile, destFile);
			}
			this.cptFichiersCopies++;
			this.currentJob.getTasks().add(
					new Task(srcFile, destFile, Task.TASK_COPY));
			logger.info("COPY FROM " + srcFile.toAbsolutePath() + " TO "
					+ destFile.toAbsolutePath());
			this.jobHistory.addOrUpdateSectionData("task_"
					+ this.currentJob.getTasks().size(), "ACTION", "COPY");
		} else {
			if (!this.optSimulation) {
				Files.move(srcFile, destFile);
			}
			this.cptFichiersDeplaces++;
			this.currentJob.getTasks().add(
					new Task(srcFile, destFile, Task.TASK_MOVE));
			logger.info("MOVE FROM " + srcFile.toAbsolutePath() + " TO "
					+ destFile.toAbsolutePath());
			this.jobHistory.addOrUpdateSectionData("task_"
					+ this.currentJob.getTasks().size(), "ACTION", "MOVE");
		}
		this.jobHistory.addOrUpdateSectionData("task_"
				+ this.currentJob.getTasks().size(), "SRC", srcFile
				.toAbsolutePath().toString());
		this.jobHistory.addOrUpdateSectionData("task_"
				+ this.currentJob.getTasks().size(), "DEST", destFile
				.toAbsolutePath().toString());
	}

	/**
	 * Permet d'annuler les tâches du dernier job.
	 * 
	 * @throws IOException
	 */
	public void undoJob() throws IOException {
		if (this.currentJob == null) {
			return;
		}

		List<Task> tachesAnnulees = new ArrayList<Task>();
		List<Task> tachesMkdir = new ArrayList<Task>();

		for (Task task : this.currentJob.getTasks()) {
			try {
				if (task.isCopyTask()) {
					Files.delete(task.getDest());
					logger.info("DELETE " + task.getDest());
				} else if (task.isMoveTask()) {
					Files.move(task.getSource(), task.getDest());
					logger.info("RESTORE FROM " + task.getDest() + " TO "
							+ task.getSource());
				} else if (task.isRenameTask()) {
					Files.move(task.getSource(), task.getDest());
					logger.info("RENAME FROM "
							+ task.getDest().toAbsolutePath() + " TO "
							+ task.getSource().toAbsolutePath());
				} else if (task.isMkdirTask()) {
					tachesMkdir.add(task);
				}
			} catch (FileNotFoundException e) {
				// TODO: handle exception
			}
			tachesAnnulees.add(task);
		}
		for (Task task : tachesMkdir) {
			try {
				Files.delete(task.getSource());
			} catch (Exception e) {
				logger.error("Impossible de supprimer le dossier "
						+ task.getSource().toAbsolutePath());
			}
			tachesAnnulees.add(task);
			logger.info("DELETE " + task.getSource().toAbsolutePath());
		}

		this.currentJob.getTasks().removeAll(tachesAnnulees);
	}

}
