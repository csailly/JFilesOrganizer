package com.nestof.io.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class Xml {
	public static void main(String[] args) throws IOException {
		Xml xml = new Xml("test.xml", "test");
		Element section = xml.addSection("Ma section");
		xml.addOrUpdateSectionData("Ma section", "donnee1", "test data 1");
		xml.addOrUpdateSectionData("Ma section", "donnee2", "test data 2");

		System.out.println(xml.readSectionDataValue("Ma section", "donnee1",
				null));
		System.out.println(xml.readSectionDataValue("Ma section", "donnee2",
				null));

		xml.addOrUpdateSectionData("Ma section", "donnee1", "test data 11");
		xml.addOrUpdateSectionData("Ma section", "donnee2", "test data 21");

		System.out.println(xml.readSectionDataValue("Ma section", "donnee1",
				null));
		System.out.println(xml.readSectionDataValue("Ma section", "donnee2",
				null));

		xml.save();
	}

	private Element racine;

	private org.jdom.Document document;

	private final String fileName;

	/**
	 * 
	 * @param fileName
	 * @param mode
	 */
	public Xml(String fileName, String rootElement) {
		this.fileName = fileName;

		if (new File(this.fileName).exists()) {
			// On crée une instance de SAXBuilder
			SAXBuilder sxb = new SAXBuilder();
			try {
				// On crée un nouveau document JDOM avec en argument le fichier
				// XML
				// Le parsing est terminé ;)
				this.document = sxb.build(new File(this.fileName));
			} catch (Exception e) {
				// TODO exception
			}

			// On initialise un nouvel élément racine avec l'élément racine du
			// document.
			this.racine = this.document.getRootElement();
		} else {
			this.racine = new Element(rootElement);
			// On crée un nouveau Document JDOM basé sur la racine que l'on
			// vient de créer
			this.document = new Document(this.racine);
		}
	}

	/**
	 * 
	 * @param sectionName
	 * @param dataName
	 * @param dataValue
	 * @return
	 */
	public Element addOrUpdateSectionData(String sectionName, String dataName,
			String dataValue) {
		Element section = this.readSection(sectionName);

		if (section == null) {
			section = this.addSection(sectionName);
		}

		Element data = this.readSectionData(sectionName, dataName);

		if (data == null) {

			data = new Element("Data");
			Attribute att1 = new Attribute("name", dataName);
			data.setAttribute(att1);

			Attribute att2 = new Attribute("value", dataValue);
			data.setAttribute(att2);

			section.addContent(data);

		} else {
			data.removeAttribute("name");
			data.removeAttribute("value");
			Attribute att1 = new Attribute("name", dataName);
			data.setAttribute(att1);

			Attribute att2 = new Attribute("value", dataValue);
			data.setAttribute(att2);
		}

		return data;
	}

	/**
	 * 
	 * @param sectionName
	 * @return
	 */
	private Element addSection(String sectionName) {

		Element section = this.readSection(sectionName);

		if (section == null) {
			section = new Element("Section");

			Attribute classe = new Attribute("name", sectionName);
			section.setAttribute(classe);

			this.racine.addContent(section);
		}

		return section;
	}

	/**
	 * 
	 * @param sectionName
	 * @return
	 */
	private Element readSection(String sectionName) {
		List<Element> listSection = this.racine.getChildren("Section");
		for (Element element : listSection) {
			if (sectionName.equals(element.getAttribute("name").getValue())) {
				return element;
			}
		}
		return null;
	}

	private Element readSectionData(String sectionName, String dataName) {

		List<Element> listSection = this.racine.getChildren("Section");
		List<Element> listData;
		for (Element element : listSection) {
			if (sectionName.equals(element.getAttribute("name").getValue())) {
				listData = element.getChildren();

				for (Element data : listData) {
					if (dataName.equals(data.getAttribute("name").getValue())) {
						return data;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param sectionName
	 * @param dataName
	 * @param defaultValue
	 *            : the value returned if not found.
	 * @return
	 */
	public String readSectionDataValue(String sectionName, String dataName,
			String defaultValue) {

		List<Element> listSection = this.racine.getChildren("Section");
		List<Element> listData;
		for (Element element : listSection) {
			if (sectionName.equals(element.getAttribute("name").getValue())) {
				listData = element.getChildren();

				for (Element data : listData) {
					if (dataName.equals(data.getAttribute("name").getValue())) {
						return data.getAttribute("value").getValue();
					}
				}
			}
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param sectionName
	 */
	private boolean removeSection(String sectionName) {

		List<Element> listSection = this.racine.getChildren("Section");

		for (Element element : listSection) {
			if (sectionName.equals(element.getAttribute("name").getValue())) {
				return this.racine.removeContent(element);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param sectionName
	 * @param dataName
	 * @return
	 */
	private boolean removeSectionData(String sectionName, String dataName) {

		List<Element> listSection = this.racine.getChildren("Section");
		List<Element> listData;
		for (Element element : listSection) {
			if (sectionName.equals(element.getAttribute("name").getValue())) {
				listData = element.getChildren();

				for (Element data : listData) {
					if (dataName.equals(data.getAttribute("name").getValue())) {
						return element.removeContent(data);
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void save() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(
					this.fileName);
			// On utilise ici un affichage classique avec getPrettyFormat()
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			// Remarquez qu'il suffit simplement de créer une instance de
			// FileOutputStream
			// avec en argument le nom du fichier pour effectuer la
			// sérialisation.
			sortie.output(this.document, fileOutputStream);
			fileOutputStream.close();
		} catch (java.io.IOException e) {
		}
	}

}