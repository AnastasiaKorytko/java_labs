package org.example;

import javax.swing.*;
import java.io.File;

public class ParallelFileWorker extends Thread {
    private HouseFile file;
    private String filename;
    private String format;
    private JTextArea logArea;

    public ParallelFileWorker(HouseFile file, String filename, String format, JTextArea logArea) {
        this.file = file;
        this.filename = filename;
        this.format = format;
        this.logArea = logArea;
        setName("Parallel-" + format + "-Worker");
    }

    @Override
    public void run() {
        log("Starting " + format + " export in thread: " + getName());

        try {
            switch (format) {
                case "CSV":
                    file.writeToFile(filename);
                    break;
                case "XML":
                    file.writeToXML(filename);
                    break;
                case "JSON":
                    file.writeToJSON(filename);
                    break;
                case "ENCRYPTED":
                    file.encrWriteToFile(filename);
                    break;
                case "ENCRYPTED_XML":
                    file.writeToEncryptedXML(filename);
                    break;
                case "ENCRYPTED_JSON":
                    file.writeToEncryptedJSON(filename);
                    break;
            }

            File savedFile = new File(filename);
            log(format + " file saved successfully: " + savedFile.getAbsolutePath() +
                    " (" + savedFile.length() + " bytes)");

        } catch (Exception e) {
            log("Error in " + format + " export: " + e.getMessage());
        }
    }

    private void log(String message) {
        if (logArea != null) {
            SwingUtilities.invokeLater(() -> {
                logArea.append("[Parallel] " + message + "\n");
            });
        }
        System.out.println("[" + getName() + "] " + message);
    }
}
