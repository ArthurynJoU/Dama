package org.dama;

import org.dama.console.ConsoleUI;
import org.dama.core.Board;
import org.dama.core.Game;
import org.dama.entity.Score;
import org.dama.service.ScoreService;
import org.dama.service.ScoreServiceJDBC;

import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    private final Scanner scanner = new Scanner(System.in);
    private final ScoreService scoreService = new ScoreServiceJDBC();

    public void run() {
        while(true) {
            showMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    startNewGame(false);
                    break;
                case "2":
                    startNewGame(true);
                    break;
                case "3":
                    showTopScores();
                    break;
                case "4":
                    showRules();
                    break;
                case "5":
                    System.out.println("Koniec programu...");
                    return;
                default:
                    System.out.println("Neplatná voľba, zadajte 1..5");
            }
        }
    }

    private void showMenu() {
        System.out.println("*************************");
        System.out.println("*        DÁMA HRA       *");
        System.out.println("*************************");
        System.out.println("1) Nová hra (klasická inicializácia)");
        System.out.println("2) Rýchla demo hra (pre učiteľa) - 1 minúta");
        System.out.println("3) Najlepšie výsledky");
        System.out.println("4) Pravidlá / Nápoveda");
        System.out.println("5) Koniec");
        System.out.print("Vaša voľba: ");
    }

    private void startNewGame(boolean demoForTeacher) {
        System.out.print("Zadajte meno bieleho hráča: ");
        String whiteName = scanner.nextLine().trim();
        if (whiteName.isEmpty()) whiteName = "Biely";

        System.out.print("Zadajte meno čierneho hráča (alebo prázdne pre Bota): ");
        String blackInput = scanner.nextLine().trim();
        boolean isPvp = !blackInput.isEmpty();
        String blackName = isPvp ? blackInput : "Bot";

        // Vytvoríme hraciu plochu
        Board board = new Board();

        // Buď klasické rozloženie, alebo špeciálna rýchla prezentácia.
        if (demoForTeacher) {
            board.initializeRychlaPrezentacia();
        } else {
            // Nič netreba, lebo konštruktor Board volá initialize() štandardne.
        }

        Game game = new Game(whiteName, blackName, isPvp, board);

        // Vytvoríme konzolové UI a spustíme hru.
        ConsoleUI ui = new ConsoleUI(game);
        ui.play();

        // Po skončení hry možnosť uložiť skóre
        if (game.isGameOver()) {
            String winner = game.getWinnerName();
            System.out.println("Hra skončila! Víťaz: " + winner);
            System.out.println("Uložiť výsledok do tabuľky skóre? (y/n)");
            String ans = scanner.nextLine().trim().toLowerCase();
            if (ans.startsWith("y")) {
                Score score = new Score(
                        "Dama",
                        winner,
                        winner.equals(whiteName) ? game.getWhiteScore() : game.getBlackScore(),
                        new Date()
                );
                scoreService.addScore(score);
                System.out.println("Výsledok uložený. Gratulácia, " + winner + "!");
            }
        }
    }

    private void showTopScores() {
        System.out.println("TOP 5 výsledkov hry Dáma:");
        try {
            scoreService.getTopScores("Dama").stream()
                    .limit(5)
                    .forEach(s -> System.out.printf(
                            "%s  %d bodov  (%s)\n",
                            s.getPlayer(), s.getPoints(), s.getPlayedOn()
                    ));
        } catch (Exception e) {
            System.out.println("Chyba pri čítaní výsledkov: " + e.getMessage());
        }
        System.out.println();
    }

    private void showRules() {
        System.out.println("PRAVIDLÁ HRY 'DÁMA':");
        System.out.println("- Hrá sa na 8x8, figúrky (pešiaky) sa pohybujú šikmo o 1 pole dopredu (biele hore, čierne dole).");
        System.out.println("- Branenie sa vykoná preskočením súperovej figúrky, ak je za ňou voľné pole.");
        System.out.println("- Vstup ťahu: napr. 'C3 D4'.");
        System.out.println("- Po dosiahnutí protiľahlej strany sa pešiak mení na dámu (KING).");
        System.out.println("- Teleport (T): ak figúrka vstúpi na 'T', premiestni sa na druhý 'T'.");
        System.out.println("- Počas hry možno zadať 'X' pre vzdanie sa.");
        System.out.println("Prajeme príjemnú hru!\n");
    }
}
