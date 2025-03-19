package org.dama;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Hracia doska pre hru Dáma, rozmer 8x8.
 * Obsahuje 12 figúrok pre čierneho hráča (riadky 0..2)
 * a 12 figúrok pre bieleho hráča (riadky 5..7).
 * Uprostred (riadky 3..4) sa môžu náhodne pridať dva teleporty.
 */
public class Board {
    public static final int SIZE = 8;
    private final Piece[][] grid;
    private final boolean[][] teleports; // dva náhodné teleporty

    public Board() {
        grid = new Piece[SIZE][SIZE];
        teleports = new boolean[SIZE][SIZE];
        initialize();
        placeRandomTeleports();
    }

    /**
     * Metóda na inicializáciu hracej dosky:
     * - Čierny hráč (BLACK) v riadkoch 0..2 (iba tmavé polia),
     * - Biely hráč (WHITE) v riadkoch 5..7.
     */
    private void initialize() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = null;
                teleports[r][c] = false;
            }
        }
        // Čierne figúrky
        for (int r = 0; r <= 2; r++) {
            for (int c = 0; c < SIZE; c++) {
                if ((r + c) % 2 == 0) {
                    grid[r][c] = new Piece(Player.BLACK, PieceType.MAN);
                }
            }
        }
        // Biele figúrky
        for (int r = 5; r <= 7; r++) {
            for (int c = 0; c < SIZE; c++) {
                if ((r + c) % 2 == 0) {
                    grid[r][c] = new Piece(Player.WHITE, PieceType.MAN);
                }
            }
        }
    }

    /**
     * Náhodné umiestnenie dvoch teleportov do riadkov 3..4 na tmavé voľné polia.
     */
    private void placeRandomTeleports() {
        List<Position> freeDarkMid = new ArrayList<>();
        for (int r = 3; r <= 4; r++) {
            for (int c = 0; c < SIZE; c++) {
                if ((r + c) % 2 == 0 && grid[r][c] == null) {
                    freeDarkMid.add(new Position(r, c));
                }
            }
        }
        if (freeDarkMid.size() < 2) return;
        Random rnd = new Random();
        Position p1 = freeDarkMid.remove(rnd.nextInt(freeDarkMid.size()));
        Position p2 = freeDarkMid.remove(rnd.nextInt(freeDarkMid.size()));
        teleports[p1.row][p1.col] = true;
        teleports[p2.row][p2.col] = true;
    }

    public boolean isWithinBounds(Position pos) {
        return pos.row >= 0 && pos.row < SIZE && pos.col >= 0 && pos.col < SIZE;
    }

    public Piece getPiece(Position pos) {
        if (!isWithinBounds(pos)) return null;
        return grid[pos.row][pos.col];
    }

    public void setPiece(Position pos, Piece piece) {
        if (isWithinBounds(pos)) {
            grid[pos.row][pos.col] = piece;
        }
    }

    /**
     * Získa zoznam validných ťahov pre figúrku na danej pozícii.
     */
    public List<Move> getValidMoves(Position pos) {
        Piece piece = getPiece(pos);
        if (piece == null) {
            return new ArrayList<>();
        }
        if (piece.getType() == PieceType.MAN) {
            return getManMoves(pos);
        } else {
            return getKingMoves(pos);
        }
    }

    /**
     * Získa všetky validné ťahy pre zadaného hráča (všetky jeho figúrky na doske).
     */
    public List<Move> getAllValidMoves(Player player) {
        List<Move> list = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getOwner() == player) {
                    list.addAll(getValidMoves(new Position(r, c)));
                }
            }
        }
        return list;
    }

    /**
     * Spočítanie figúrok daného hráča na doske.
     */
    public int countPieces(Player player) {
        int count = 0;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece piece = grid[r][c];
                if (piece != null && piece.getOwner() == player) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Vykonanie ťahu (presun figúrky a prípadné vyhodenie).
     * Vráti vyhodenú figúrku (ak existuje), inak null.
     */
    public Piece makeMove(Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();
        Piece moving = getPiece(from);
        if (moving == null) return null;

        setPiece(to, moving);
        setPiece(from, null);

        Piece capturedPiece = null;
        if (move.isCapture()) {
            Position capPos = move.getCapturedPosition();
            capturedPiece = getPiece(capPos);
            if (capturedPiece != null) {
                setPiece(capPos, null);
            }
        }
        // Overenie teleportu
        if (teleports[to.row][to.col]) {
            Position otherT = findOtherTeleport(to);
            if (otherT != null && getPiece(otherT) == null) {
                setPiece(otherT, moving);
                setPiece(to, null);
                // Farebné zobrazenie pre teleport:
                System.out.println("\u001B[32mTeleport!\u001B[0m " + to + " -> " + otherT);
            }
        }
        return capturedPiece;
    }

    private Position findOtherTeleport(Position current) {
        for (int r = 3; r <= 4; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (teleports[r][c]) {
                    if (!(r == current.row && c == current.col)) {
                        return new Position(r, c);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Vypíše hraciu dosku do konzoly, so zvýraznením cieľových polí pre aktuálneho hráča.
     * Vyfarbíme figúrky: biely hráč = biela farba, čierny hráč = červená farba.
     */
    public void printBoard(String whiteName, String blackName, int whiteScore, int blackScore, Player currentPlayer) {
        System.out.printf("White: %s     Score: %d\n", whiteName, whiteScore);
        System.out.printf("Black: %s     Score: %d\n\n", blackName, blackScore);

        // Získame všetky možné ciele (to) pre aktuálneho hráča
        List<Move> moves = getAllValidMoves(currentPlayer);
        Set<Position> possibleDestinations = new HashSet<>();
        for (Move m : moves) {
            possibleDestinations.add(m.getTo());
        }

        // Hlavička so stĺpcami
        System.out.print("    ");
        for (char col = 'A'; col <= 'H'; col++) {
            System.out.printf("  %c ", col);
        }
        System.out.println();
        System.out.print("    +");
        for (int i = 0; i < SIZE; i++) {
            System.out.print("---+");
        }
        System.out.println();

        // Vypíšeme riadky zhora (r=0 -> "8") až dole (r=7 -> "1")
        for (int r = 0; r < SIZE; r++) {
            int rowLabel = SIZE - r;
            System.out.printf(" %2d |", rowLabel);
            for (int c = 0; c < SIZE; c++) {
                Piece p = grid[r][c];
                if (p != null) {
                    // Výpis figúrky s farbou
                    System.out.print(" " + coloredPieceChar(p) + " |");
                } else if (teleports[r][c]) {
                    System.out.print(" T |");
                } else {
                    Position pos = new Position(r, c);
                    if (possibleDestinations.contains(pos)) {
                        // Cieľové polia pre aktuálneho hráča označíme bodkou
                        System.out.print(" . |");
                    } else {
                        System.out.print("   |");
                    }
                }
            }
            System.out.printf("%2d\n", rowLabel);
            System.out.print("    +");
            for (int i = 0; i < SIZE; i++) {
                System.out.print("---+");
            }
            System.out.println();
        }
        // Spodná značka
        System.out.print("    ");
        for (char col = 'A'; col <= 'H'; col++) {
            System.out.printf("  %c ", col);
        }
        System.out.println("\n");
    }

    /**
     * Vráti farebný znak figúrky:
     * - biely hráč = biela farba,
     * - čierny hráč = červená farba,
     * - MAN = malé písmeno (w/b), KING = veľké (W/B).
     */
    private String coloredPieceChar(Piece piece) {
        char ch;
        if (piece.getOwner() == Player.WHITE) {
            ch = (piece.getType() == PieceType.MAN) ? 'w' : 'W';
            // biela farba  => \u001B[37m
            return "\u001B[37m" + ch + "\u001B[0m";
        } else {
            ch = (piece.getType() == PieceType.MAN) ? 'b' : 'B';
            // červená farba => \u001B[31m
            return "\u001B[31m" + ch + "\u001B[0m";
        }
    }

    // Metódy pre pešiaka (MAN)
    private List<Move> getManMoves(Position pos) {
        List<Move> list = new ArrayList<>();
        Piece p = getPiece(pos);
        if (p == null) return list;
        Player owner = p.getOwner();
        int dir = (owner == Player.WHITE) ? -1 : +1;

        int[] dc = {-1, 1};
        for (int d : dc) {
            Position next = new Position(pos.row + dir, pos.col + d);
            if (isWithinBounds(next) && getPiece(next) == null) {
                list.add(new Move(pos, next));
            }
            // Preskúšanie vyhodenia
            Position between = next;
            Position landing = new Position(pos.row + 2 * dir, pos.col + 2 * d);
            if (isWithinBounds(between) && isWithinBounds(landing)) {
                Piece bp = getPiece(between);
                if (bp != null && bp.getOwner() != owner && getPiece(landing) == null) {
                    list.add(new Move(pos, landing, between));
                }
            }
        }
        return list;
    }

    // Metódy pre dámu (KING)
    private List<Move> getKingMoves(Position pos) {
        List<Move> list = new ArrayList<>();
        Piece p = getPiece(pos);
        if (p == null) return list;
        int[] dr = {-1, -1, 1, 1};
        int[] dc = {-1, 1, -1, 1};
        for (int i = 0; i < 4; i++) {
            int rr = pos.row + dr[i];
            int cc = pos.col + dc[i];
            boolean captureUsed = false;
            while (isWithinBounds(new Position(rr, cc))) {
                if (grid[rr][cc] == null) {
                    list.add(new Move(pos, new Position(rr, cc)));
                } else {
                    if (!captureUsed && grid[rr][cc].getOwner() != p.getOwner()) {
                        int rr2 = rr + dr[i];
                        int cc2 = cc + dc[i];
                        if (isWithinBounds(new Position(rr2, cc2)) && grid[rr2][cc2] == null) {
                            list.add(new Move(pos, new Position(rr2, cc2), new Position(rr, cc)));
                        }
                    }
                    break;
                }
                rr += dr[i];
                cc += dc[i];
            }
        }
        return list;
    }
}
