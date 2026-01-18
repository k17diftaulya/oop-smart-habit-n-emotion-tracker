package com.habittracker.service;

import com.habittracker.model.AppData;
import java.io.*;

public class DataService {
    private static final String DATA_FILE = "appdata.ser";

    public void saveData(AppData data) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(data);
        }
    }

    public AppData loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return new AppData();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (AppData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new AppData();
        }
    }
}