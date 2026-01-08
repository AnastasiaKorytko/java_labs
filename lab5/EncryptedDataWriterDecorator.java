package org.example;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;

public class EncryptedDataWriterDecorator extends DataWriterDecorator {

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1234567890abcdef";

    public EncryptedDataWriterDecorator(DataWriter wrappee) {
        super(wrappee);
    }

    @Override
    public void write(List<House> houses, String filename) {
        if (!filename.toLowerCase().endsWith(".enc")) {
            filename = filename + ".enc";
        }

        writeEncryptedData(houses, filename);
    }

    private void writeEncryptedData(List<House> houses, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            StringBuilder sb = new StringBuilder();
            for (House house : houses) {
                sb.append(String.format("%d,%s,%.1f,%d,%s,%.2f%n",
                        house.getId(),
                        house.getType(),
                        house.getArea(),
                        house.getRooms(),
                        sdf.format(house.getDate()),
                        house.getPrice()));
            }

            String encryptedData = encrypt(sb.toString());
            pw.println(encryptedData);

        } catch (Exception e) {
            System.out.println("Error writing encrypted file: " + e.getMessage());
        }
    }

    private String encrypt(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Encryption";
    }
}