package cz.vsb.fei.java2.lab08.controllers;

import cz.vsb.fei.java2.lab08.entities.Player;
import cz.vsb.fei.java2.lab08.repositories.PlayerRepository;
import jakarta.websocket.ClientEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;


    @GetMapping({"/", ""})
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @GetMapping("/name/{name}")
    public List<Player> findByName(@PathVariable String name) {
        return playerRepository.findByFirstName(name);
    }


    @GetMapping("/before")
    public List<Player> findBeforeChristmas() {
        return playerRepository.findBeforeChristmas(LocalDate.of(2025, 12, 24));
    }

    @PostMapping({"/", ""})
    public Player save(@RequestBody Player player) {
        return playerRepository.save(player);
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<String> delete(@PathVariable Long id) {
        playerRepository.deleteById(id);
        return ResponseEntity.ok("Player deleted");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable Long id) {
        Player player = playerRepository.findById(id).orElse(null);
        if (player != null) {
            return ResponseEntity.ok(player);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
        }
    }


}
