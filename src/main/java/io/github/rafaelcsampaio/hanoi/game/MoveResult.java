package io.github.rafaelcsampaio.hanoi.game;

public enum MoveResult {
    SUCCESS,
    EMPTY_SOURCE,
    SAME_TOWER,
    LARGER_DISK_ON_SMALLER
}