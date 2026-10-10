package io.github.rafaelcsampaio.hanoi.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class HanoiGame {

    private static final int TOWER_COUNT = 3;
    private static final int MIN_DISKS = 1;
    private static final int MAX_DISKS = 6;

    private final int diskCount;
    private final List<Deque<Integer>> towers;

    private int validMoves;
    private int invalidMoves;

    public HanoiGame(int diskCount) {
        validateDiskCount(diskCount);

        this.diskCount = diskCount;
        this.towers = new ArrayList<>(TOWER_COUNT);

        for (int i = 0; i < TOWER_COUNT; i++) {
            towers.add(new ArrayDeque<>());
        }

        reset();
    }

    public void reset() {
        for (Deque<Integer> tower : towers) {
            tower.clear();
        }

        for (int disk = diskCount; disk >= 1; disk--) {
            towers.get(0).addLast(disk);
        }

        validMoves = 0;
        invalidMoves = 0;
    }

    public int getDiskCount() {
        return diskCount;
    }

    public int getTowerCount() {
        return TOWER_COUNT;
    }

    public List<Integer> getTowerSnapshot(int towerIndex) {
        validateTowerIndex(towerIndex);
        return List.copyOf(towers.get(towerIndex));
    }

    public MoveResult moveDisk(int sourceTower, int targetTower) {
        validateTowerIndex(sourceTower);
        validateTowerIndex(targetTower);

        if (sourceTower == targetTower) {
            return MoveResult.SAME_TOWER;
        }

        Deque<Integer> source = towers.get(sourceTower);
        Deque<Integer> target = towers.get(targetTower);

        if (source.isEmpty()) {
            return MoveResult.EMPTY_SOURCE;
        }

        int movingDisk = source.peekLast();

        if (!target.isEmpty() && movingDisk > target.peekLast()) {
            invalidMoves++;
            return MoveResult.LARGER_DISK_ON_SMALLER;
        }

        target.addLast(source.removeLast());
        validMoves++;

        return MoveResult.SUCCESS;
    }

    public boolean isSolved() {
        return towers.get(TOWER_COUNT - 1).size() == diskCount;
    }

    public GameStats getStats() {
        return new GameStats(
                validMoves,
                invalidMoves,
                calculateMinimumMoves()
        );
    }

    private int calculateMinimumMoves() {
        return (1 << diskCount) - 1;
    }

    private void validateDiskCount(int diskCount) {
        if (diskCount < MIN_DISKS || diskCount > MAX_DISKS) {
            throw new IllegalArgumentException(
                    "Disk count must be between "
                            + MIN_DISKS
                            + " and "
                            + MAX_DISKS
                            + "."
            );
        }
    }

    private void validateTowerIndex(int towerIndex) {
        if (towerIndex < 0 || towerIndex >= TOWER_COUNT) {
            throw new IllegalArgumentException(
                    "Tower index must be between 0 and "
                            + (TOWER_COUNT - 1)
                            + "."
            );
        }
    }
}