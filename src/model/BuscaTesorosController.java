package model;


import enums.GameStatus;
import exceptions.InvalidCoordinateException;
import exceptions.InvalidGameSettingsException;
import java.util.List;

// Main controller - demonstrates use of all classes
public class BuscaTesorosController {
    private Game game;
    private TopTen topTen;

    public BuscaTesorosController() {
        this.topTen = new TopTen();
    }

    public void startNewGame(Setting settings, String nickname) {
        this.game = new Game(settings, nickname);
        this.game.start();
    }

    public void selectCell(int row, int column) throws InvalidCoordinateException {
        if (this.game != null) {
            this.game.revealCell(row, column);
        }
    }

    public GameStatus checkGameStatus() {
        if (this.game != null) {
            return this.game.getStatus();
        }
        return null;
    }

    public int calculateFinalScore() {
        if (this.game != null) {
            int baseScore = this.game.getTotalScore();
            if (this.game.getLifes() > 0 && this.game.checkVictoryCondition()) {
                baseScore += this.game.getSettings().getPerfectBonus();
            }
            baseScore += (this.game.getLifes() * 20 * (int) this.game.getSettings().getLifesMultiplier());
            return baseScore;
        }
        return 0;
    }

    public void registerScore(String nickName) throws InvalidGameSettingsException {
        if (this.game == null) {
            throw new InvalidGameSettingsException("No hay juego en progreso");
        }
        int finalScore = this.calculateFinalScore();
        RecordScore record = new RecordScore(nickName, finalScore, this.game.getLifes(), this.game.getSettings().getLevel());
        if (this.topTen.isTopTenScores(finalScore, this.game.getLifes())) {
            this.topTen.addScore(record);
            this.topTen.saveFile();
        }
    }

    public boolean canEnterTopTen(int score, int remainingLifes) {
        return this.topTen.isTopTenScores(score, remainingLifes);
    }

    public List<RecordScore> getTopTenRecords() {
        return this.topTen.getRecords();
    }

    public Game getGame() {
        return this.game;
    }

    public Board getBoard() {
        if (this.game != null) {
            return this.game.getBoard();
        }
        return null;
    }
}
