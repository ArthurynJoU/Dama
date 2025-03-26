package org.dama.service;

import org.dama.entity.Rating;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "5872";
    public static final String SELECT = "SELECT game, player, rating, ratedOn FROM rating WHERE game = ? ORDER BY rating DESC LIMIT 10";
    public static final String DELETE = "DELETE FROM rating";
    public static final String INSERT = "INSERT INTO rating (game, player, rating, ratedon) VALUES (?, ?, ?, ?)";
    public static final String UPDATE = "UPDATE rating SET rating = ?, ratedOn = ? WHERE game = ? AND player = ?";
    public static final String AVG_RATING = "SELECT AVG(rating) FROM rating WHERE game = ?";
    public static final String GET_RATING = "SELECT rating FROM rating WHERE game = ? AND player = ?";

    public void addRating(Rating rating) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, rating.getGame());
            statement.setString(2, rating.getPlayer());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Problem inserting rating", e);
        }
    }

    public List<Rating> getTopRatings(String game) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT)) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                List<Rating> ratings = new ArrayList<>();
                while (rs.next()) {
                    ratings.add(new Rating(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                }
                return ratings;
            }
        } catch (SQLException e) {
            throw new RatingException("Problem selecting ratings", e);
        }
    }

    @Override
    public void reset() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new RatingException("Problem deleting ratings", e);
        }
    }

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setInt(1, rating.getRating());
            statement.setTimestamp(2, new Timestamp(rating.getRatedOn().getTime()));
            statement.setString(3, rating.getGame());
            statement.setString(4, rating.getPlayer());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Problem updating rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(AVG_RATING)) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Problem calculating average rating", e);
        }
        return 0;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(GET_RATING)) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Problem retrieving rating", e);
        }
        return 0;
    }
}
