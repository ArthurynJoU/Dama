package org.dama.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testGameOver() {
        Board board = new Board();
        Game game = new Game("Alice", "Bob", true, board);
        // Сотрем все чёрные шашки
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
        // допустим, возьмём белую шашку (row=6,col=0) -> на (5,1)
        Game game = new Game("W", "B", true, board);
        boolean result = game.moveCoordinates(new Position(6,0), new Position(5,1));
        assertTrue(result);
        // проверяем, что шашка сдвинулась
        assertNull(board.getPiece(new Position(6,0)));
        Piece p = board.getPiece(new Position(5,1));
        assertNotNull(p);
        assertEquals(Player.WHITE, p.getOwner());
    }
}
