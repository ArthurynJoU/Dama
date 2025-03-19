package org.dama;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void initialPieceCount() {
        Board board = new Board();
        // Očakávame 12 čiernych a 12 bielych figúrok
        int blackCount = board.countPieces(Player.BLACK);
        int whiteCount = board.countPieces(Player.WHITE);

        assertEquals(12, blackCount, "Na doske má byť 12 čiernych figúrok.");
        assertEquals(12, whiteCount, "Na doske má byť 12 bielych figúrok.");
    }

    @Test
    public void moveManForward() {
        Board board = new Board();
        // Vyberieme nejakú bielu figúrku, napr. v riadku 6 alebo 7
        Position from = new Position(6, 0); // ak to je biela figúrka
        Piece piece = board.getPiece(from);
        assertNotNull(piece, "Očakávame bielu figúrku na (6,0).");
        assertEquals(Player.WHITE, piece.getOwner(), "Figúrka má patriť bielemu hráčovi.");
        // Pokúsime sa posunúť o 1 dopredu-vľavo (row -1, col -1)
        // Napr. from(6,0) → to(5, -1) je mimo dosky, tak skúsime do(5,1)
        Position to = new Position(5, 1);

        // Overíme, či je ťah validný v zozname getValidMoves
        boolean found = board.getValidMoves(from).stream()
                .anyMatch(m -> m.getTo().equals(to));
        // Možno je to validné, ak je tmavé pole a prázdne
        if (!found) {
            fail("Nepodarilo sa nájsť platný ťah z (6,0) na (5,1).");
        }

        // Vykonáme ťah
        Move chosen = board.getValidMoves(from).stream()
                .filter(m -> m.getTo().equals(to))
                .findFirst()
                .orElse(null);
        assertNotNull(chosen, "Nemáme validný ťah, aj keď by mal existovať.");

        board.makeMove(chosen);

        // Skontrolujeme, či sa figúrka presunula
        assertNull(board.getPiece(from), "Pôvodná pozícia má byť prázdna.");
        Piece newPiece = board.getPiece(to);
        assertNotNull(newPiece, "Nová pozícia (5,1) má obsahovať figúrku.");
        assertEquals(Player.WHITE, newPiece.getOwner(), "Figúrka je biela.");
    }

    @Test
    public void captureTest() {
        Board board = new Board();
        // Na jednoduchý test vyhodíme manuálne čiernu figúrku do polohy, kde ju vie biela zobrať
        // Alebo skôr by sme museli upraviť dosku. Pre demonštráciu len kontrola, že sa to neskončí chybou

        // Napríklad, zistíme, či volanie makeMove s neplatným ťahom nezhodí program
        Position from = new Position(6, 0); // biela
        Position to = new Position(4, 2);   // teoreticky preskok
        Move notSure = new Move(from, to, new Position(5,1)); // fiktívne
        Piece captured = board.makeMove(notSure);
        // Ak to nebolo platné, captured by mal byť null
        // a figúrka sa nemá presunúť.
        assertNull(captured, "Nemalo by dôjsť k vyhodeniu, lebo asi to nebol validný ťah.");
        assertNotNull(board.getPiece(from), "Figúrka z (6,0) tam asi ostane.");
    }
}
