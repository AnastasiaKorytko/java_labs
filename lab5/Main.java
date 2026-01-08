package org.example;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.zip.*;

abstract class DataStorage {
    public abstract void addHouse(House house);
    public abstract void updateHouse(int id, House house);
    public abstract void deleteHouse(int id);
    public abstract void displayAll();
    public abstract House getHouseById(int id);
    public abstract List<House> getAllHouses();
}

abstract class FileReadWrite {
    public abstract void readFromFile(String filename);
    public abstract void writeToFile(String filename);
    public abstract void readFromXML(String filename);
    public abstract void writeToXML(String filename);
    public abstract void readFromJSON(String filename);
    public abstract void writeToJSON(String filename);
    public abstract void encrWriteToFile(String filename);
    public abstract void encrReadFromFile(String filename);
}

class HouseFile extends FileReadWrite {
    private DataStorage storage;
    public HouseFile(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public void readFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            int loadedCount = 0;
            int duplicatedCount = 0;

            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length == 6) {
                        int originalId = Integer.parseInt(parts[0].trim());
                        String type = parts[1].trim();
                        double area = Double.parseDouble(parts[2].trim());
                        int rooms = Integer.parseInt(parts[3].trim());
                        Date date = sdf.parse(parts[4].trim());
                        double price = Double.parseDouble(parts[5].trim());
                        HouseBuilder houseBuilder = new ConcreteHouseBuilder();
                        House newHouse = houseBuilder
                                .setId(originalId)
                                .setType(type)
                                .setArea(area)
                                .setRooms(rooms)
                                .setDate(date)
                                .setPrice(price)
                                .build();

                        int finalId = findSuitableId(newHouse, originalId);

                        if (finalId != originalId) {
                            newHouse.setId(finalId);
                            duplicatedCount++;
                        }

                        storage.addHouse(newHouse);
                        loadedCount++;
                    } else {
                        System.out.println("Error: incorrect string - " + line);
                    }
                } catch (NumberFormatException | ParseException e) {
                    System.out.println("Error: incorrect data - " + line);
                    logError("Reading error: " + line + "  " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("File reading error: " + e.getMessage());
        }
    }

    @Override
    public void readFromXML(String filename) {
        try {
            File xmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList houseList = doc.getElementsByTagName("house");
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            int loadedCount = 0;
            int duplicatedCount = 0;
            for (int i = 0; i < houseList.getLength(); i++) {
                Node houseNode = houseList.item(i);
                if (houseNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element houseElement = (Element) houseNode;
                    int originalId = Integer.parseInt(getElementValue(houseElement, "id"));
                    String type = getElementValue(houseElement, "type");
                    double area = Double.parseDouble(getElementValue(houseElement, "area"));
                    int rooms = Integer.parseInt(getElementValue(houseElement, "rooms"));
                    Date date = sdf.parse(getElementValue(houseElement, "date"));
                    double price = Double.parseDouble(getElementValue(houseElement, "price"));
                    HouseBuilder houseBuilder = new ConcreteHouseBuilder();
                    House newHouse = houseBuilder.setId(originalId).setType(type).setArea(area).setRooms(rooms).setDate(date).setPrice(price).build();
                    int finalId = findSuitableId(newHouse, originalId);
                    if (finalId != originalId) {
                        newHouse.setId(finalId);
                        duplicatedCount++;
                    }
                    storage.addHouse(newHouse);
                    loadedCount++;
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | ParseException e) {
            System.out.println("XML reading error: " + e.getMessage());
            logError("XML reading error: " + e.getMessage());
        }
    }

    @Override
    public void readFromJSON(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
            String jsonString = jsonContent.toString().trim();
            if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
                jsonString = jsonString.substring(1, jsonString.length() - 1);
            }

            String[] houseObjects = jsonString.split("\\},\\s*\\{");
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            int loadCount = 0;
            int duplicateCount = 0;

            for (int i = 0; i < houseObjects.length; i++) {
                String houseStr = houseObjects[i];
                if (i == 0) houseStr = houseStr + "}";
                else if (i == houseObjects.length - 1) houseStr = "{" + houseStr;
                else houseStr = "{" + houseStr + "}";
                Map<String, String> houseMap = parseJSONObject(houseStr);
                int originalId = Integer.parseInt(houseMap.get("id"));
                String type = houseMap.get("type");
                double area = Double.parseDouble(houseMap.get("area"));
                int rooms = Integer.parseInt(houseMap.get("rooms"));
                Date date = sdf.parse(houseMap.get("date"));
                double price = Double.parseDouble(houseMap.get("price"));
                HouseBuilder houseBuilder = new ConcreteHouseBuilder();
                House newHouse = houseBuilder.setId(originalId).setType(type).setArea(area).setRooms(rooms).setDate(date).setPrice(price).build();
                int finalId = findSuitableId(newHouse, originalId);
                if (finalId != originalId) {
                    newHouse.setId(finalId);
                    duplicateCount++;
                }
                storage.addHouse(newHouse);
                loadCount++;
            }
        } catch (IOException | ParseException e) {
            System.out.println("JSON reading error: " + e.getMessage());
            logError("JSON reading error: " + e.getMessage());
        }
    }

    private int findSuitableId(House newHouse, int origId) {
        if (storage.getHouseById(origId) == null) {
            return origId;
        }
        House existingId = storage.getHouseById(origId);
        if (isDuplicate(existingId, newHouse)) {
            return origId;
        }
        return findNextId();
    }

    private boolean isDuplicate(House house1, House house2) {
        if (house1 == null || house2 == null) return false;
        return house1.getType().equals(house2.getType()) &&
                Math.abs(house1.getArea() - house2.getArea()) < 0.001 &&
                house1.getRooms() == house2.getRooms() &&
                house1.getDate().equals(house2.getDate()) &&
                Math.abs(house1.getPrice() - house2.getPrice()) < 0.001;
    }

    private int findNextId() {
        int maxId = 0;
        List<House> allHouses = storage.getAllHouses();
        for (House house : allHouses) {
            if (house.getId() > maxId) {
                maxId = house.getId();
            }
        }
        return maxId + 1;
    }

    @Override
    public void writeToFile(String filename) {
        DataWriter writer = new CsvDataWriter();
        System.out.println("Using: " + writer.getDescription());
        writer.write(storage.getAllHouses(), filename);
    }

    @Override
    public void writeToXML(String filename) {
        DataWriter writer = new XmlDataWriterDecorator(new CsvDataWriter());
        System.out.println("Using: " + writer.getDescription());
        writer.write(storage.getAllHouses(), filename);
    }

    @Override
    public void writeToJSON(String filename) {
        DataWriter writer = new JsonDataWriterDecorator(new CsvDataWriter());
        System.out.println("Using: " + writer.getDescription());
        writer.write(storage.getAllHouses(), filename);
    }

    @Override
    public void encrWriteToFile(String filename) {
        DataWriter writer = new EncryptedDataWriterDecorator(new CsvDataWriter());
        System.out.println("Using: " + writer.getDescription());
        writer.write(storage.getAllHouses(), filename);
    }

    @Override
    public void encrReadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String decrypted = Encryption.decrypt(sb.toString());
            BufferedReader reader = new BufferedReader(new StringReader(decrypted));
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            String record;
            while ((record = reader.readLine()) != null) {
                String[] parts = record.split(",");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0].trim());
                    String type = parts[1].trim();
                    double area = Double.parseDouble(parts[2].trim());
                    int rooms = Integer.parseInt(parts[3].trim());
                    Date date = sdf.parse(parts[4].trim());
                    double price = Double.parseDouble(parts[5].trim());
                    HouseBuilder houseBuilder = new ConcreteHouseBuilder();
                    House house = houseBuilder.setId(id).setType(type).setArea(area).setRooms(rooms).setDate(date).setPrice(price).build();
                    storage.addHouse(house);
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("File reading error: " + e.getMessage());
        }
    }

    public void writeToEncryptedXML(String filename) {
        DataWriter writer = new EncryptedDataWriterDecorator(
                new XmlDataWriterDecorator(
                        new CsvDataWriter()
                )
        );
        System.out.println("Using: " + writer.getDescription());
        writer.write(storage.getAllHouses(), filename);
    }

    public void writeToEncryptedJSON(String filename) {
        DataWriter writer = new EncryptedDataWriterDecorator(
                new JsonDataWriterDecorator(
                        new CsvDataWriter()
                )
        );
        System.out.println("Using: " + writer.getDescription());
        writer.write(storage.getAllHouses(), filename);
    }

    private String getElementValue(Element parent, String name) {
        NodeList nodeList = parent.getElementsByTagName(name);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    private Element createElement(Document doc, String name, String value) {
        Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value));
        return element;
    }

    private Map<String, String> parseJSONObject(String jsonObject) {
        Map<String, String> result = new HashMap<>();
        jsonObject = jsonObject.trim();
        if (jsonObject.startsWith("{") && jsonObject.endsWith("}")) {
            jsonObject = jsonObject.substring(1, jsonObject.length() - 1);
        }
        String[] pairs = jsonObject.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim().replace("\"", "");
                result.put(key, value);
            }
        }
        return result;
    }

    private void logError(String errorMessage) {
        try (PrintWriter logger = new PrintWriter(new FileWriter("error.log", true))) {
            logger.println(new Date() + " - " + errorMessage);
        } catch (IOException e) {
            System.out.println("log-file writing error: " + e.getMessage());
        }
    }
}

class Encryption {
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1234567890abcdef";

    public static String encrypt(String data) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            System.out.println("Encryption error: " + e.getMessage());
            return data;
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            System.out.println("Decryption error: " + e.getMessage());
            return encryptedData;
        }
    }
}

class Archiving {
    public static void zipFile(String sourceFile, String zipName) {
        try (FileOutputStream fos = new FileOutputStream(zipName);
             ZipOutputStream zipOut = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(sourceFile)) {
            ZipEntry zipEntry = new ZipEntry(new File(sourceFile).getName());
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        } catch (IOException e) {
            System.out.println("Zip error: " + e.getMessage());
        }
    }

    public static void unzipFile(String zipName, String outputDir) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipName))) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                File newFile = new File(outputDir, entry.getName());
                new File(newFile.getParent()).mkdirs();
                try (FileOutputStream os = new FileOutputStream(newFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zis.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                }
                entry = zis.getNextEntry();
            }
            zis.closeEntry();
        } catch (IOException e) {
            System.out.println("Unzip error: " + e.getMessage());
        }
    }
}

class Menu {
    private Scanner scan;
    private DataStorage storage;
    private HouseFile file;
    private HouseBuilder builder;
    private HouseDirector director;

    public Menu(DataStorage storage) {
        this.scan = new Scanner(System.in);
        this.storage = storage;
        this.file = new HouseFile(storage);
        this.builder = new ConcreteHouseBuilder();
        this.director = new HouseDirector(builder);
    }

    public void showMenu() {
        while (true) {
            System.out.println();
            System.out.println("HOUSE FACTORY");
            System.out.println("1.Show all houses");
            System.out.println("2. Add house");
            System.out.println("3.Add Economy house");
            System.out.println("4.Add Premium house");
            System.out.println("5.Edit house");
            System.out.println("6.Delete house");
            System.out.println("7.Load from file");
            System.out.println("8.Save to file");
            System.out.println("9.Sort houses");
            System.out.println("10.Load from XML");
            System.out.println("11.Save to XML (XML Decorator)");
            System.out.println("12.Load from JSON");
            System.out.println("13.Save to JSON (JSON Decorator)");
            System.out.println("14.Encrypt & Save ");
            System.out.println("15.Decrypt & Load File");
            System.out.println("16.Save to Encrypted XML");
            System.out.println("17.Save to Encrypted JSON");
            System.out.println("18.Zip File");
            System.out.println("19.Unzip File");
            System.out.println("0.Quit");

            try {
                int choice = Integer.parseInt(scan.nextLine());
                switch (choice) {
                    case 1:
                        storage.displayAll();
                        break;
                    case 2:
                        addFromConsole();
                        break;
                    case 3:
                        addEconomyHouse();
                        break;
                    case 4:
                        addPremiumHouse();
                        break;
                    case 5:
                        updateFromConsole();
                        break;
                    case 6:
                        deleteFromConsole();
                        break;
                    case 7:
                        System.out.print("Enter the file name: ");
                        String readFile = scan.nextLine();
                        file.readFromFile(readFile);
                        break;
                    case 8:
                        System.out.print("Enter the file name: ");
                        String writeFile = scan.nextLine();
                        file.writeToFile(writeFile);
                        break;
                    case 9:
                        sort();
                        break;
                    case 10:
                        System.out.print("Enter the XML file name: ");
                        String readXML = scan.nextLine();
                        file.readFromXML(readXML);
                        break;
                    case 11:
                        System.out.print("Enter the XML file name: ");
                        String writeXML = scan.nextLine();
                        file.writeToXML(writeXML);
                        break;
                    case 12:
                        System.out.print("Enter the JSON file name: ");
                        String readJSON = scan.nextLine();
                        file.readFromJSON(readJSON);
                        break;
                    case 13:
                        System.out.print("Enter the JSON file name: ");
                        String writeJSON = scan.nextLine();
                        file.writeToJSON(writeJSON);
                        break;
                    case 14:
                        System.out.print("Enter the filename to encrypt and save: ");
                        String encFile = scan.nextLine();
                        file.encrWriteToFile(encFile);
                        break;
                    case 15:
                        System.out.print("Enter the filename to decrypt and load: ");
                        String decFile = scan.nextLine();
                        file.encrReadFromFile(decFile);
                        break;
                    case 16:
                        System.out.print("Enter filename for encrypted XML: ");
                        String encXmlFile = scan.nextLine();
                        file.writeToEncryptedXML(encXmlFile);
                        break;
                    case 17:
                        System.out.print("Enter filename for encrypted JSON: ");
                        String encJsonFile = scan.nextLine();
                        file.writeToEncryptedJSON(encJsonFile);
                        break;
                    case 18:
                        System.out.print("Enter source file to zip: ");
                        String srcZip = scan.nextLine();
                        System.out.print("Enter archive name: ");
                        String zipName = scan.nextLine();
                        Archiving.zipFile(srcZip, zipName);
                        break;
                    case 19:
                        System.out.print("Enter ZIP file to extract: ");
                        String zipFile = scan.nextLine();
                        System.out.print("Enter output folder: ");
                        String outFolder = scan.nextLine();
                        Archiving.unzipFile(zipFile, outFolder);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: enter a number");
            }
        }
    }

    private void addFromConsole() {
        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(scan.nextLine());
            if (storage.getHouseById(id) != null) {
                System.out.println("House with this ID already exists");
                return;
            }
            House house = director.constructHouseFromConsole(scan, id);
            if (house != null) {
                storage.addHouse(house);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addEconomyHouse() {
        try {
            System.out.println("\nCREATE ECONOMY HOUSE");
            System.out.print("ID: ");
            int id = Integer.parseInt(scan.nextLine());

            if (storage.getHouseById(id) != null) {
                System.out.println("House with this ID already exists");
                return;
            }

            System.out.print("Area (max 100 sq.m.): ");
            double area = Double.parseDouble(scan.nextLine());
            House house = director.constructEconomyHouse(id, area);
            storage.addHouse(house);
            System.out.println("Auto-calculated price: " + house.getPrice());
            System.out.println("Type: " + house.getType());
            System.out.println("Rooms: " + house.getRooms());

        } catch (Exception e) {
            System.out.println("Error creating economy house: " + e.getMessage());
        }
    }

    private void addPremiumHouse() {
        try {
            System.out.println("\nCREATE PREMIUM HOUSE");
            System.out.print("ID: ");
            int id = Integer.parseInt(scan.nextLine());

            if (storage.getHouseById(id) != null) {
                System.out.println("House with this ID already exists");
                return;
            }

            System.out.print("Area (min 150 sq.m.): ");
            double area = Double.parseDouble(scan.nextLine());
            System.out.print("Number of rooms (min 4): ");
            int rooms = Integer.parseInt(scan.nextLine());
            House house = director.constructPremiumHouse(id, area, rooms);
            storage.addHouse(house);
            System.out.println("Auto-calculated price: " + house.getPrice());
            System.out.println("Type: " + house.getType());

        } catch (Exception e) {
            System.out.println("Error creating premium house: " + e.getMessage());
        }
    }

    private void updateFromConsole() {
        try {
            System.out.print("Enter the house ID to edit: ");
            int id = Integer.parseInt(scan.nextLine());
            House existingHouse = storage.getHouseById(id);
            if (existingHouse == null) {
                System.out.println("House not found");
                return;
            }

            System.out.print("New type: ");
            String type = scan.nextLine();
            System.out.print("New area: ");
            double area = Double.parseDouble(scan.nextLine());
            System.out.print("New number of rooms: ");
            int rooms = Integer.parseInt(scan.nextLine());
            System.out.print("New construction date (dd.mm.yyyy): ");
            String dateStr = scan.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date date = sdf.parse(dateStr);
            System.out.print("New price: ");
            double price = Double.parseDouble(scan.nextLine());
            HouseBuilder houseBuilder = new ConcreteHouseBuilder();
            House updatedHouse = houseBuilder.setId(id).setType(type).setArea(area).setRooms(rooms).setDate(date).setPrice(price).build();
            storage.updateHouse(id, updatedHouse);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteFromConsole() {
        try {
            System.out.print("Enter the house ID to delete: ");
            int id = Integer.parseInt(scan.nextLine());
            storage.deleteHouse(id);
        } catch (NumberFormatException e) {
            System.out.println("Error: enter correct ID");
        }
    }

    private void sort() {
        System.out.println("\nSORT");
        System.out.println("1. By price");
        System.out.println("2. By area");
        System.out.println("3. By number of rooms");
        System.out.println("4. By construction date");
        System.out.print("Choose a field to sort by: ");

        try {
            int choice = Integer.parseInt(scan.nextLine());

            if (storage instanceof HouseListStorage) {
                List<House> houses = ((HouseListStorage) storage).getAllHouses();

                switch (choice) {
                    case 1:
                        houses.sort(Comparator.comparingDouble(House::getPrice));
                        break;
                    case 2:
                        houses.sort(Comparator.comparingDouble(House::getArea));
                        break;
                    case 3:
                        houses.sort(Comparator.comparingInt(House::getRooms));
                        break;
                    case 4:
                        houses.sort(Comparator.comparing(House::getDate));
                        break;
                    default:
                        System.out.println("Error: incorrect choice");
                        return;
                }

            } else if (storage instanceof HouseMapStorage) {
                HouseMapStorage mapStorage = (HouseMapStorage) storage;
                Map<Integer, House> originalMap = mapStorage.getHousesMap();
                Comparator<House> houseComparator;
                switch (choice) {
                    case 1:
                        houseComparator = Comparator.comparingDouble(House::getPrice);
                        break;
                    case 2:
                        houseComparator = Comparator.comparingDouble(House::getArea);
                        break;
                    case 3:
                        houseComparator = Comparator.comparingInt(House::getRooms);
                        break;
                    case 4:
                        houseComparator = Comparator.comparing(House::getDate);
                        break;
                    default:
                        System.out.println("Error: incorrect choice");
                        return;
                }
                List<House> sortedHouses = new ArrayList<>(originalMap.values());
                sortedHouses.sort(houseComparator);
                Map<Integer, House> sortedMap = new LinkedHashMap<>();
                for (House house : sortedHouses) {
                    sortedMap.put(house.getId(), house);
                }
                originalMap.clear();
                originalMap.putAll(sortedMap);
            }
            storage.displayAll();

        } catch (NumberFormatException e) {
            System.out.println("Error: enter a number");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("HOUSE FACTORY");
        System.out.println();

        DataStorage storage = new HouseMapStorage();
        Menu menu = new Menu(storage);
        menu.showMenu();
    }
}