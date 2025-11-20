package model;

// TrapCell extends Cell - demonstrates polymorphism
public class TrapCell extends Cell {
    private String description;
    private int severity;
    private int basePenalty;

    public TrapCell(int row, int column, String description, int severity) {
        super(row, column);
        this.description = description;
        this.severity = severity;
        this.basePenalty = 100;
    }

    @Override
    public void processReveal(Game game) {
        if (!this.isRevealed()) {
            this.reveal();
            double multiplier = game.getSettings().getLifesMultiplier();
            int penalty = this.calculatePenalty(multiplier);
            game.loseLife();
            game.addScore(-penalty);
            game.incrementTrapsActivated();
        }
    }

    public int calculatePenalty(double multiplier) {
        return (int) (this.basePenalty * multiplier);
    }

    public int getSeverity() {
        return this.severity;
    }

    public String getDescription() {
        return this.description;
    }
}
