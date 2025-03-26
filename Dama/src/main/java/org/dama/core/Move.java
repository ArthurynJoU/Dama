package org.dama.core;

public class Move {
    private final Position from;
    private final Position to;
    private final Position captured; // null, если не взятие

    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
        this.captured = null;
    }

    public Move(Position from, Position to, Position captured) {
        this.from = from;
        this.to = to;
        this.captured = captured;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Position getCapturedPosition() {
        return captured;
    }

    public boolean isCapture() {
        return captured != null;
    }

    @Override
    public String toString() {
        if (isCapture()) {
            return from + " -> " + to + " (capture: " + captured + ")";
        }
        return from + " -> " + to;
    }
}
