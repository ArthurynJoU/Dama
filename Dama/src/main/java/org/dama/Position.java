package org.dama;

import java.util.Objects;

/**
 * Pozícia s dvoma súradnicami (row, col).
 * row=0 = horný okraj, row=7 = spodný okraj,
 * col=0 = stĺpec A, col=7 = stĺpec H.
 */
public class Position {
    public final int row;
    public final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return row == p.row && col == p.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Pre výpis do reťazca (napr. "A3"),
     * kde col=0 -> 'A', row=0 -> 8.
     */
    @Override
    public String toString() {
        char c = (char) ('A' + col);
        int r = 8 - row;
        return "" + c + r;
    }
}
