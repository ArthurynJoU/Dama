package org.dama.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.dama.entity.Rating;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByGameAndPlayer(String game, String player);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.game = ?1")
    Double findAverageRatingByGame(String game);
}
