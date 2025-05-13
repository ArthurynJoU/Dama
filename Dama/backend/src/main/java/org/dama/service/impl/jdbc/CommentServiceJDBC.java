package org.dama.service.impl.jdbc;

import org.dama.entity.Comment;
import org.dama.service.exception.CommentException;
import org.dama.service.CommentService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {
    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "12345678";
    public static final String INSERT = "INSERT INTO comment (game, player, comment, commentedon) VALUES (?, ?, ?, ?)";
    public static final String SELECT = "SELECT game, player, comment, commentedon FROM comment WHERE game = ? ORDER BY commentedon DESC";
    public static final String DELETE = "DELETE FROM comment";

    @Override
    public void addComment(Comment comment) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, comment.getGame());
            statement.setString(2, comment.getPlayer());
            statement.setString(3, comment.getComment());
            // Povedzme, že v entite Comment máš getCommentedOn() -> LocalDateTime
            statement.setTimestamp(4, Timestamp.valueOf(comment.getCommentedOn()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Problem inserting comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT)) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                List<Comment> comments = new ArrayList<>();
                while (rs.next()) {
                    // Správna konverzia Timestamp -> LocalDateTime
                    LocalDateTime ldt = rs.getTimestamp(4).toLocalDateTime();
                    // Pridanie komentára do zoznamu
                    comments.add(new Comment(
                            rs.getString(1), // game
                            rs.getString(2), // player
                            rs.getString(3), // comment
                            ldt             // commentedOn
                    ));
                }
                return comments;
            }
        } catch (SQLException e) {
            throw new CommentException("Problem retrieving comments", e);
        }
    }


    @Override
    public void reset() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new CommentException("Problem deleting comments", e);
        }
    }
}
