package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainGUI extends JFrame {
    private DataStorage storage;
    private HouseFile file;
    private HouseBuilder builder;
    private HouseDirector director;

    private JTable houseTable;
    private DefaultTableModel tableModel;
    private JTextArea logArea;
    private JComboBox<String> typeComboBox;
    private JSpinner areaSpinner;
    private JSpinner roomsSpinner;
    private JSpinner priceSpinner;
    private JSpinner idSpinner;

    public MainGUI() {
        storage = new HouseMapStorage();
        file = new HouseFile(storage);
        builder = new ConcreteHouseBuilder();
        director = new HouseDirector(builder);

        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        String[] columnNames = {"ID", "Type", "Area", "Rooms", "Date", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        houseTable = new JTable(tableModel);
        houseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        typeComboBox = new JComboBox<>(new String[]{"Standard", "Economy", "Premium", "Luxury"});

        SpinnerNumberModel idModel = new SpinnerNumberModel(1, 1, 10000, 1);
        idSpinner = new JSpinner(idModel);

        SpinnerNumberModel areaModel = new SpinnerNumberModel(50.0, 10.0, 500.0, 0.5);
        areaSpinner = new JSpinner(areaModel);

        SpinnerNumberModel roomsModel = new SpinnerNumberModel(2, 1, 10, 1);
        roomsSpinner = new JSpinner(roomsModel);

        SpinnerNumberModel priceModel = new SpinnerNumberModel(50000.0, 10000.0, 1000000.0, 1000.0);
        priceSpinner = new JSpinner(priceModel);

        areaSpinner.setEditor(new JSpinner.NumberEditor(areaSpinner, "0.0"));
        priceSpinner.setEditor(new JSpinner.NumberEditor(priceSpinner, "0.00"));
    }

    private void setupLayout() {
        setTitle("House Factory");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("House Management System"));
        add(topPanel, BorderLayout.NORTH);

        JSplitPane centerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        JScrollPane tableScrollPane = new JScrollPane(houseTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Houses List"));
        centerSplitPane.setTopComponent(tableScrollPane);

        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Operation Log"));
        centerSplitPane.setBottomComponent(logScrollPane);

        centerSplitPane.setDividerLocation(400);
        add(centerSplitPane, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("House Management"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        leftPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(idSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        leftPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(typeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        leftPanel.add(new JLabel("Area (sq.m.):"), gbc);
        gbc.gridx = 1;
        leftPanel.add(areaSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        leftPanel.add(new JLabel("Rooms:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(roomsSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        leftPanel.add(new JLabel("Price ($):"), gbc);
        gbc.gridx = 1;
        leftPanel.add(priceSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JButton addButton = new JButton("Add House");
        addButton.setBackground(new Color(100, 200, 100));
        leftPanel.add(addButton, gbc);

        gbc.gridy = 6;
        JButton updateButton = new JButton("Update Selected");
        updateButton.setBackground(new Color(100, 150, 200));
        leftPanel.add(updateButton, gbc);

        gbc.gridy = 7;
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setBackground(new Color(200, 100, 100));
        leftPanel.add(deleteButton, gbc);

        gbc.gridy = 8;
        JButton clearButton = new JButton("Clear All");
        leftPanel.add(clearButton, gbc);

        gbc.gridy = 9;
        JButton refreshButton = new JButton("Refresh Table");
        refreshButton.setBackground(new Color(200, 200, 100));
        leftPanel.add(refreshButton, gbc);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("File Operations"));
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 2;

        int row = 0;

        rightPanel.add(new JLabel("Load From:"), gbc);
        row++;

        gbc.gridy = row;
        JPanel loadPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        JButton loadFileBtn = new JButton("Load from File");
        JButton loadXMLBtn = new JButton("Load from XML");
        JButton loadJSONBtn = new JButton("Load from JSON");
        JButton loadEncBtn = new JButton("Load Encrypted File");

        loadFileBtn.addActionListener(e -> loadFromFile());
        loadXMLBtn.addActionListener(e -> loadFromXML());
        loadJSONBtn.addActionListener(e -> loadFromJSON());
        loadEncBtn.addActionListener(e -> loadFromEncrypted());

        loadPanel.add(loadFileBtn);
        loadPanel.add(loadXMLBtn);
        loadPanel.add(loadJSONBtn);
        loadPanel.add(loadEncBtn);

        gbc.gridy = row;
        rightPanel.add(loadPanel, gbc);
        row += 2;

        gbc.gridy = row;
        rightPanel.add(new JLabel("Save To:"), gbc);
        row++;

        gbc.gridy = row;
        JPanel savePanel = new JPanel(new GridLayout(7, 1, 5, 5));

        JButton saveFileBtn = new JButton("Save to File");
        JButton saveXMLBtn = new JButton("Save to XML");
        JButton saveJSONBtn = new JButton("Save to JSON");
        JButton saveEncBtn = new JButton("Save Encrypted");
        JButton saveEncXMLBtn = new JButton("Save Encrypted XML");
        JButton saveEncJSONBtn = new JButton("Save Encrypted JSON");
        JButton saveAllBtn = new JButton("Save All Formats");

        saveFileBtn.addActionListener(e -> saveToFile());
        saveXMLBtn.addActionListener(e -> saveToXML());
        saveJSONBtn.addActionListener(e -> saveToJSON());
        saveEncBtn.addActionListener(e -> saveToEncrypted());
        saveEncXMLBtn.addActionListener(e -> saveToEncryptedXML());
        saveEncJSONBtn.addActionListener(e -> saveToEncryptedJSON());
        saveAllBtn.addActionListener(e -> saveAllFormats());

        savePanel.add(saveFileBtn);
        savePanel.add(saveXMLBtn);
        savePanel.add(saveJSONBtn);
        savePanel.add(saveEncBtn);
        savePanel.add(saveEncXMLBtn);
        savePanel.add(saveEncJSONBtn);
        savePanel.add(saveAllBtn);

        gbc.gridy = row;
        rightPanel.add(savePanel, gbc);
        row += 2;

        gbc.gridy = row;
        rightPanel.add(new JLabel("Archive Operations:"), gbc);
        row++;

        gbc.gridy = row;
        JPanel archivePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton zipBtn = new JButton("Zip File");
        JButton unzipBtn = new JButton("Unzip File");

        zipBtn.addActionListener(e -> zipFile());
        unzipBtn.addActionListener(e -> unzipFile());

        archivePanel.add(zipBtn);
        archivePanel.add(unzipBtn);

        gbc.gridy = row;
        rightPanel.add(archivePanel, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton sortPriceBtn = new JButton("Sort by Price");
        JButton sortAreaBtn = new JButton("Sort by Area");
        JButton sortRoomsBtn = new JButton("Sort by Rooms");
        JButton sortDateBtn = new JButton("Sort by Date");

        sortPriceBtn.addActionListener(e -> sortHouses(1));
        sortAreaBtn.addActionListener(e -> sortHouses(2));
        sortRoomsBtn.addActionListener(e -> sortHouses(3));
        sortDateBtn.addActionListener(e -> sortHouses(4));

        bottomPanel.add(sortPriceBtn);
        bottomPanel.add(sortAreaBtn);
        bottomPanel.add(sortRoomsBtn);
        bottomPanel.add(sortDateBtn);

        JSplitPane leftRightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        leftRightSplitPane.setDividerLocation(250);

        add(leftRightSplitPane, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addHouse());
        updateButton.addActionListener(e -> updateHouse());
        deleteButton.addActionListener(e -> deleteHouse());
        clearButton.addActionListener(e -> clearAll());
        refreshButton.addActionListener(e -> refreshTable());

        pack();
        setSize(1200, 800);
        setLocationRelativeTo(null);

        refreshTable();
    }

    private void setupListeners() {
        houseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && houseTable.getSelectedRow() != -1) {
                int selectedRow = houseTable.getSelectedRow();
                idSpinner.setValue(Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()));
                typeComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 1));
                areaSpinner.setValue(Double.parseDouble(tableModel.getValueAt(selectedRow, 2).toString()));
                roomsSpinner.setValue(Integer.parseInt(tableModel.getValueAt(selectedRow, 3).toString()));
                priceSpinner.setValue(Double.parseDouble(tableModel.getValueAt(selectedRow, 5).toString()));
            }
        });
    }

    private void addHouse() {
        try {
            int id = (Integer) idSpinner.getValue();
            String type = (String) typeComboBox.getSelectedItem();
            double area = (Double) areaSpinner.getValue();
            int rooms = (Integer) roomsSpinner.getValue();
            double price = (Double) priceSpinner.getValue();

            if (storage.getHouseById(id) != null) {
                logMessage("Error: House with ID " + id + " already exists");
                return;
            }

            HouseBuilder houseBuilder = new ConcreteHouseBuilder();
            House house = houseBuilder
                    .setId(id)
                    .setType(type)
                    .setArea(area)
                    .setRooms(rooms)
                    .setDate(new Date())
                    .setPrice(price)
                    .build();

            storage.addHouse(house);
            refreshTable();
            logMessage("House added: ID=" + id + ", Type=" + type);

        } catch (Exception e) {
            logMessage("Error adding house: " + e.getMessage());
        }
    }

    private void updateHouse() {
        int selectedRow = houseTable.getSelectedRow();
        if (selectedRow == -1) {
            logMessage("Please select a house to update");
            return;
        }

        try {
            int id = (Integer) idSpinner.getValue();
            String type = (String) typeComboBox.getSelectedItem();
            double area = (Double) areaSpinner.getValue();
            int rooms = (Integer) roomsSpinner.getValue();
            double price = (Double) priceSpinner.getValue();

            House existingHouse = storage.getHouseById(id);
            Date date = existingHouse != null ? existingHouse.getDate() : new Date();

            HouseBuilder houseBuilder = new ConcreteHouseBuilder();
            House updatedHouse = houseBuilder
                    .setId(id)
                    .setType(type)
                    .setArea(area)
                    .setRooms(rooms)
                    .setDate(date)
                    .setPrice(price)
                    .build();

            storage.updateHouse(id, updatedHouse);
            refreshTable();
            logMessage("House updated: ID=" + id);

        } catch (Exception e) {
            logMessage("Error updating house: " + e.getMessage());
        }
    }

    private void deleteHouse() {
        int selectedRow = houseTable.getSelectedRow();
        if (selectedRow == -1) {
            logMessage("Please select a house to delete");
            return;
        }

        try {
            int id = (Integer) tableModel.getValueAt(selectedRow, 0);
            storage.deleteHouse(id);
            refreshTable();
            logMessage("House deleted: ID=" + id);

        } catch (Exception e) {
            logMessage("Error deleting house: " + e.getMessage());
        }
    }

    private void clearAll() {
        if (JOptionPane.showConfirmDialog(this, "Clear all houses?", "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            storage = new HouseMapStorage();
            file = new HouseFile(storage);
            refreshTable();
            logMessage("All houses cleared");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<House> houses = storage.getAllHouses();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        for (House house : houses) {
            tableModel.addRow(new Object[]{
                    house.getId(),
                    house.getType(),
                    String.format("%.1f", house.getArea()),
                    house.getRooms(),
                    sdf.format(house.getDate()),
                    String.format("%.2f", house.getPrice())
            });
        }

        logMessage("Table refreshed. Total houses: " + houses.size());
    }

    private void sortHouses(int criteria) {
        if (storage instanceof HouseMapStorage) {
            HouseMapStorage mapStorage = (HouseMapStorage) storage;
            Comparator<House> comparator = switch (criteria) {
                case 1 -> Comparator.comparingDouble(House::getPrice);
                case 2 -> Comparator.comparingDouble(House::getArea);
                case 3 -> Comparator.comparingInt(House::getRooms);
                case 4 -> Comparator.comparing(House::getDate);
                default -> null;
            };

            if (comparator != null) {
                List<House> sorted = new ArrayList<>(mapStorage.getHousesMap().values());
                sorted.sort(comparator);
                mapStorage.getHousesMap().clear();
                for (House h : sorted) {
                    mapStorage.getHousesMap().put(h.getId(), h);
                }
                refreshTable();
                logMessage("Sorted by " +
                        switch(criteria) {
                            case 1 -> "Price";
                            case 2 -> "Area";
                            case 3 -> "Rooms";
                            case 4 -> "Date";
                            default -> "Unknown";
                        });
            }
        }
    }

    private void loadFromFile() {
        new Thread(() -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getPath();
                logMessage("Loading from file: " + filename + " (in background thread)");
                file.readFromFile(filename);
                SwingUtilities.invokeLater(this::refreshTable);
            }
        }).start();
    }

    private void loadFromXML() {
        new Thread(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File f) {
                    return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
                }
                public String getDescription() {
                    return "XML Files (*.xml)";
                }
            });
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getPath();
                logMessage("Loading from XML: " + filename + " (in background thread)");
                file.readFromXML(filename);
                SwingUtilities.invokeLater(this::refreshTable);
            }
        }).start();
    }

    private void loadFromJSON() {
        new Thread(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File f) {
                    return f.getName().toLowerCase().endsWith(".json") || f.isDirectory();
                }
                public String getDescription() {
                    return "JSON Files (*.json)";
                }
            });
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getPath();
                logMessage("Loading from JSON: " + filename + " (in background thread)");
                file.readFromJSON(filename);
                SwingUtilities.invokeLater(this::refreshTable);
            }
        }).start();
    }

    private void loadFromEncrypted() {
        new Thread(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File f) {
                    return f.getName().toLowerCase().endsWith(".enc") || f.isDirectory();
                }
                public String getDescription() {
                    return "Encrypted Files (*.enc)";
                }
            });
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getPath();
                logMessage("Loading encrypted file: " + filename + " (in background thread)");
                file.encrReadFromFile(filename);
                SwingUtilities.invokeLater(this::refreshTable);
            }
        }).start();
    }

    private void saveToFile() {
        new Thread(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("houses.csv"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getPath();
                logMessage("Saving to file: " + filename + " (in background thread)");
                file.writeToFile(filename);
                logMessage("File saved successfully");
            }
        }).start();
    }

    private void saveToXML() {
        new Thread(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("houses.xml"));
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File f) {
                    return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
                }
                public String getDescription() {
                    return "XML Files (*.xml)";
                }
            });
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getPath();
                logMessage("Saving to XML: " + filename + " (in background thread)");
                file.writeToXML(filename);
                logMessage("XML file saved successfully");
            }
        }).start();
    }

    private void saveToJSON() {
        new Thread(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("houses.json"));
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(java.io.File f) {
                    return f.getName().toLowerCase().endsWith(".json") || f.isDirectory();
                }
                public String getDescription() {
                    return "JSON Files (*.json)";
                }
            });
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getPath();
                logMessage("Saving to JSON: " + filename + " (in background thread)");
                file.writeToJSON(filename);
                logMessage("JSON file saved successfully");
            }
        }).start();
    }

    private void saveToEncrypted() {
        new Thread(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("houses.enc"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getPath();
                logMessage("Saving encrypted: " + filename + " (in background thread)");
                file.encrWriteToFile(filename);
                logMessage("Encrypted file saved successfully");
            }
        }).start();
    }

    private void saveToEncryptedXML() {
        new Thread(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("houses_enc.xml"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getPath();
                logMessage("Saving encrypted XML: " + filename + " (in background thread)");
                file.writeToEncryptedXML(filename);
                logMessage("Encrypted XML saved successfully");
            }
        }).start();
    }

    private void saveToEncryptedJSON() {
        new Thread(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("houses_enc.json"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getPath();
                logMessage("Saving encrypted JSON: " + filename + " (in background thread)");
                file.writeToEncryptedJSON(filename);
                logMessage("Encrypted JSON saved successfully");
            }
        }).start();
    }

    private void saveAllFormats() {
        new Thread(() -> {
            logMessage("Starting parallel save to all formats...");

            Thread csvThread = new Thread(() -> {
                file.writeToFile("parallel_houses.csv");
                logMessage("CSV saved in thread: " + Thread.currentThread().getName());
            });

            Thread xmlThread = new Thread(() -> {
                file.writeToXML("parallel_houses.xml");
                logMessage("XML saved in thread: " + Thread.currentThread().getName());
            });

            Thread jsonThread = new Thread(() -> {
                file.writeToJSON("parallel_houses.json");
                logMessage("JSON saved in thread: " + Thread.currentThread().getName());
            });

            csvThread.start();
            xmlThread.start();
            jsonThread.start();

            try {
                csvThread.join();
                xmlThread.join();
                jsonThread.join();
                logMessage("All formats saved successfully in parallel threads!");
            } catch (InterruptedException e) {
                logMessage("Parallel save interrupted: " + e.getMessage());
            }
        }).start();
    }

    private void zipFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String sourceFile = fileChooser.getSelectedFile().getPath();
            String zipName = sourceFile + ".zip";
            new Thread(() -> {
                logMessage("Zipping file: " + sourceFile + " (in background thread)");
                Archiving.zipFile(sourceFile, zipName);
                logMessage("File zipped successfully: " + zipName);
            }).start();
        }
    }

    private void unzipFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File f) {
                return f.getName().toLowerCase().endsWith(".zip") || f.isDirectory();
            }
            public String getDescription() {
                return "ZIP Files (*.zip)";
            }
        });
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String zipFile = fileChooser.getSelectedFile().getPath();
            JFileChooser dirChooser = new JFileChooser();
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (dirChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String outputDir = dirChooser.getSelectedFile().getPath();
                new Thread(() -> {
                    logMessage("Unzipping: " + zipFile + " (in background thread)");
                    Archiving.unzipFile(zipFile, outputDir);
                    logMessage("File unzipped successfully to: " + outputDir);
                }).start();
            }
        }
    }

    private void logMessage(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + sdf.format(new Date()) + "] " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}