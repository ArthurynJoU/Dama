package org.dama;

import org.dama.entity.Rating;
import org.dama.service.RatingException;
import org.dama.service.RatingService;
import org.dama.service.RatingServiceJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RatingServiceJPATest {

    private RatingService ratingService;

    @BeforeEach
    public void init() {
        ratingService = new RatingServiceJPA();
        ratingService.reset();
    }

    @Test
    public void testAddRating() {
        Rating rating = new Rating("Dama", "Hráč", 4, LocalDateTime.now());
        ratingService.addRating(rating);

        int hodnotenie = ratingService.getRating("Dama", "Hráč");
        assertEquals(4, hodnotenie);
    }

    @Test
    public void testSetRating() {
        ratingService.addRating(new Rating("Dama", "Test", 2, LocalDateTime.now()));
        ratingService.setRating(new Rating("Dama", "Test", 5, LocalDateTime.now()));

        int aktualne = ratingService.getRating("Dama", "Test");
        assertEquals(5, aktualne);
    }

    @Test
    public void testGetAverageRating() {
        ratingService.addRating(new Rating("Dama", "A", 3, LocalDateTime.now()));
        ratingService.addRating(new Rating("Dama", "B", 5, LocalDateTime.now()));

        int priemer = ratingService.getAverageRating("Dama");
        assertEquals(4, priemer);
    }

    @Test
    public void testResetRatings() {
        ratingService.addRating(new Rating("Dama", "X", 1, LocalDateTime.now()));
        ratingService.reset();

        int priemer = ratingService.getAverageRating("Dama");
        assertEquals(0, priemer);
    }
}
