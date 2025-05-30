package org.dama.service.impl.jdbc;

import org.dama.entity.Score;
import org.dama.service.exception.ScoreException;
import org.dama.service.ScoreService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreServiceJDBC implements ScoreService {

    private static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345678";

    private static final String INSERT_SQL =
            "INSERT INTO score (game, player, points, playedon) VALUES (?,?,?,?)";

    private static final String SELECT_SQL =
            "SELECT game, player, points, playedon FROM score WHERE game=? ORDER BY points DESC LIMIT ?";

    private static final String DELETE_SQL = "DELETE FROM score";

    @Override
    public void addScore(Score score) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Najprv zistíme, či už hráč existuje
            String checkSql = "SELECT points FROM score WHERE game=? AND player=?";
            try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                psCheck.setString(1, score.getGame());
                psCheck.setString(2, score.getPlayer());
                ResultSet rs = psCheck.executeQuery();
                if (rs.next()) {
                    int oldPoints = rs.getInt(1);
                    // ak je nový počet bodov lepší, tak update, inak nerobíme nič
                    if (score.getPoints() > oldPoints) {
                        String updateSql = "UPDATE score SET points=?, playedon=? WHERE game=? AND player=?";
                        try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                            psUpdate.setInt(1, score.getPoints());
                            // Konverzia z LocalDateTime na Timestamp
                            psUpdate.setTimestamp(2, Timestamp.valueOf(score.getPlayedOn()));
                            psUpdate.setString(3, score.getGame());
                            psUpdate.setString(4, score.getPlayer());
                            psUpdate.executeUpdate();
                        }
                    }
                } else {
                    // Záznam neexistuje => vložíme
                    try (PreparedStatement psInsert = conn.prepareStatement(INSERT_SQL)) {
                        psInsert.setString(1, score.getGame());
                        psInsert.setString(2, score.getPlayer());
                        psInsert.setInt(3, score.getPoints());
                        // Konverzia z LocalDateTime na Timestamp
                        psInsert.setTimestamp(4, Timestamp.valueOf(score.getPlayedOn()));
                        psInsert.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new ScoreException("Chyba pri pridávaní výsledku", e);
        }
    }

    @Override
    public List<Score> getTopScores(String game, int limit) {
        List<Score> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(SELECT_SQL)) {
            ps.setString(1, game);
            ps.setInt   (2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Score s = new Score();
                    s.setGame(rs.getString(1));
                    s.setPlayer(rs.getString(2));
                    s.setPoints(rs.getInt(3));
                    // Konverzia z Timestamp na LocalDateTime
                    s.setPlayedOn(rs.getTimestamp(4).toLocalDateTime());
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            throw new ScoreException("Chyba pri čítaní výsledkov", e);
        }
        return list;
    }

    @Override
    public void reset() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement()) {
            st.executeUpdate(DELETE_SQL);
        } catch (SQLException e) {
            throw new ScoreException("Chyba pri resetovaní tabuľky skóre", e);
        }
    }
}
