package cz.vsb.fei.java2.lab08.repositories;

import cz.vsb.fei.java2.lab08.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByFirstName(String name);

    @Query("SELECT p FROM Player p WHERE p.dayOfBirth < :date")
    List<Player> findBeforeChristmas(LocalDate date);
}
