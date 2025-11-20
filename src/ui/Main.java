package ui;

import enums.GameStatus;
import enums.LevelDifficulty;
import exceptions.InvalidCoordinateException;
import exceptions.InvalidGameSettingsException;
import java.util.List;
import java.util.Scanner;
import model.Board;
import model.BuscaTesorosController;
import model.Cell;
import model.EmptyCell;
import model.Game;
import model.RecordScore;
import model.Setting;
import model.TrapCell;
import model.TreasureCell;


public class Main {
    private static BuscaTesorosController controller;
    private static Scanner scanner;

    public static void main(String[] args) {
        controller = new BuscaTesorosController();
        scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMainMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    startNewGame();
                    break;
                case 2:
                    showTopTen();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        }
        scanner.close();
        System.out.println("Saliendo del juego...");
    }

    private static void displayMainMenu() {
        System.out.println("\n=== BuscaTesoros ===");
        System.out.println("1. Iniciar nuevo juego");
        System.out.println("2. Ver Top 10");
        System.out.println("3. Salir");
        System.out.print("Seleccione opcion: ");
    }

    private static void startNewGame() {
        System.out.println("\n=== Seleccionar Dificultad ===");
        System.out.println("1. Facil");
        System.out.println("2. Medio");
        System.out.println("3. Dificil");
        System.out.println("4. Experto");
        System.out.println("5. Personalizado");
        System.out.print("Seleccione dificultad: ");

        int diffChoice = getIntInput();
        Setting settings = null;

        try {
            switch (diffChoice) {
                case 1:
                    settings = new Setting(LevelDifficulty.EASY);
                    break;
                case 2:
                    settings = new Setting(LevelDifficulty.MEDIUM);
                    break;
                case 3:
                    settings = new Setting(LevelDifficulty.DIFFICULT);
                    break;
                case 4:
                    settings = new Setting(LevelDifficulty.EXPERT);
                    break;
                case 5:
                    settings = createCustomSettings();
                    break;
                default:
                    System.out.println("Opcion invalida");
                    return;
            }

            if (settings != null && settings.validateCustomSettings()) {
                System.out.print("Ingrese su apodo: ");
                String nickname = scanner.nextLine().trim();
                if (nickname.isEmpty()) {
                    nickname = "Jugador";
                }

                controller.startNewGame(settings, nickname);
                playGame();
            } else if (settings == null) {
                System.out.println("Error: Configuracion invalida");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static Setting createCustomSettings() throws InvalidGameSettingsException {
        System.out.println("\n=== Configuracion Personalizada ===");
        System.out.print("Tamano del tablero (8-20): ");
        int boardSize = getIntInput();

        System.out.print("Cantidad de tesoros: ");
        int treasures = getIntInput();

        System.out.print("Cantidad de trampas: ");
        int traps = getIntInput();

        System.out.print("Vidas iniciales: ");
        int lifes = getIntInput();

        System.out.print("Activar pistas (1=si, 0=no): ");
        boolean clues = getIntInput() == 1;

        System.out.print("Activar revelacion automatica (1=si, 0=no): ");
        boolean autoReveal = getIntInput() == 1;

        System.out.print("Multiplicador de tesoros: ");
        double treasureMultiplier = getDoubleInput();

        System.out.print("Multiplicador de vidas: ");
        double lifeMultiplier = getDoubleInput();

        System.out.print("Bonus por partida perfecta: ");
        int perfectBonus = getIntInput();

        Setting settings = new Setting(boardSize, treasures, traps, lifes, clues, autoReveal, treasureMultiplier, lifeMultiplier, perfectBonus);

        if (!settings.validateCustomSettings()) {
            throw new InvalidGameSettingsException("Configuracion invalida: Verifica el tamano del tablero y cantidad de elementos");
        }

        return settings;
    }

    private static void playGame() {
        Game game = controller.getGame();
        Board board = controller.getBoard();

        if (game == null || board == null) {
            System.out.println("Error: Juego no inicializado");
            return;
        }

        System.out.println("\nTablero " + board.getSize() + "x" + board.getSize() + " Iniciado!");
        System.out.println("Tesoros por encontrar: " + game.getSettings().getAmountTreasures());
        System.out.println("Vidas: " + game.getLifes());

        while (game.getStatus() == GameStatus.IN_PROGRESS) {
            try {
                displayBoard(board);
                System.out.print("Ingrese fila (0-" + (board.getSize() - 1) + "): ");
                int row = getIntInput();

                System.out.print("Ingrese columna (0-" + (board.getSize() - 1) + "): ");
                int column = getIntInput();

                controller.selectCell(row, column);
                Cell cell = board.getCell(row, column);

                // Polymorphism and downcasting demonstration
                if (cell instanceof TreasureCell) {
                    // Downcasting - converting from Cell to TreasureCell
                    TreasureCell treasure = (TreasureCell) cell;
                    System.out.println("Tesoro encontrado: " + treasure.getName() + "!");
                } else if (cell instanceof TrapCell) {
                    // Downcasting - converting from Cell to TrapCell
                    TrapCell trap = (TrapCell) cell;
                    System.out.println("Trampa activada: " + trap.getDescription());
                } else if (cell instanceof EmptyCell) {
                    // Downcasting - converting from Cell to EmptyCell
                    EmptyCell empty = (EmptyCell) cell;
                    System.out.println("Celda vacia. Pista: " + empty.getClue());
                }

                System.out.println("Vidas: " + game.getLifes() + ", Puntuacion: " + game.getTotalScore());
            } catch (InvalidCoordinateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        if (game.getStatus() == GameStatus.VICTORY) {
            System.out.println("\nVictoria! Encontraste todos los tesoros!");
        } else {
            System.out.println("\nDerrota! Se acabaron las vidas.");
        }

        int finalScore = controller.calculateFinalScore();
        System.out.println("Puntuacion Final: " + finalScore);

        if (controller.canEnterTopTen(finalScore, game.getLifes())) {
            System.out.print("Ingrese su nombre para el Top 10: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                try {
                    controller.registerScore(name);
                    System.out.println("Puntuacion registrada en el Top 10!");
                } catch (InvalidGameSettingsException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private static void displayBoard(Board board) {
        System.out.println("\nTablero:");
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                try {
                    Cell cell = board.getCell(i, j);
                    if (cell.isRevealed()) {
                        if (cell instanceof TreasureCell) {
                            System.out.print("[T] ");
                        } else if (cell instanceof TrapCell) {
                            System.out.print("[X] ");
                        } else if (cell instanceof EmptyCell) {
                            EmptyCell empty = (EmptyCell) cell;
                            System.out.print("[" + empty.getClue() + "] ");
                        }
                    } else {
                        System.out.print("[?] ");
                    }
                } catch (InvalidCoordinateException e) {
                    System.out.print("[E] ");
                }
            }
            System.out.println();
        }
    }

    private static void showTopTen() {
        List<RecordScore> records = controller.getTopTenRecords();

        System.out.println("\n=== Top 10 Puntajes ===");
        if (records.isEmpty()) {
            System.out.println("No hay puntajes registrados");
        } else {
            for (int i = 0; i < records.size(); i++) {
                RecordScore record = records.get(i);
                System.out.println((i + 1) + ". " + record.getNickName() + " - Puntuacion: " + record.getScore() + " - Vidas: " + record.getRemainingLifes() + " - Nivel: " + record.getLevel());
            }
        }
    }

    private static int getIntInput() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        } finally {
            scanner.nextLine();
        }
    }

    private static double getDoubleInput() {
        try {
            return scanner.nextDouble();
        } catch (Exception e) {
            scanner.nextLine();
            return 0.0;
        } finally {
            scanner.nextLine();
        }
    }
}