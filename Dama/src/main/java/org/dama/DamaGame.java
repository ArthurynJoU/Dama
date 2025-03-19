package org.dama;

import java.util.Scanner;

/**
 * Hlavná trieda, ktorá spustí hru:
 * - opýta sa na mená hráčov,
 * - zistí, či ide o Hráč-Bot alebo Hráč-Hráč,
 * - vytvorí inštanciu Game a ConsoleUI,
 * - spustí metódu play().
 */
public class DamaGame {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Zadajte meno bieleho hráča (max 9 znakov):");
        String white = sc.nextLine().trim();
        if (white.isEmpty()) white = "Alice";
        if (white.length() > 9) white = white.substring(0, 9);

        System.out.println("Zadajte meno čierneho hráča (max 9 znakov):");
        String black = sc.nextLine().trim();
        if (black.isEmpty()) black = "Bob";
        if (black.length() > 9) black = black.substring(0, 9);

        System.out.println("Zvoľte variant hry (napíšte 1 alebo 2):");
        System.out.println("1) Hráč - Bot");
        System.out.println("2) Hráč - Hráč");
        int variant = 1;
        try {
            variant = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            // ak je zadané niečo iné, predvolená hodnota je 1
        }
        boolean isPvp = (variant == 2);

        Game game = new Game(white, black, isPvp);
        ConsoleUI ui = new ConsoleUI(game);
        ui.play();
    }
}
