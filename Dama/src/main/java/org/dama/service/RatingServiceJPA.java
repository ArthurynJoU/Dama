package org.dama.service;

import org.dama.entity.Rating;
import org.dama.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("server")
@Transactional
public class RatingServiceJPA implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public void setRating(Rating rating) throws RatingException {
        try {
            // Najprv sa pokúsime nájsť existujúce hodnotenie pre danú hru a hráča
            Rating existing = ratingRepository.findByGameAndPlayer(rating.getGame(), rating.getPlayer()).orElse(null);
            if (existing != null) {
                // Ak existuje, aktualizujeme hodnotenie a čas
                existing.setRating(rating.getRating());
                existing.setRatedOn(rating.getRatedOn());
                ratingRepository.save(existing);
            } else {
                // Inak vložíme nové hodnotenie
                ratingRepository.save(rating);
            }
        } catch (Exception e) {
            throw new RatingException("Problem setting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try {
            // Predpokladáme, že RatingRepository obsahuje JPQL dotaz:
            // @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.game = ?1")
            // Double findAverageRatingByGame(String game);
            Double avg = ratingRepository.findAverageRatingByGame(game);
            return avg != null ? avg.intValue() : 0;
        } catch (Exception e) {
            throw new RatingException("Problem calculating average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            Rating rating = ratingRepository.findByGameAndPlayer(game, player).orElse(null);
            return rating != null ? rating.getRating() : 0;
        } catch (Exception e) {
            throw new RatingException("Problem retrieving rating", e);
        }
    }

    @Override
    public void addRating(Rating rating) {
        try {
            ratingRepository.save(rating);
        } catch (Exception e) {
            throw new RatingException("Problem inserting rating", e);
        }
    }


    @Override
    public void reset() throws RatingException {
        try {
            ratingRepository.deleteAll();
        } catch (Exception e) {
            throw new RatingException("Problem deleting ratings", e);
        }
    }
}
