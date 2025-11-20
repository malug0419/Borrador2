package model;

import enums.LevelDifficulty;
import java.io.Serializable;

// Serializable - enables object persistence
public class RecordScore implements Serializable, Comparable<RecordScore> {
    private static final long serialVersionUID = 1L;
    private String nickName;
    private int score;
    private int remainingLifes;
    private LevelDifficulty level;

    public RecordScore(String nickName, int score, int remainingLifes, LevelDifficulty level) {
        this.nickName = nickName;
        this.score = score;
        this.remainingLifes = remainingLifes;
        this.level = level;
    }

    public int compareTo(RecordScore other) {
        if (this.score != other.score) {
            return other.score - this.score;
        }
        return other.remainingLifes - this.remainingLifes;
    }

    public String getNickName() {
        return this.nickName;
    }

    public int getScore() {
        return this.score;
    }

    public int getRemainingLifes() {
        return this.remainingLifes;
    }

    public LevelDifficulty getLevel() {
        return this.level;
    }
}
