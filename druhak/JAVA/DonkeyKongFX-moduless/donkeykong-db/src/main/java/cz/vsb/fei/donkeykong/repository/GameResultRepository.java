package cz.vsb.fei.donkeykong.repository;

import cz.vsb.fei.donkeykong.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {

    List<GameResult> findByPlayerNameOrderByPlayedAtDesc(String playerName);

    @Query(value = "SELECT * FROM GameResult ORDER BY played_at DESC LIMIT 10", nativeQuery = true)
    List<GameResult> findLast10Games();

    Integer countByPlayerName(String playerName);
}


