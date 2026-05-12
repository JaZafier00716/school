package cz.vsb.fei.donkeykong.repository;

import cz.vsb.fei.donkeykong.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {

    List<GameResult> findByPlayerNameOrderByPlayedAtDesc(String playerName);

    List<GameResult> findTop10ByOrderByPlayedAtDesc();

    Integer countByPlayerName(String playerName);
}

