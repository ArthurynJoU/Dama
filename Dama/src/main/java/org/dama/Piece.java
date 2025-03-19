package org.dama;

/**
 * Jednoduchá reprezentácia figúrky:
 * - vlastník (biely alebo čierny),
 * - typ (MAN alebo KING).
 */
public class Piece {
    private final Player owner;
    private PieceType type;

    public Piece(Player owner, PieceType type) {
        this.owner = owner;
        this.type = type;
    }

    public Player getOwner() {
        return owner;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }
}
