package org.dama;

import java.util.List;
import java.util.Random;

/**
 * Trieda Game zabezpečuje logiku hry Dáma, prideľovanie skóre a striedanie hráčov.
 */
public class Game {
    private final Board board;
    private Player currentPlayer;

    private final String whiteName;
    private final String blackName;
    private int whiteScore;
    private int blackScore;

    private final boolean playerVsPlayer;

    public Game(String whiteName, String blackName, boolean isPVP) {
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.whiteScore = 0;
        this.blackScore = 0;
        this.playerVsPlayer = isPVP;

        board = new Board();
        currentPlayer = Player.WHITE;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public int getWhiteScore() {
        return whiteScore;
    }

    public int getBlackScore() {
        return blackScore;
    }

    public boolean isPlayerVsPlayer() {
        return playerVsPlayer;
    }

    /**
     * Overí, či hra ešte neskončila (ak nejaký hráč prišiel o všetky figúrky).
     */
    public boolean isGameOver() {
        return board.countPieces(Player.WHITE) == 0
                || board.countPieces(Player.BLACK) == 0;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == Player.WHITE) ? Player.BLACK : Player.WHITE;
    }

    /**
     * Vykonanie ťahu z pozície from do to (napr. "C3 D4").
     * Ak je vyhodená figúrka, pripočítame skóre (MAN=+1, KING=+2).
     */
    public boolean moveCoordinates(Position from, Position to) {
        Piece piece = board.getPiece(from);
        if (piece == null) {
            System.out.println("Na danej pozícii nie je žiadna figúrka.");
            return false;
        }
        if (piece.getOwner() != currentPlayer) {
            System.out.println("Nemôžete hýbať súperovou figúrkou.");
            return false;
        }

        // Hľadáme ťah v zozname validMoves
        List<Move> valid = board.getValidMoves(from);
        Move chosen = null;
        for (Move m : valid) {
            if (m.getTo().equals(to)) {
                chosen = m;
                break;
            }
        }
        if (chosen == null) {
            System.out.println("Neplatný ťah.");
            return false;
        }
        // Vykonáme ťah
        Piece capturedPiece = board.makeMove(chosen);
        if (capturedPiece != null) {
            addScore(currentPlayer, capturedPiece);
        }

        // Skontrolujeme promóciu na dámu
        if (piece.getType() == PieceType.MAN) {
            if (piece.getOwner() == Player.WHITE && to.row == 0) {
                piece.setType(PieceType.KING);
            }
            if (piece.getOwner() == Player.BLACK && to.row == Board.SIZE - 1) {
                piece.setType(PieceType.KING);
            }
        }

        // Ak išlo o vyhodenie, môže nastať reťazenie (viacnásobné braníe)
        if (chosen.isCapture()) {
            multipleCapture(to);
        }

        return true;
    }

    /**
     * Pripočíta skóre hráčovi: MAN=+1, KING=+2.
     */
    private void addScore(Player player, Piece capturedPiece) {
        int add = (capturedPiece.getType() == PieceType.MAN) ? 1 : 2;
        if (player == Player.WHITE) {
            whiteScore += add;
        } else {
            blackScore += add;
        }
    }

    /**
     * Spracovanie viacnásobného vyhadzovania v jednom ťahu,
     * kým existuje nejaké vyhadzovacie ťahy pre tú istú figúrku.
     */
    private void multipleCapture(Position currentPos) {
        boolean again = true;
        while (again) {
            again = false;
            Piece p = board.getPiece(currentPos);
            if (p == null) break;
            List<Move> moves = board.getValidMoves(currentPos);
            for (Move m : moves) {
                if (m.isCapture()) {
                    Piece captured = board.makeMove(m);
                    if (captured != null) {
                        addScore(p.getOwner(), captured);
                    }
                    // Promócia
                    if (p.getType() == PieceType.MAN) {
                        if (p.getOwner() == Player.WHITE && m.getTo().row == 0) {
                            p.setType(PieceType.KING);
                        }
                        if (p.getOwner() == Player.BLACK && m.getTo().row == Board.SIZE - 1) {
                            p.setType(PieceType.KING);
                        }
                    }
                    currentPos = m.getTo();
                    again = true;
                    break;
                }
            }
        }
    }

    /**
     * Jednoduchá logika ťahu bota: náhodne vyberie jeden z validMoves pre BLACK.
     */
    public void botMove() {
        List<Move> all = board.getAllValidMoves(Player.BLACK);
        if (all.isEmpty()) {
            System.out.println("Bot nemá žiadne platné ťahy.");
            return;
        }
        Move chosen = all.get(new Random().nextInt(all.size()));
        System.out.println("Bot ťahá: " + chosen);

        Piece piece = board.getPiece(chosen.getFrom());
        Piece captured = board.makeMove(chosen);
        if (captured != null) {
            addScore(Player.BLACK, captured);
        }
        // Promócia
        if (piece != null && piece.getType() == PieceType.MAN && chosen.getTo().row == Board.SIZE - 1) {
            piece.setType(PieceType.KING);
        }
        if (chosen.isCapture()) {
            multipleCapture(chosen.getTo());
        }
    }
}
