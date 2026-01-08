package org.example;
import java.util.List;

public interface DataWriter {
    void write(List<House> houses, String filename);
    String getDescription();
}
