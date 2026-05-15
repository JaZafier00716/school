package cz.vsb.fei.donkeykong.repository;

import cz.vsb.fei.donkeykong.entity.GameLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameLevelRepository extends JpaRepository<GameLevel, Long> {

    Optional<GameLevel> findByLevelNumber(Integer levelNumber);

    List<GameLevel> findAllByOrderByLevelNumberAsc();
}
