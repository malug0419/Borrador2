package interfaces;

// CHANGE: Create Scoreable as an interface with two methods
// Interface to calculate and retrieve points - used for desacoplamiento
// Implemented by: TreasureCell, EmptyCell
public interface Scoreable {
    int getPoint();
    
    int calculatePoints(double multiplier);
}
