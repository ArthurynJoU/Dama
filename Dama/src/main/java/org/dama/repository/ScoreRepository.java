package org.dama.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.dama.entity.Score;
import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findTop5ByGameOrderByPointsDesc(String game);
}
