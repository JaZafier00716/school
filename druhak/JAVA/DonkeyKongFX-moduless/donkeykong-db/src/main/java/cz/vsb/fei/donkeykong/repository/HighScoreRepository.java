package cz.vsb.fei.donkeykong.repository;

import cz.vsb.fei.donkeykong.entity.HighScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighScoreRepository extends JpaRepository<HighScore, Long> {

    List<HighScore> findByPlayerNameOrderByScoreDesc(String playerName);

    List<HighScore> findTop10ByOrderByScoreDesc();

    List<HighScore> findTop10ByOrderByIdDesc();

    Integer countByPlayerName(String playerName);
}
