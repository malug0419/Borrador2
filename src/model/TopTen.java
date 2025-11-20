package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Serialization and Deserialization demo
public class TopTen {
    private List<RecordScore> record;
    private int maximumCapacity;

    public TopTen() {
        this.record = new ArrayList<RecordScore>();
        this.maximumCapacity = 10;
        this.loadFromFile();
    }

    public TopTen(List<RecordScore> record) {
        this.record = record;
        this.maximumCapacity = 10;
    }

    public boolean addScore(RecordScore newRecord) {
        if (this.record.size() < this.maximumCapacity) {
            this.record.add(newRecord);
            Collections.sort(this.record);
            return true;
        } else if (newRecord.compareTo(this.record.get(this.maximumCapacity - 1)) < 0) {
            this.record.remove(this.maximumCapacity - 1);
            this.record.add(newRecord);
            Collections.sort(this.record);
            return true;
        }
        return false;
    }

    public boolean isTopTenScores(int score, int remainingLifes) {
        if (this.record.size() < this.maximumCapacity) {
            return true;
        }
        RecordScore last = this.record.get(this.maximumCapacity - 1);
        if (score > last.getScore()) {
            return true;
        }
        if (score == last.getScore() && remainingLifes > last.getRemainingLifes()) {
            return true;
        }
        return false;
    }

    public List<RecordScore> getRecords() {
        return this.record;
    }

    public void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("topten.dat"))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList) {
                this.record = (ArrayList<RecordScore>) obj;
            }
        } catch (Exception e) {
            this.record = new ArrayList<RecordScore>();
        }
    }

    public void saveFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("topten.dat"))) {
            oos.writeObject(this.record);
        } catch (Exception e) {
            System.out.println("Error al guardar Top Ten: " + e.getMessage());
        }
    }
}
