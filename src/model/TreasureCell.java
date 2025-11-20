package model;

import interfaces.Scoreable;

// TreasureCell implements Scoreable interface
// Demonstrates: Polymorphism (processReveal), Interface implementation
public class TreasureCell extends Cell implements Scoreable {
    private int points;
    private String image;
    private String name;

    public TreasureCell(int row, int column, String name, String image) {
        super(row, column);
        this.name = name;
        this.image = image;
        this.points = 200;
    }

    @Override
    public void processReveal(Game game) {
        if (!this.isRevealed()) {
            this.reveal();
            double multiplier = game.getSettings().getTreasuresMultiplier();
            int points = this.calculatePoints(multiplier);
            game.addScore(points);
            game.incrementTreasureFound();
        }
    }

    @Override
    public int calculatePoints(double multiplier) {
        return (int) (this.points * multiplier);
    }

    @Override
    public int getPoint() {
        return this.points;
    }

    public String getName() {
        return this.name;
    }

    public String getImage() {
        return this.image;
    }
}
