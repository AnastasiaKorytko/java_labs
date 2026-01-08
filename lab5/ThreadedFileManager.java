package org.example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadedFileManager {
    private ExecutorService executor;
    private List<Future<?>> futures;
    private JTextArea logArea;

    public ThreadedFileManager(JTextArea logArea) {
        this.executor = Executors.newFixedThreadPool(3); // 3 параллельных потока
        this.futures = new ArrayList<>();
        this.logArea = logArea;
    }

    public void submitExport(HouseFile file, String filename, String format) {
        Future<?> future = executor.submit(() -> {
            log("Starting export: " + format + " to " + filename);

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

                log("Export completed: " + format);

            } catch (Exception e) {
                log("Export error (" + format + "): " + e.getMessage());
            }
        });

        futures.add(future);
    }

    public void submitImport(HouseFile file, String filename, String format) {
        Future<?> future = executor.submit(() -> {
            log("Starting import: " + format + " from " + filename);

            try {
                switch (format) {
                    case "CSV":
                        file.readFromFile(filename);
                        break;
                    case "XML":
                        file.readFromXML(filename);
                        break;
                    case "JSON":
                        file.readFromJSON(filename);
                        break;
                    case "ENCRYPTED":
                        file.encrReadFromFile(filename);
                        break;
                }

                log("Import completed: " + format);

            } catch (Exception e) {
                log("Import error (" + format + "): " + e.getMessage());
            }
        });

        futures.add(future);
    }

    public void waitForCompletion() {
        for (Future<?> future : futures) {
            try {
                future.get(); // Ждем завершения
            } catch (InterruptedException | ExecutionException e) {
                log("Error waiting for completion: " + e.getMessage());
            }
        }
        futures.clear();
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    private void log(String message) {
        if (logArea != null) {
            SwingUtilities.invokeLater(() -> {
                logArea.append("[ThreadManager] " + message + "\n");
            });
        }
    }
}
