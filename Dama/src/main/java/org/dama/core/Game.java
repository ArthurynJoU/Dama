package org.dama.core;

import org.dama.entity.Comment;
import org.dama.entity.Rating;
import org.dama.service.ScoreException;
import org.dama.service.CommentServiceJDBC;
import org.dama.service.RatingServiceJDBC;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * Riadi logiku hry: ktory hráč je na ťahu, prideľovanie bodov za branenie,
 * rozpoznanie konca hry (keď niektorá strana nemá figúrky).
 */
public class Game {
    private final Board board;
    private final String whiteName;
    private final String blackName;
    private int whiteScore;
    private int blackScore;
    private final boolean playerVsPlayer;
    private Player currentPlayer;
    private boolean gameOver;

    public Game(String whiteName, String blackName, boolean isPvp, Board board) {
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.whiteScore = 0;
        this.blackScore = 0;
        this.playerVsPlayer = isPvp;
        this.board = board;
        this.currentPlayer = Player.WHITE;
        this.gameOver = false;
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

    public boolean isGameOver() {
        if (gameOver) return true;
        int wCount = board.countPieces(Player.WHITE);
        int bCount = board.countPieces(Player.BLACK);
        if (wCount == 0 || bCount == 0) {
            gameOver = true;
        }
        return gameOver;
    }

    public void switchPlayer() {
        if (!isGameOver()) {
            currentPlayer = (currentPlayer == Player.WHITE) ? Player.BLACK : Player.WHITE;
        }
    }

    /**
     * Pokus vykonať ťah z from do to. Vráti true, ak sa ťah podaril.
     */
    public boolean moveCoordinates(Position from, Position to) {
        if (isGameOver()) {
            System.out.println("Hra sa už skončila.");
            return false;
        }
        // overenie figúrky
        Piece piece = board.getPiece(from);
        if (piece == null) {
            System.out.println("Na danej pozícii nie je žiadna figúrka!");
            return false;
        }
        if (piece.getOwner() != currentPlayer) {
            System.out.println("Táto figúrka patrí súperovi!");
            return false;
        }

        // zistíme, či (from -> to) je medzi platnými ťahmi
        List<Move> valid = board.getValidMoves(from);
        Move chosen = null;
        for (Move m : valid) {
            if (m.getTo().equals(to)) {
                chosen = m;
                break;
            }
        }
        if (chosen == null) {
            System.out.println("Neplatný ťah!");
            return false;
        }

        // vykonanie
        Piece captured = board.makeMove(chosen);
        if (captured != null) {
            addScore(currentPlayer, captured);
            // možnosť opakovaného branenia
            multipleCapture(chosen.getTo());
        }

        // kontrola premeny pešiaka na dámu
        if (piece.getType() == PieceType.MAN) {
            if (piece.getOwner() == Player.WHITE && to.row == 0) {
                piece.setType(PieceType.KING);
            } else if (piece.getOwner() == Player.BLACK && to.row == Board.SIZE - 1) {
                piece.setType(PieceType.KING);
            }
        }
        return true;
    }

    /**
     * Ak je možné branenie po presune, môžeme ho ihneď vykonať (reťazenie).
     */
    private void multipleCapture(Position pos) {
        boolean again = true;
        while (again) {
            again = false;
            Piece p = board.getPiece(pos);
            if (p == null) break;
            List<Move> moves = board.getValidMoves(pos);
            for (Move m : moves) {
                if (m.isCapture()) {
                    Piece captured = board.makeMove(m);
                    if (captured != null) {
                        addScore(p.getOwner(), captured);
                        pos = m.getTo();
                        again = true;
                        // prípadná premena
                        if (p.getType() == PieceType.MAN) {
                            if (p.getOwner() == Player.WHITE && pos.row == 0) {
                                p.setType(PieceType.KING);
                            } else if (p.getOwner() == Player.BLACK && pos.row == Board.SIZE - 1) {
                                p.setType(PieceType.KING);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * Za každý zajatý pešiak dostane hráč 1 bod, za dámu 2 body.
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
     * Ak hrá proti počítaču (bot), spraví náhodný ťah.
     */
    public void botMove() {
        if (isGameOver()) return;
        List<Move> moves = board.getAllValidMoves(Player.BLACK);
        if (moves.isEmpty()) {
            System.out.println("Bot nemôže pohnúť!");
            return;
        }
        Move chosen = moves.get(new Random().nextInt(moves.size()));
        System.out.println("Bot ťahá: " + chosen);
        Piece piece = board.getPiece(chosen.getFrom());
        Piece captured = board.makeMove(chosen);
        if (captured != null) {
            addScore(Player.BLACK, captured);
            multipleCapture(chosen.getTo());
        }
        // premena
        if (piece != null && piece.getType() == PieceType.MAN && chosen.getTo().row == Board.SIZE - 1) {
            piece.setType(PieceType.KING);
        }
    }

    /**
     * Vzdanie sa => odstránime všetky figúrky tohto hráča.
     */
    public void surrender() {
        if (!isGameOver()) {
            if (currentPlayer == Player.WHITE) {
                removeAll(Player.WHITE);
            } else {
                removeAll(Player.BLACK);
            }
            gameOver = true;
        }
    }

    private void removeAll(Player player) {
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Piece p = board.getPiece(new Position(r,c));
                if (p != null && p.getOwner() == player) {
                    board.setPiece(new Position(r,c), null);
                }
            }
        }
    }

    public String getWinnerName() {
        int wCount = board.countPieces(Player.WHITE);
        int bCount = board.countPieces(Player.BLACK);
        if (wCount == 0 && bCount == 0) {
            return "Remíza???";
        }
        if (wCount == 0) {
            return blackName;
        } else if (bCount == 0) {
            return whiteName;
        }
        return "Zatiaľ nikto nevyhral";
    }

    public void saveMessage(String playerName, String gameName, String massage, CommentServiceJDBC commentService) {
        long elapsedTime = System.currentTimeMillis();
        Timestamp playedOn = new Timestamp(elapsedTime);

        Comment comment = new Comment(gameName, playerName, massage, playedOn);
        try {
            commentService.addComment(comment);
        } catch (ScoreException e) {
            System.err.println("Failed to save comment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveRating(String playerName, String gameName, int ratings, RatingServiceJDBC ratingService) {
        long elapsedTime = System.currentTimeMillis();
        Timestamp playedOn = new Timestamp(elapsedTime);

        Rating rating = new Rating(gameName, playerName, ratings, playedOn);
        try {
            ratingService.addRating(rating);
        } catch (ScoreException e) {
            System.err.println("Failed to save score: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
