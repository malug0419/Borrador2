package model;

import interfaces.Scoreable;

// EmptyCell implements Scoreable - demonstrates interface implementation
// Demonstrates: Polymorphism (processReveal), Interface implementation
public class EmptyCell extends Cell implements Scoreable {
    private int clue;

    public EmptyCell(int row, int column) {
        super(row, column);
        this.clue = 0;
    }

    @Override
    public void processReveal(Game game) {
        if (!this.isRevealed()) {
            this.reveal();
            // Auto-reveal logic can be handled here
            if (game.getSettings().isAutoRevealEnabled() && this.clue == 0) {
                game.getBoard().revealCellsAround(this.row, this.column);
            }
        }
    }

    @Override
    public int calculatePoints(double multiplier) {
        return 0;
    }

    @Override
    public int getPoint() {
        return 0;
    }

    public int getClue() {
        return this.clue;
    }

    public void setClue(int clue) {
        this.clue = clue;
    }
}
