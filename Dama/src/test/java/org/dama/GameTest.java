package org.dama;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void newGameInitialState() {
        Game game = new Game("Alice", "Bob", false);
        assertNotNull(game.getBoard(), "Game má mať vytvorenú hraciu dosku.");
        assertEquals("Alice", game.getWhiteName(), "Nesedí meno bieleho hráča.");
        assertEquals("Bob", game.getBlackName(), "Nesedí meno čierneho hráča.");
        assertEquals(Player.WHITE, game.getCurrentPlayer(), "Na začiatku ťahá biely hráč.");
        // Overíme, že hra ešte nie je over
        assertFalse(game.isGameOver(), "Na začiatku hra nemá byť ukončená.");
    }

    @Test
    public void switchingPlayers() {
        Game game = new Game("Alice", "Bob", true);
        // Na začiatku je currentPlayer = WHITE
        assertEquals(Player.WHITE, game.getCurrentPlayer());
        game.switchPlayer();
        assertEquals(Player.BLACK, game.getCurrentPlayer());
        game.switchPlayer();
        assertEquals(Player.WHITE, game.getCurrentPlayer());
    }

    @Test
    public void moveCoordinatesFail() {
        Game game = new Game("Alice", "Bob", false);
        // Skúsime ťah z pozície kde asi nie je figúrka
        // Napr. (0,0) je čierna figúrka, ale nechceme ťahať bielym
        // Ešte je currentPlayer = WHITE, no (0,0) patrí čiernemu...
        boolean result = game.moveCoordinates(new Position(0,0), new Position(1,1));
        // očakávame false
        assertFalse(result, "Nesmie byť platný ťah, lebo biely hráč nemôže hýbať čiernou figúrkou.");
    }

    @Test
    public void gameOverCheck() {
        Game game = new Game("Alice", "Bob", false);
        // Ručne vymažeme všetky čierne figúrky z board, ak chceme simulovať koniec
        Board board = game.getBoard();
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Piece p = board.getPiece(new Position(r,c));
                if (p != null && p.getOwner() == Player.BLACK) {
                    board.setPiece(new Position(r,c), null);
                }
            }
        }
        // Teraz by mal blackCount=0
        assertTrue(game.isGameOver(), "Hra má byť ukončená, lebo čierny nemá žiadne figúrky.");
    }
}
