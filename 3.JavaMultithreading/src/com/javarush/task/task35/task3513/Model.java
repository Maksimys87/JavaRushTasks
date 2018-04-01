package com.javarush.task.task35.task3513;

import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;
    int score;
    int maxTile;

    public Model() {
       resetGameTiles();
    }

    void resetGameTiles() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_WIDTH; y++) {
                gameTiles[x][y] = new Tile();
            }
        }
        addTile();
        addTile();
        score = 0;
        maxTile = 2;
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> list = new ArrayList<>();
        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_WIDTH; y++) {
                if (gameTiles[x][y].isEmpty()) list.add(gameTiles[x][y]);
            }
        }
        return list;
    }

    private void addTile() {
        List<Tile> list = getEmptyTiles();
        if (list.size() != 0)
        list.get((int) (list.size()*Math.random())).value = Math.random() < 0.9 ? 2 : 4;
    }

    boolean canMove() {
        if (getEmptyTiles().size() != 0) return true;
        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 1; y < FIELD_WIDTH; y++) {
                if ((gameTiles[x][y].value == gameTiles[x][y-1].value) || (gameTiles[y][x].value == gameTiles[y-1][x].value))
                    return true;
            }
        }
        return false;
    }

    void left() {
        if (isSaveNeeded) saveState(gameTiles);
        boolean isChange = false;
        for (int x = 0; x < FIELD_WIDTH; x++) {
            if (compressTiles(gameTiles[x]) | mergeTiles(gameTiles[x]))
                isChange = true;
        }
        if (isChange) addTile();
        isSaveNeeded = true;
    }

    void right() {
        saveState(gameTiles);
        rotateClockWise();
        rotateClockWise();
        left();
        rotateCounterClockWise();
        rotateCounterClockWise();
    }

    void up() {
        saveState(gameTiles);
        rotateCounterClockWise();
        left();
        rotateClockWise();
    }

    void down() {
        saveState(gameTiles);
        rotateClockWise();
        left();
        rotateCounterClockWise();
    }

    private void rotateClockWise() {
        for (int x=0; x<FIELD_WIDTH/2; x++)
        {
            for (int y=x; y<FIELD_WIDTH-1-x; y++)
            {
                // меняем местами 4 угла
                Tile tmp = gameTiles[x][y];
                gameTiles[x][y] = gameTiles[FIELD_WIDTH-1-y][x];
                gameTiles[FIELD_WIDTH-1-y][x] = gameTiles[FIELD_WIDTH-1-x][FIELD_WIDTH-1-y];
                gameTiles[FIELD_WIDTH-1-x][FIELD_WIDTH-1-y] = gameTiles[y][FIELD_WIDTH-1-x];
                gameTiles[y][FIELD_WIDTH-1-x] = tmp;
            }
        }
    }

    private void rotateCounterClockWise() {
        for (int x=0; x<FIELD_WIDTH/2; x++)
        {
            for (int y=x; y<FIELD_WIDTH-1-x; y++)
            {
                // меняем местами 4 угла
                Tile tmp = gameTiles[x][y];
                gameTiles[x][y] = gameTiles[y][FIELD_WIDTH-1-x];
                gameTiles[y][FIELD_WIDTH-1-x] = gameTiles[FIELD_WIDTH-1-x][FIELD_WIDTH-1-y];
                gameTiles[FIELD_WIDTH-1-x][FIELD_WIDTH-1-y] = gameTiles[FIELD_WIDTH-1-y][x];
                gameTiles[FIELD_WIDTH-1-y][x] = tmp;
            }
        }
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean isCompression = false;
        for (int i = 0; i < FIELD_WIDTH-1; i++) {
            for (int j = 0; j < FIELD_WIDTH-1; j++) {
                if (tiles[j].isEmpty() && !tiles[j+1].isEmpty()) {
                    if (!isCompression)isCompression = true;
                    tiles[j].value = tiles[j+1].value;
                    tiles[j+1].value = 0;
                }
            }
        }
        return isCompression;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean isMerger = false;
        for (int i = 1; i < FIELD_WIDTH; i++) {
            if ((tiles[i-1].value != 0) && (tiles[i-1].value == tiles[i].value)) {
                tiles[i - 1].value *= 2;
                tiles[i].value = 0;
                if (!isMerger)isMerger = true;
                if (tiles[i - 1].value > maxTile) maxTile = tiles[i-1].value;
                score += tiles[i-1].value;
            }
        }
        compressTiles(tiles);
        return isMerger;
    }

    private void saveState(Tile[][] tiles) {
        Tile[][] tilesTemp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_WIDTH; y++) {
                tilesTemp[x][y] = new Tile(tiles[x][y].value);
            }
        }
        previousStates.push(tilesTemp);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (!previousStates.empty() && !previousScores.empty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    public void randomMove() {
        int n = ((int) (Math.random() * 100)) % 4;
        switch (n) {
            case 0 : left();
            break;
            case 1 : right();
            break;
            case 2 : up();
            break;
            case 3 : down();
            break;
        }
    }

    private boolean hasBoardChanged() {
        Tile[][] tempTiles = previousStates.peek();
        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_WIDTH; y++) {
                if (tempTiles[x][y].value != gameTiles[x][y].value) return true;
            }
        }
        return false;
    }

    private MoveEfficiency getMoveEfficiency(Move move) {
        move.move();
        if (hasBoardChanged()) {
            MoveEfficiency moveEfficiency = new MoveEfficiency(getEmptyTiles().size(),score,move);
            rollback();
            return moveEfficiency;
        }
        else {
            MoveEfficiency moveEfficiency = new MoveEfficiency(-1,0,move);
            rollback();
            return moveEfficiency;
        }
    }

    public void autoMove() {
        PriorityQueue<MoveEfficiency> priorityQueue = new PriorityQueue<>(4,Collections.reverseOrder());
        priorityQueue.offer(getMoveEfficiency(this::left));
        priorityQueue.offer(getMoveEfficiency(this::right));
        priorityQueue.offer(getMoveEfficiency(this::up));
        priorityQueue.offer(getMoveEfficiency(this::down));
        priorityQueue.peek().getMove().move();
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }
}
