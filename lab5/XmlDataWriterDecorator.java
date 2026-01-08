package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class XmlDataWriterDecorator extends DataWriterDecorator {

    public XmlDataWriterDecorator(DataWriter wrappee) {
        super(wrappee);
    }

    @Override
    public void write(List<House> houses, String filename) {
        if (!filename.toLowerCase().endsWith(".xml")) {
            filename = filename + ".xml";
        }
        createXmlDocument(houses, filename);
    }

    private void createXmlDocument(List<House> houses, String filename) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("houses");
            doc.appendChild(rootElement);

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            for (House house : houses) {
                Element houseElement = doc.createElement("house");
                rootElement.appendChild(houseElement);

                addXmlElement(doc, houseElement, "id", String.valueOf(house.getId()));
                addXmlElement(doc, houseElement, "type", house.getType());
                addXmlElement(doc, houseElement, "area", String.valueOf(house.getArea()));
                addXmlElement(doc, houseElement, "rooms", String.valueOf(house.getRooms()));
                addXmlElement(doc, houseElement, "date", sdf.format(house.getDate()));
                addXmlElement(doc, houseElement, "price", String.valueOf(house.getPrice()));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);

        } catch (Exception e) {
            System.out.println("Error writing XML file: " + e.getMessage());
        }
    }

    private void addXmlElement(Document doc, Element parent, String name, String value) {
        Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value));
        parent.appendChild(element);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + XML Format";
    }
}
