package org.dama;

import java.util.Scanner;

/**
 * Konzolové používateľské rozhranie pre hru Dáma.
 * - Žiadny výpis "Možné ťahy"
 * - Doska obsahuje bodky '.' tam, kam môže aktuálny hráč ťahať.
 */
public class ConsoleUI {
    private final Game game;
    private final Scanner scanner;

    public ConsoleUI(Game game) {
        this.game = game;
        this.scanner = new Scanner(System.in);
    }

    public void play() {
        while (!game.isGameOver()) {
            // Vypíšeme hraciu dosku s farbami
            game.getBoard().printBoard(
                    game.getWhiteName(),
                    game.getBlackName(),
                    game.getWhiteScore(),
                    game.getBlackScore(),
                    game.getCurrentPlayer()
            );

            // Ak je režim Hráč-Bot a ťahá čierny hráč, voláme botMove
            if (!game.isPlayerVsPlayer() && game.getCurrentPlayer() == Player.BLACK) {
                System.out.println("Ťah bota (" + game.getBlackName() + "):");
                game.botMove();
                game.switchPlayer();
            } else {
                // Inak je na ťahu človek
                if (game.getCurrentPlayer() == Player.WHITE) {
                    System.out.println("Na ťahu je biely hráč (" + game.getWhiteName() + "). Napíšte ťah (napr. C3 D4) alebo X pre ukončenie:");
                } else {
                    System.out.println("Na ťahu je čierny hráč (" + game.getBlackName() + "). Napíšte ťah (napr. C3 D4) alebo X:");
                }
                handleHumanMove();
            }
        }

        // Po ukončení hry vypíšeme ešte raz dosku a určíme víťaza
        game.getBoard().printBoard(
                game.getWhiteName(),
                game.getBlackName(),
                game.getWhiteScore(),
                game.getBlackScore(),
                game.getCurrentPlayer()
        );

        int wCount = game.getBoard().countPieces(Player.WHITE);
        int bCount = game.getBoard().countPieces(Player.BLACK);
        if (wCount == 0) {
            System.out.println("Biely hráč (" + game.getWhiteName() + ") nemá figúrky. Čierny vyhral!");
        } else {
            System.out.println("Čierny hráč (" + game.getBlackName() + ") nemá figúrky. Biely vyhral!");
        }
    }

    /**
     * Spracovanie vstupu človeka. Očakávame 'C3 D4' alebo 'X' pre ukončenie hry.
     */
    private void handleHumanMove() {
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("X")) {
            System.out.println("Hra ukončená hráčom.");
            System.exit(0);
        }
        String[] parts = input.split("\\s+");
        if (parts.length != 2) {
            System.out.println("Neplatný formát. Skúste napr. 'C3 D4'.");
            return;
        }
        Position from = parsePosition(parts[0]);
        Position to = parsePosition(parts[1]);
        if (from == null || to == null) {
            System.out.println("Neplatné pozície.");
            return;
        }
        boolean success = game.moveCoordinates(from, to);
        if (success) {
            game.switchPlayer();
        }
    }

    /**
     * Konverzia reťazca (napr. "C3") na objekt Position, kde row=0 je horný okraj (8).
     */
    private Position parsePosition(String s) {
        if (s.length() < 2) return null;
        char colChar = Character.toUpperCase(s.charAt(0));
        int col = colChar - 'A';
        int rowNum;
        try {
            rowNum = Integer.parseInt(s.substring(1));
        } catch (NumberFormatException e) {
            return null;
        }
        int row = 8 - rowNum;
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null;
        }
        return new Position(row, col);
    }
}
