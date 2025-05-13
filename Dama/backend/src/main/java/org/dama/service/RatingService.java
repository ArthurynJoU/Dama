package org.dama.service;

import org.dama.entity.Rating;
import org.dama.service.exception.RatingException;
import org.springframework.stereotype.Service;

@Service
public interface RatingService {
    void setRating(Rating rating) throws RatingException;
    int getAverageRating(String game) throws RatingException;
    int getRating(String game, String player) throws RatingException;

    void addRating(Rating rating);

    void reset() throws RatingException;
}
