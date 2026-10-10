package io.github.rafaelcsampaio.hanoi.game;

public record GameStats(
        int validMoves,
        int invalidMoves,
        int minimumMoves
) {

    public int totalAttempts() {
        return validMoves + invalidMoves;
    }

    public int extraMoves() {
        return Math.max(0, validMoves - minimumMoves);
    }
}