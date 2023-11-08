package edu.ramapo.abhurtya.pente.model;

public class Computer extends Player {

    private Pair<Integer, Integer> location;

    public Computer() {
        this.location = new Pair<>(-1, -1);
    }

    @Override
    public void play(Board board, char symbol) {
        // Use the strategy function to get the best move
        Pair<Integer, Integer> bestMove = strategy(board, symbol);

        // Convert the numerical coordinates to a human-readable representation
        char column = (char)('A' + bestMove.getValue());
        int row = bestMove.getKey() + 1;

        System.out.println("Computer chose position: " + column + row);

        // Validation done by the strategy
        setLocation(bestMove.getKey(), bestMove.getValue());
    }

    public void setLocation(int x, int y) {
        this.location = new Pair<>(x, y);
    }

    public Pair<Integer, Integer> getLocation() {
        return location;
    }

    @Override
    public String getPlayerType() {
        return "Computer";
    }


}