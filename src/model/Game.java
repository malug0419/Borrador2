package model;

import enums.GameStatus;
import exceptions.InvalidCoordinateException;

public class Game {
    private Board board;
    private String nickname;
    private Setting settings;
    private GameStatus status;
    private int totalScore;
    private int lifes;
    private int foundTreasures;
    private int trapActivated;

    public Game(Setting settings, String nickname) {
        this.settings = settings;
        this.nickname = nickname;
        this.board = new Board(settings.getBoardSize(), settings.getAmountTreasures(), settings.getAmountTraps(), settings.isClueEnabled(), settings.isAutoRevealEnabled());
        this.totalScore = 0;
        this.lifes = settings.getInitialLifes();
        this.foundTreasures = 0;
        this.trapActivated = 0;
        this.status = GameStatus.IN_PROGRESS;
    }

    public void start() {
        this.status = GameStatus.IN_PROGRESS;
    }

    // Polymorphism in action - processReveal is overridden in Cell subclasses
    public void revealCell(int row, int column) throws InvalidCoordinateException {
        Cell cell = this.board.revealCell(row, column);
        cell.processReveal(this);
        this.updateGameStatus();
    }

    public void addScore(int points) {
        this.totalScore += points;
    }

    public void loseLife() {
        this.lifes--;
    }

    public void incrementTreasureFound() {
        this.foundTreasures++;
    }

    public void incrementTrapsActivated() {
        this.trapActivated++;
    }

    public boolean checkVictoryCondition() {
        return this.foundTreasures == this.settings.getAmountTreasures();
    }

    public boolean checkDefeatCondition() {
        return this.lifes <= 0;
    }

    public void updateGameStatus() {
        if (this.checkVictoryCondition()) {
            this.status = GameStatus.VICTORY;
        } else if (this.checkDefeatCondition()) {
            this.status = GameStatus.DEFEAT;
        }
    }

    public String getNickname() {
        return this.nickname;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public int getTotalScore() {
        return this.totalScore;
    }

    public int getLifes() {
        return this.lifes;
    }

    public int getFoundTreasures() {
        return this.foundTreasures;
    }

    public int getTrapsActivated() {
        return this.trapActivated;
    }

    public Board getBoard() {
        return this.board;
    }

    public Setting getSettings() {
        return this.settings;
    }
}
