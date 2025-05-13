package org.dama.service.impl.jpa;

import org.dama.service.exception.RatingException;
import org.dama.service.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(RatingServiceJPA.class);

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingServiceJPA(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }


    @Override
    @Transactional
    public void setRating(Rating rating) {
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
            log.error("Problem setting rating for game {} and player {}",
                    rating.getGame(), rating.getPlayer(), e);
            throw new RatingException("Problem setting rating", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int getAverageRating(String game) {
        try {
            // Predpokladáme, že RatingRepository obsahuje JPQL dotaz:
            // @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.game = ?1")
            // Double findAverageRatingByGame(String game);
            Double avg = ratingRepository.findAverageRatingByGame(game);
            return avg != null ? avg.intValue() : 0;
        } catch (Exception e) {
            log.error("Problem calculating average rating for game {}", game, e);
            throw new RatingException("Problem calculating average rating", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int getRating(String game, String player) {
        try {
            Rating rating = ratingRepository.findByGameAndPlayer(game, player).orElse(null);
            return rating != null ? rating.getRating() : 0;
        } catch (Exception e) {
            log.error("Problem retrieving rating for game '{}' and player '{}'", game, player, e);
            throw new RatingException("Problem retrieving rating", e);
        }
    }

    @Override
    @Transactional
    public void addRating(Rating rating) {
        try {
            ratingRepository.save(rating);
        } catch (Exception e) {
            log.error("Problem inserting rating for game '{}' and player '{}'",
                    rating.getGame(), rating.getPlayer(), e);
            throw new RatingException("Problem inserting rating", e);
        }
    }


    @Override
    @Transactional
    public void reset() {
        try {
            ratingRepository.deleteAll();
        } catch (Exception e) {
            log.error("Problem deleting all ratings", e);
            throw new RatingException("Problem deleting ratings", e);
        }
    }
}
