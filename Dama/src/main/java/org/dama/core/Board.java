package org.dama.core;

import java.util.*;

/**
 * Trieda Board predstavuje 8x8 hraciu plochu s figúrkami a teleportmi.
 * Umožňuje inicializovať rôzne začiatočné pozície,
 * vykonávať ťahy, branenia a tlačiť ASCII-reprezentáciu do konzoly.
 */
public class Board {
    public static final int SIZE = 8;
    private final Piece[][] grid;
    private final Map<Position, Position> teleports;

    public Board() {
        this.grid = new Piece[SIZE][SIZE];
        this.teleports = new HashMap<>();
        initialize();
    }

    /**
     * Klasická štartovacia pozícia na 8x8 pre dámu (checkers).
     * Čierne figúrky hore, biele dole, na tmavých poliach.
     */
    private void initialize() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = null;
            }
        }
        teleports.clear();

        // Čierne figúrky (rady 0..2)
        for (int r = 0; r <= 2; r++) {
            for (int c = 0; c < SIZE; c++) {
                if ((r + c) % 2 == 0) {
                    grid[r][c] = new Piece(Player.BLACK, PieceType.MAN);
                }
            }
        }
        // Biele figúrky (rady 5..7)
        for (int r = 5; r <= 7; r++) {
            for (int c = 0; c < SIZE; c++) {
                if ((r + c) % 2 == 0) {
                    grid[r][c] = new Piece(Player.WHITE, PieceType.MAN);
                }
            }
        }
    }

    /**
     * Kratšia demo-inicializácia, ktorá umiestni len pár figúrok a 1 teleport.
     */
    public void initializeDemo() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = null;
            }
        }
        teleports.clear();

        // Príklad: Biela na C3 (row=5,col=2), Biela na G5 (row=3,col=6),
        // Čierna na E7 (row=1,col=4)
        grid[5][2] = new Piece(Player.WHITE, PieceType.MAN);
        grid[3][6] = new Piece(Player.WHITE, PieceType.MAN);
        grid[1][4] = new Piece(Player.BLACK, PieceType.MAN);

        // Teleport G5 <-> C7
        addTeleport(new Position(3,6), new Position(1,2));
    }

    /**
     * Špeciálna rýchla prezentácia s dvoma teleportmi (napr. do 1 minúty).
     */
    public void initializeRychlaPrezentacia() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = null;
            }
        }
        teleports.clear();

        // Biele figúrky (ľahko dostupné na branenie)
        grid[5][2] = new Piece(Player.WHITE, PieceType.MAN); // C3
        grid[5][4] = new Piece(Player.WHITE, PieceType.MAN); // E3

        // Čierne figúrky (blízko bielych, aby sa stretli)
        grid[2][3] = new Piece(Player.BLACK, PieceType.MAN); // D6
        grid[2][5] = new Piece(Player.BLACK, PieceType.MAN); // F6

        // Presne jeden pár teleportov, logicky umiestnený
        addTeleport(new Position(4, 3), new Position(3, 4));  // D4 <-> E5
    }


    /**
     * Pridanie dvoch jednosmerne spárovaných teleportov:
     * teleports[from] = to  + teleports[to] = from
     */
    public void addTeleport(Position from, Position to) {
        teleports.put(from, to);
        teleports.put(to, from);
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
     * Zistí všetky platné ťahy (vrátane prípadného branenia),
     * podľa druhu figúrky (MAN alebo KING).
     */
    public List<Move> getValidMoves(Position pos) {
        Piece piece = getPiece(pos);
        if (piece == null) {
            return Collections.emptyList();
        }
        if (piece.getType() == PieceType.MAN) {
            return getManMoves(pos);
        } else {
            return getKingMoves(pos);
        }
    }

    /**
     * Vráti zoznam všetkých možných ťahov, ktoré môže spraviť hráč 'player'.
     */
    public List<Move> getAllValidMoves(Player player) {
        List<Move> result = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Position pos = new Position(r, c);
                Piece p = getPiece(pos);
                if (p != null && p.getOwner() == player) {
                    result.addAll(getValidMoves(pos));
                }
            }
        }
        return result;
    }

    /**
     * Vykoná ťah (vrátane možného branenia a teleportu).
     * Vráti figúrku, ktorá bola braná (ak nejaká bola).
     */
    public Piece makeMove(Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();
        Piece moving = getPiece(from);
        if (moving == null) return null;

        // vykonaj presun
        setPiece(to, moving);
        setPiece(from, null);

        Piece captured = null;
        // ak išlo o branenie
        if (move.isCapture()) {
            Position capPos = move.getCapturedPosition();
            captured = getPiece(capPos);
            if (captured != null) {
                setPiece(capPos, null);
            }
        }

        // teleport (ak je cieľová pozícia teleporta)
        if (teleports.containsKey(to)) {
            Position exit = teleports.get(to);
            if (getPiece(exit) == null) {
                setPiece(exit, moving);
                setPiece(to, null);
                System.out.println("Teleport! " + to + " -> " + exit);
            }
        }
        return captured;
    }

    /**
     * Spočíta, koľko figúrok ešte hráč 'player' má na ploche.
     */
    public int countPieces(Player player) {
        int count = 0;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getOwner() == player) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Vypíše hraciu plochu do konzoly (ASCII-art), vrátane označenia možných ťahov.
     */
    public void printBoard(String whiteName, String blackName,
                           int whiteScore, int blackScore, Player currentPlayer) {
        System.out.printf("Biely: %s (body=%d)\n", whiteName, whiteScore);
        System.out.printf("Čierny: %s (body=%d)\n", blackName, blackScore);
        System.out.println("Na ťahu: " + currentPlayer + "\n");

        // zistíme, kam sa dá aktuálne ťahať
        Set<Position> moznePolicka = new HashSet<>();
        List<Move> moves = getAllValidMoves(currentPlayer);
        for (Move m : moves) {
            moznePolicka.add(m.getTo());
        }

        // hlavička (stĺpce A..H)
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

        // riadky 8..1
        for (int r = 0; r < SIZE; r++) {
            int rowLabel = SIZE - r;
            System.out.printf(" %2d |", rowLabel);

            for (int c = 0; c < SIZE; c++) {
                Position pos = new Position(r, c);
                Piece p = getPiece(pos);

                String symbol;
                if (p != null) {
                    symbol = getColoredPieceChar(p);
                }
                else if (teleports.containsKey(pos)) {
                    symbol = "T"; // teleport
                }
                else if (moznePolicka.contains(pos)) {
                    symbol = "."; // možná cieľová pozícia
                }
                else {
                    symbol = " ";
                }

                System.out.printf(" %s |", symbol);
            }

            System.out.printf(" %2d\n", rowLabel);
            System.out.print("    +");
            for (int i = 0; i < SIZE; i++) {
                System.out.print("---+");
            }
            System.out.println();
        }

        // dolná hlavička (A..H)
        System.out.print("    ");
        for (char col = 'A'; col <= 'H'; col++) {
            System.out.printf("  %c ", col);
        }
        System.out.println("\n");
    }

    /**
     * Vracia textový symbol pre figúrku (biela = 'm' alebo 'K' bielym textom,
     * čierna = 'm' alebo 'K' červeným textom).
     */
    private String getColoredPieceChar(Piece piece) {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_WHITE = "\u001B[37m";
        final String ANSI_RED   = "\u001B[31m";

        char ch = (piece.getType() == PieceType.MAN) ? 'm' : 'K';
        if (piece.getOwner() == Player.WHITE) {
            return ANSI_WHITE + ch + ANSI_RESET;
        } else {
            return ANSI_RED + ch + ANSI_RESET;
        }
    }

    // -----------------------------------------------------------------------------------
    // OPRAVENÁ LÓGIKA PRE DÁMU (KING): môže ísť o ľubovoľný počet polí šikmo,
    // kým nenarazí na koniec, vlastnú figúrku alebo (potenciálne) súperovu na branenie.
    // -----------------------------------------------------------------------------------
    private List<Move> getKingMoves(Position pos) {
        List<Move> moves = new ArrayList<>();
        Piece p = getPiece(pos);
        if (p == null) return moves;

        // Dáma môže chodiť vo všetkých štyroch diagonálnych smeroch
        int[] dr = {-1, +1};
        int[] dc = {-1, +1};

        for (int rowDelta : dr) {
            for (int colDelta : dc) {
                int step = 1;
                while (true) {
                    int rr = pos.row + step * rowDelta;
                    int cc = pos.col + step * colDelta;
                    Position checkingPos = new Position(rr, cc);

                    if (!isWithinBounds(checkingPos)) {
                        break; // mimo dosky
                    }
                    Piece occupant = getPiece(checkingPos);

                    if (occupant == null) {
                        // žiadna figúrka => dá sa sem voľne ísť
                        moves.add(new Move(pos, checkingPos));
                        step++;
                    } else {
                        // Našli sme figúrku -> buď to je súper, a môžeme skúsiť brať...
                        if (occupant.getOwner() != p.getOwner()) {
                            int jumpR = rr + rowDelta;
                            int jumpC = cc + colDelta;
                            Position jumpPos = new Position(jumpR, jumpC);
                            if (isWithinBounds(jumpPos) && getPiece(jumpPos) == null) {
                                // Za súperovou figúrkou je voľné pole -> ťah s braním
                                moves.add(new Move(pos, jumpPos, checkingPos));
                            }
                        }
                        // Po nájdení prvej figúrky (vlastnej či cudzej) už nepokračujeme
                        break;
                    }
                }
            }
        }
        return moves;
    }

    // -----------------------------------------------------------------------------------
    // Pohyby pešiaka (MAN) ostávajú ako doteraz (len 1 krok vpred / 1 krok s braním).
    // -----------------------------------------------------------------------------------
    private List<Move> getManMoves(Position pos) {
        List<Move> moves = new ArrayList<>();
        Piece p = getPiece(pos);
        if (p == null) return moves;

        // Bieli idú hore (row--), čierni dole (row++)
        int dir = (p.getOwner() == Player.WHITE) ? -1 : +1;
        int[] dc = {-1, +1};

        for (int d : dc) {
            // jednoduchý krok
            Position next = new Position(pos.row + dir, pos.col + d);
            if (isWithinBounds(next) && getPiece(next) == null) {
                moves.add(new Move(pos, next));
            }
            // branenie (ak je za súperovou figúrkou voľno)
            Position mid = next;
            Position land = new Position(pos.row + 2*dir, pos.col + 2*d);
            if (isWithinBounds(mid) && isWithinBounds(land)) {
                Piece midPiece = getPiece(mid);
                if (midPiece != null && midPiece.getOwner() != p.getOwner() && getPiece(land) == null) {
                    moves.add(new Move(pos, land, mid));
                }
            }
        }
        return moves;
    }
}
