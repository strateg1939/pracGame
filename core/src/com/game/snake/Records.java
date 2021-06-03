package com.game.snake;

import java.io.*;
import java.util.ArrayList;

/**
 * class that loads / saves all records
 * ObjectInput/Output is used to load/save
 */
public class Records {
    private ArrayList<Record> records;
    private static final String recordsFileName = "saves.txt";
    public Records(){
        load();
    }
    public void load() {
        try {
            // Check if the saved file exist
            File file = new File(recordsFileName);
            if (file.exists()) {
                ObjectInputStream oos = new ObjectInputStream(new FileInputStream(recordsFileName));
                records = (ArrayList<Record>) oos.readObject();
                oos.close();
            } else {
                records = new ArrayList<>();
                save();
            }
        } catch (Exception e) {
            System.out.println("Something is wrong with load");
        }
    }

    /**
     * save all information
     */
    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(recordsFileName));
            oos.writeObject(records);
            oos.close();
        } catch (Exception e) {
            System.out.println("Something is wrong with save");
        }
    }
    public void add(Record record){
        records.add(record);
    }

    public ArrayList<Record> getRecords() {
        return records;
    }
}
