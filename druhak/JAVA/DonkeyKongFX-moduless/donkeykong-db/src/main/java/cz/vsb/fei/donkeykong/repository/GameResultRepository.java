package cz.vsb.fei.donkeykong.repository;

import cz.vsb.fei.donkeykong.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {

    List<GameResult> findAllByOrderByPlayedAtDesc();

    List<GameResult> findByPlayer_NameOrderByPlayedAtDesc(String playerName);

    List<GameResult> findByPlayer_NameOrderByScoreDesc(String playerName);

    List<GameResult> findTop10ByOrderByPlayedAtDesc();

}
