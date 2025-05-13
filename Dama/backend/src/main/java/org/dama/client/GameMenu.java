package org.dama.client;

import org.dama.console.ConsoleUI;
import org.dama.core.Board;
import org.dama.core.Game;
import org.dama.entity.Score;
import org.dama.entity.Comment;
import org.dama.entity.Rating;
import org.dama.service.CommentService;
import org.dama.service.RatingService;
import org.dama.service.ScoreService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class GameMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final ScoreService scoreService;
    private final CommentService commentService;
    private final RatingService ratingService;

    public GameMenu(ScoreService scoreService, CommentService commentService, RatingService ratingService) {
        this.scoreService = scoreService;
        this.commentService = commentService;
        this.ratingService = ratingService;
    }

    public void run() {
        while (true) {
            showMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> startNewGame(false);
                case "2" -> startNewGame(true);
                case "3" -> showTopScores();
                case "4" -> showRules();
                case "5" -> {
                    System.out.println("Koniec programu...");
                    return;
                }
                default -> System.out.println("Neplatná voľba, zadajte 1..5");
            }
        }
    }

    private void showMenu() {
        System.out.println("*************************");
        System.out.println("*        DÁMA HRA       *");
        System.out.println("*************************");
        System.out.println("1) Nová hra");
        System.out.println("2) Demo hra");
        System.out.println("3) Najlepšie výsledky");
        System.out.println("4) Pravidlá");
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
        Board board = new Board();
        if (demoForTeacher) {
            board.initializeRychlaPrezentacia();
        }
        Game game = new Game(whiteName, blackName, isPvp, board);
        ConsoleUI ui = new ConsoleUI(game);
        ui.play();
        if (game.isGameOver()) {
            String winner = game.getWinnerName();
            System.out.println("Hra skončila! Víťaz: " + winner);
            System.out.println("Uložiť výsledok do tabuľky skóre? (y/n)");
            String ans = scanner.nextLine().trim().toLowerCase();
            if (ans.startsWith("y")) {
                Score score = new Score("Dama", winner,
                        winner.equals(whiteName) ? game.getWhiteScore() : game.getBlackScore(),
                        LocalDateTime.now());
                scoreService.addScore(score);
                System.out.println("Výsledok uložený. Gratulácia, " + winner + "!");
                System.out.println("Chcete pridať hodnotenie hry? (zadajte číslo 1..5 alebo stlačte Enter pre preskočenie):");
                String ratingStr = scanner.nextLine().trim();
                while (!ratingStr.isEmpty()) {
                    try {
                        int ratingValue = Integer.parseInt(ratingStr);
                        if (ratingValue >= 1 && ratingValue <= 5) {
                            Rating rating = new Rating("Dama", winner, ratingValue, LocalDateTime.now());
                            ratingService.setRating(rating);
                            System.out.println("Hodnotenie uložené!");
                            break;
                        } else {
                            System.out.println("Neplatné číslo, skúste znova (zadajte číslo 1..5 alebo Enter pre preskočenie):");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Zadané neplatné číslo, skúste znova (zadajte číslo 1..5 alebo Enter pre preskočenie):");
                    }
                    ratingStr = scanner.nextLine().trim();
                }
                System.out.println("Chcete pridať komentár? (y/n)");
                String cAns = scanner.nextLine().trim().toLowerCase();
                if (cAns.startsWith("y")) {
                    System.out.println("Zadajte svoj komentár:");
                    String text = scanner.nextLine();
                    Comment comment = new Comment("Dama", winner, text, LocalDateTime.now());
                    commentService.addComment(comment);
                    System.out.println("Komentár uložený!");
                }
                int avgRating = ratingService.getAverageRating("Dama");
                System.out.println("Priemerné hodnotenie hry: " + avgRating);
            }
        }
    }

    private void showTopScores() {
        scoreService.getTopScores("Dama", 5)
                .forEach(score -> System.out.printf("%s  %d bodov  (%s)\n",
                        score.getPlayer(), score.getPoints(), score.getPlayedOn()));
        System.out.println();
    }

    private void showRules() {
        System.out.println("PRAVIDLÁ HRY:");
        System.out.println("- Hrá sa na 8x8, figúrky (pešiaky) sa pohybujú šikmo o 1 pole dopredu (biele hore, čierne dole).");
        System.out.println("- Branenie sa vykoná preskočením súperovej figúrky, ak je za ňou voľné pole.");
        System.out.println("- Vstup ťahu: napr. 'C3 D4'.");
        System.out.println("- Po dosiahnutí protiľahlej strany sa pešiak mení na dámu (KING).");
        System.out.println("- Teleport (T): ak figúrka vstúpi na 'T', premiestni sa na druhý 'T'.");
        System.out.println("- Počas hry možno zadať 'X' pre vzdanie sa.");
        System.out.println("Prajeme príjemnú hru!\n");
    }
}
