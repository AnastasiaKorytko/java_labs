package org.example;

import java.util.List;

public abstract class DataWriterDecorator implements DataWriter {
    protected DataWriter wrappee;
    public DataWriterDecorator(DataWriter wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public void write(List<House> houses, String filename) {
        wrappee.write(houses, filename);
    }

    @Override
    public String getDescription() {
        return wrappee.getDescription();
    }
}
