package org.dama;

import org.dama.entity.Rating;
import org.dama.repository.RatingRepository;
import org.dama.service.RatingServiceJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RatingServiceJPATest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingServiceJPA ratingService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRating() {
        Rating rating = new Rating("Dama", "Hráč", 4, LocalDateTime.now());
        when(ratingRepository.findByGameAndPlayer("Dama", "Hráč"))
                .thenReturn(Optional.of(rating));
        ratingService.addRating(rating);
        verify(ratingRepository).save(rating);
        int value = ratingService.getRating("Dama", "Hráč");
        assertEquals(4, value);
    }

    @Test
    void testSetRating() {
        Rating existing = new Rating("Dama", "Test", 2, LocalDateTime.now());
        when(ratingRepository.findByGameAndPlayer("Dama", "Test"))
                .thenReturn(Optional.of(existing));
        Rating newRating = new Rating("Dama", "Test", 5, LocalDateTime.now());
        ratingService.setRating(newRating);
        verify(ratingRepository).save(argThat(r -> r.getPlayer().equals("Test") && r.getRating() == 5));
        when(ratingRepository.findByGameAndPlayer("Dama", "Test"))
                .thenReturn(Optional.of(new Rating("Dama", "Test", 5, LocalDateTime.now())));
        int current = ratingService.getRating("Dama", "Test");
        assertEquals(5, current);
    }

    @Test
    void testGetAverageRating() {
        when(ratingRepository.findAverageRatingByGame("Dama")).thenReturn(4.0);
        ratingService.addRating(new Rating("Dama", "A", 3, LocalDateTime.now()));
        ratingService.addRating(new Rating("Dama", "B", 5, LocalDateTime.now()));
        verify(ratingRepository, times(2)).save(any(Rating.class));
        int avg = ratingService.getAverageRating("Dama");
        assertEquals(4, avg);
    }

    @Test
    void testResetRatings() {
        ratingService.addRating(new Rating("Dama", "X", 1, LocalDateTime.now()));
        ratingService.reset();
        verify(ratingRepository).deleteAll();
        when(ratingRepository.findAverageRatingByGame("Dama")).thenReturn(null);
        int avgAfter = ratingService.getAverageRating("Dama");
        assertEquals(0, avgAfter);
    }
}
