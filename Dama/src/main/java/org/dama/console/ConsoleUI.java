package org.dama.console;

import org.dama.core.*;

import java.util.Scanner;

/**
 * Konzolové UI - interaktívny vstup z klávesnice, volá metódy z Game.
 */
public class ConsoleUI {
    private final Game game;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(Game game) {
        this.game = game;
    }

    public void play() {
        while (!game.isGameOver()) {
            game.getBoard().printBoard(
                    game.getWhiteName(),
                    game.getBlackName(),
                    game.getWhiteScore(),
                    game.getBlackScore(),
                    game.getCurrentPlayer()
            );

            // Ak nie je PvP a na ťahu je čierny (Bot), tak vykoná botMove()
            if (!game.isPlayerVsPlayer() && game.getCurrentPlayer() == Player.BLACK) {
                game.botMove();
                game.switchPlayer();
                continue;
            }

            System.out.print("Zadajte svoj ťah (napr. 'C3 D4'), alebo 'X' pre vzdanie sa: ");
            String line = scanner.nextLine().trim().toUpperCase();

            if (line.equals("X")) {
                System.out.println("Hráč sa vzdáva.");
                game.surrender();
                break;
            }

            // Očakávame formát "C3 D4"
            String[] parts = line.split(" +");
            if (parts.length < 2) {
                System.out.println("Neplatný formát vstupu, skúste napr. 'C3 D4'.");
                continue;
            }
            Position from = parsePos(parts[0]);
            Position to = parsePos(parts[1]);
            if (from == null || to == null) {
                System.out.println("Neplatná pozícia (skúste napr. 'C3').");
                continue;
            }

            boolean ok = game.moveCoordinates(from, to);
            if (ok) {
                game.switchPlayer();
            }
        }
    }

    /**
     * Parsuje textovú reprezentáciu pozície (napr. "C3") na objekt Position.
     */
    private Position parsePos(String txt) {
        if (txt.length() < 2) return null;
        char colChar = txt.charAt(0);
        if (colChar < 'A' || colChar > 'H') return null;
        int col = colChar - 'A';

        String rowPart = txt.substring(1);
        int rowNum;
        try {
            rowNum = Integer.parseInt(rowPart);
        } catch (NumberFormatException e) {
            return null;
        }
        if (rowNum < 1 || rowNum > 8) return null;

        // Vnútorná reprezentácia: row 0 hore, row 7 dole
        int row = 8 - rowNum;
        return new Position(row, col);
    }
}
