package cz.vsb.fei.donkeykong.repository;

import cz.vsb.fei.donkeykong.entity.HighScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighScoreRepository extends JpaRepository<HighScore, Long> {

    List<HighScore> findByPlayerNameOrderByScoreDesc(String playerName);

    @Query(value = "SELECT * FROM Scores ORDER BY points DESC LIMIT 10", nativeQuery = true)
    List<HighScore> findTop10HighScores();

    Integer countByPlayerName(String playerName);
}

