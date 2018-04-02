package com.javarush.task.task35.task3513;

public class MoveEfficiency implements Comparable<MoveEfficiency> {
    private int numberOfEmptyTiles;
    private int score;
    private Move move;

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public int compareTo(MoveEfficiency o) {
        if (numberOfEmptyTiles == o.numberOfEmptyTiles) {
            if (score == o.score) return 0;
            else return score > o.score ? 1 : -1;
        } else return numberOfEmptyTiles > o.numberOfEmptyTiles ? 1 : -1;
    }
}
