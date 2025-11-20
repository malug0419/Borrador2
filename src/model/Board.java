package model;

import exceptions.InvalidCoordinateException;
import java.util.Random;

/**
 * Representa el tablero del juego BuscaTesoros.
 * Contiene una cuadrícula de celdas que pueden ser vacías, trampas o tesoros.
 * Administra la lógica de inicialización, revelado y pistas del juego.
 */
public class Board {
    private int size;
    private Cell[][] cells;
    private int amountTreasures;
    private int amountTraps;
    private int revealedCells;
    private boolean cluesEnabled;
    private boolean autoRevealEnabled;

    public Board(int size, int treasures, int traps, boolean clues, boolean autoReveal) {
        this.size = size;
        this.cells = new Cell[size][size];
        this.amountTreasures = treasures;
        this.amountTraps = traps;
        this.cluesEnabled = clues;
        this.autoRevealEnabled = autoReveal;
        this.revealedCells = 0;
        this.initializeBoard();
    }

    public void initializeBoard() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.cells[i][j] = new EmptyCell(i, j);
            }
        }
        this.placeRandomElements();
        if (this.cluesEnabled) {
            this.calculateClues();
        }
    }

    public void placeRandomElements() {
        Random random = new Random();
        int placed = 0;

        while (placed < this.amountTreasures) {
            int row = random.nextInt(this.size);
            int col = random.nextInt(this.size);
            if (this.cells[row][col] instanceof EmptyCell && !(this.cells[row][col] instanceof TreasureCell)) {
                String[] treasureNames = {"Corona Dorada", "Esmeralda Antigua", "Diamante Real", "Oro Puro"};
                String treasureName = treasureNames[random.nextInt(treasureNames.length)];
                this.cells[row][col] = new TreasureCell(row, col, treasureName, "treasure.png");
                placed++;
            }
        }

        placed = 0;
        while (placed < this.amountTraps) {
            int row = random.nextInt(this.size);
            int col = random.nextInt(this.size);
            if (this.cells[row][col] instanceof EmptyCell && !(this.cells[row][col] instanceof TrapCell)) {
                String[] trapDescriptions = {"Pozo sin fondo", "Trampa explosiva", "Lava hirviendo"};
                String trapDesc = trapDescriptions[random.nextInt(trapDescriptions.length)];
                this.cells[row][col] = new TrapCell(row, col, trapDesc, random.nextInt(3) + 1);
                placed++;
            }
        }
    }

    public Cell revealCell(int row, int column) throws InvalidCoordinateException {
        if (row < 0 || row >= this.size || column < 0 || column >= this.size) {
            throw new InvalidCoordinateException("Coordenadas fuera del tablero");
        }
        Cell cell = this.cells[row][column];
        if (!cell.isRevealed()) {
            this.revealedCells++;
        }
        return cell;
    }

    public void revealCellsAround(int row, int column) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if (i >= 0 && i < this.size && j >= 0 && j < this.size) {
                    Cell cell = this.cells[i][j];
                    if (!cell.isRevealed()) {
                        cell.reveal();
                        this.revealedCells++;
                    }
                }
            }
        }
    }

    public void calculateClues() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.cells[i][j] instanceof EmptyCell && !(this.cells[i][j] instanceof TreasureCell)) {
                    EmptyCell emptyCell = (EmptyCell) this.cells[i][j];
                    int treasureCount = 0;
                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            int ni = i + di;
                            int nj = j + dj;
                            if (ni >= 0 && ni < this.size && nj >= 0 && nj < this.size) {
                                if (this.cells[ni][nj] instanceof TreasureCell) {
                                    treasureCount++;
                                }
                            }
                        }
                    }
                    emptyCell.setClue(treasureCount);
                }
            }
        }
    }

    public Cell getCell(int row, int column) throws InvalidCoordinateException {
        if (row < 0 || row >= this.size || column < 0 || column >= this.size) {
            throw new InvalidCoordinateException("Coordenadas fuera del tablero");
        }
        return this.cells[row][column];
    }

    public int getSize() {
        return this.size;
    }

    public int getAmountTreasures() {
        return this.amountTreasures;
    }

    public int getRevealedCells() {
        return this.revealedCells;
    }
}
