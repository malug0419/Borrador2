package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import enums.LevelDifficulty;

public class Setting {
    private LevelDifficulty level;
    private int boardSize;
    private int amountTreasures;
    private int amountTraps;
    private int initialLifes;
    private boolean cluesEnabled;
    private boolean autoRevealEnabled;
    private double treasuresMultiplier;
    private double lifeMultiplier;
    private int perfectBonus;

    public Setting(LevelDifficulty level) {
        this.level = level;
        this.loadFromFile(level);
    }

    public Setting(int boardSize, int treasures, int traps, int lifes, boolean clues, boolean autoReveal, double treasureMultiplier, double lifeMultiplier, int perfectBonus) {
        this.level = LevelDifficulty.PERSONALIZED;
        this.boardSize = boardSize;
        this.amountTreasures = treasures;
        this.amountTraps = traps;
        this.initialLifes = lifes;
        this.cluesEnabled = clues;
        this.autoRevealEnabled = autoReveal;
        this.treasuresMultiplier = treasureMultiplier;
        this.lifeMultiplier = lifeMultiplier;
        this.perfectBonus = perfectBonus;
    }

    public void loadFromFile(LevelDifficulty level) {
        try (BufferedReader reader = new BufferedReader(new FileReader("settings.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(level.toString())) {
                    String[] parts = line.split(",");
                    if (parts.length >= 9) {
                        this.boardSize = Integer.parseInt(parts[1].trim());
                        this.amountTreasures = Integer.parseInt(parts[2].trim());
                        this.amountTraps = Integer.parseInt(parts[3].trim());
                        this.initialLifes = Integer.parseInt(parts[4].trim());
                        this.cluesEnabled = Boolean.parseBoolean(parts[5].trim());
                        this.autoRevealEnabled = Boolean.parseBoolean(parts[6].trim());
                        this.treasuresMultiplier = Double.parseDouble(parts[7].trim());
                        this.lifeMultiplier = Double.parseDouble(parts[8].trim());
                        this.perfectBonus = Integer.parseInt(parts[9].trim());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar configuracion: " + e.getMessage());
        }
    }

    public boolean validateCustomSettings() {
        if (this.boardSize < 8 || this.boardSize > 20) {
            return false;
        }
        int maxCapacity = this.boardSize * this.boardSize;
        if (this.amountTreasures + this.amountTraps > maxCapacity) {
            return false;
        }
        if (this.initialLifes <= 0) {
            return false;
        }
        return true;
    }

    public LevelDifficulty getLevel() {
        return this.level;
    }

    public int getBoardSize() {
        return this.boardSize;
    }

    public int getAmountTreasures() {
        return this.amountTreasures;
    }

    public int getAmountTraps() {
        return this.amountTraps;
    }

    public int getInitialLifes() {
        return this.initialLifes;
    }

    public boolean isClueEnabled() {
        return this.cluesEnabled;
    }

    public boolean isAutoRevealEnabled() {
        return this.autoRevealEnabled;
    }

    public double getTreasuresMultiplier() {
        return this.treasuresMultiplier;
    }

    public double getLifesMultiplier() {
        return this.lifeMultiplier;
    }

    public int getPerfectBonus() {
        return this.perfectBonus;
    }
}
