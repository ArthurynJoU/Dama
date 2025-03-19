package org.dama;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PieceTest {

    @Test
    public void testConstructor() {
        Piece piece = new Piece(Player.WHITE, PieceType.MAN);
        assertEquals(Player.WHITE, piece.getOwner(), "Vlastník má byť biely hráč.");
        assertEquals(PieceType.MAN, piece.getType(), "Typ má byť MAN.");
    }

    @Test
    public void testSetType() {
        Piece piece = new Piece(Player.BLACK, PieceType.MAN);
        piece.setType(PieceType.KING);
        assertEquals(PieceType.KING, piece.getType(), "Typ sa mal zmeniť na KING.");
    }
}
