package io.github.rafaelcsampaio.hanoi.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HanoiGameTest {

    @Test
    void shouldInitializeGameWithAllDisksOnStartTower() {
        HanoiGame game = new HanoiGame(3);

        assertEquals(List.of(3, 2, 1), game.getTowerSnapshot(0));
        assertTrue(game.getTowerSnapshot(1).isEmpty());
        assertTrue(game.getTowerSnapshot(2).isEmpty());
    }

    @Test
    void shouldMoveDiskToAnotherTower() {
        HanoiGame game = new HanoiGame(3);

        MoveResult result = game.moveDisk(0, 2);

        assertEquals(MoveResult.SUCCESS, result);
        assertEquals(List.of(3, 2), game.getTowerSnapshot(0));
        assertEquals(List.of(1), game.getTowerSnapshot(2));
    }

    @Test
    void shouldRejectLargerDiskOnSmallerDisk() {
        HanoiGame game = new HanoiGame(3);

        game.moveDisk(0, 2);
        game.moveDisk(0, 1);

        MoveResult result = game.moveDisk(1, 2);

        assertEquals(MoveResult.LARGER_DISK_ON_SMALLER, result);
        assertEquals(List.of(2), game.getTowerSnapshot(1));
        assertEquals(List.of(1), game.getTowerSnapshot(2));

        GameStats stats = game.getStats();

        assertEquals(2, stats.validMoves());
        assertEquals(1, stats.invalidMoves());
    }

    @Test
    void shouldNotCountSameTowerAsInvalidMove() {
        HanoiGame game = new HanoiGame(3);

        MoveResult result = game.moveDisk(0, 0);

        assertEquals(MoveResult.SAME_TOWER, result);
        assertEquals(0, game.getStats().validMoves());
        assertEquals(0, game.getStats().invalidMoves());
    }

    @Test
    void shouldRejectInvalidDiskCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new HanoiGame(7)
        );
    }

    @Test
    void shouldNotCountEmptySourceAsInvalidMove() {
        HanoiGame game = new HanoiGame(3);

        MoveResult result = game.moveDisk(1, 2);

        assertEquals(MoveResult.EMPTY_SOURCE, result);
        assertEquals(0, game.getStats().validMoves());
        assertEquals(0, game.getStats().invalidMoves());
    }

    @Test
    void shouldDetectVictoryOnlyOnGoalTower() {
        HanoiGame game = new HanoiGame(2);

        game.moveDisk(0, 1);
        game.moveDisk(0, 2);
        game.moveDisk(1, 2);

        assertTrue(game.isSolved());
    }

    @Test
    void shouldCalculateGameStats() {
        HanoiGame game = new HanoiGame(3);

        game.moveDisk(0, 2);
        game.moveDisk(0, 1);
        game.moveDisk(1, 2);

        GameStats stats = game.getStats();

        assertEquals(2, stats.validMoves());
        assertEquals(1, stats.invalidMoves());
        assertEquals(7, stats.minimumMoves());
        assertEquals(3, stats.totalAttempts());
        assertEquals(0, stats.extraMoves());
    }

    @Test
    void shouldResetGameAndStatistics() {
        HanoiGame game = new HanoiGame(3);

        game.moveDisk(0, 2);
        game.moveDisk(0, 1);

        game.reset();

        assertEquals(List.of(3, 2, 1), game.getTowerSnapshot(0));
        assertTrue(game.getTowerSnapshot(1).isEmpty());
        assertTrue(game.getTowerSnapshot(2).isEmpty());

        assertEquals(0, game.getStats().validMoves());
        assertEquals(0, game.getStats().invalidMoves());
        assertFalse(game.isSolved());
    }

    @Test
    void shouldRejectInvalidTowerIndex() {
        HanoiGame game = new HanoiGame(3);

        assertThrows(
                IllegalArgumentException.class,
                () -> game.moveDisk(-1, 2)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> game.getTowerSnapshot(3)
        );
    }
}