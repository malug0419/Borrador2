package model;

import enums.CellStatus;

// Abstract base class - enables polymorphism with subclasses
public abstract class Cell {
    protected CellStatus status;
    protected int row;
    protected int column;

    public Cell() {
        this.status = CellStatus.HIDDEN;
        this.row = 0;
        this.column = 0;
    }

    public Cell(int row, int column) {
        this.status = CellStatus.HIDDEN;
        this.row = row;
        this.column = column;
    }

    public void reveal() {
        this.status = CellStatus.REVEALED;
    }

    public boolean isRevealed() {
        return this.status == CellStatus.REVEALED;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public CellStatus getStatus() {
        return this.status;
    }

    // Polymorphic method - overridden by subclasses
    public abstract void processReveal(Game game);
}
