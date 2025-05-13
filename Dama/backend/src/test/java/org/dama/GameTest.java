package org.dama;

import org.dama.core.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testGameOver() {
        Board board = new Board();
        Game game = new Game("Alice", "Bob", true, board);

        for (int r=0; r<Board.SIZE; r++) {
            for (int c=0; c<Board.SIZE; c++) {
                Piece p = board.getPiece(new Position(r,c));
                if (p != null && p.getOwner() == Player.BLACK) {
                    board.setPiece(new Position(r,c), null);
                }
            }
        }
        assertTrue(game.isGameOver());
        assertEquals("Alice", game.getWinnerName());
    }

    @Test
    public void testMoveCoordinates() {
        Board board = new Board();

        Game game = new Game("W", "B", true, board);
        // zmeníme ťah na platný z (5,1)=B3 na (4,0)=A4
        boolean result = game.moveCoordinates(new Position(5,1), new Position(4,0));
        assertTrue(result);

        assertNull(board.getPiece(new Position(5,1)));
        Piece p = board.getPiece(new Position(4,0));
        assertNotNull(p);
        assertEquals(Player.WHITE, p.getOwner());
    }

}
