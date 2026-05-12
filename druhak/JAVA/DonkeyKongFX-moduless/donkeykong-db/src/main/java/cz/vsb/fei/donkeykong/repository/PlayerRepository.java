package cz.vsb.fei.donkeykong.repository;

import cz.vsb.fei.donkeykong.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByName(String name);

    List<Player> findAllByOrderByNameAsc();
}
