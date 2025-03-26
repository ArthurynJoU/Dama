package org.dama.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void testInitialSetup() {
        Board board = new Board();
        // должно быть 12 чёрных, 12 белых
        int blackCount = board.countPieces(Player.BLACK);
        int whiteCount = board.countPieces(Player.WHITE);
        assertEquals(12, blackCount);
        assertEquals(12, whiteCount);
    }

    @Test
    public void testInitializeDemo() {
        Board board = new Board();
        board.initializeDemo();
        // тут смотрим, что нужные шашки стоят где надо
        Piece p1 = board.getPiece(new Position(5,2)); // C3
        assertNotNull(p1);
        assertEquals(Player.WHITE, p1.getOwner());

        Piece p2 = board.getPiece(new Position(3,6)); // G5
        assertNotNull(p2);
        assertEquals(Player.WHITE, p2.getOwner());

        Piece p3 = board.getPiece(new Position(1,4)); // E7
        assertNotNull(p3);
        assertEquals(Player.BLACK, p3.getOwner());
    }
}
