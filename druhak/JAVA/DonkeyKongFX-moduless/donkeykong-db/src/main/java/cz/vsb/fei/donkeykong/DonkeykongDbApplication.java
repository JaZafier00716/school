package cz.vsb.fei.donkeykong;

import cz.vsb.fei.donkeykong.entity.GameResult;
import cz.vsb.fei.donkeykong.entity.Player;
import cz.vsb.fei.donkeykong.repository.GameResultRepository;
import cz.vsb.fei.donkeykong.repository.HighScoreRepository;
import cz.vsb.fei.donkeykong.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDateTime;

@SpringBootApplication
public class DonkeykongDbApplication {

	public static void main(String[] args) {
		if (System.getProperty("server.port") == null && System.getenv("SERVER_PORT") == null) {
			System.setProperty("server.port", String.valueOf(findPort(8080)));
		}
		SpringApplication.run(DonkeykongDbApplication.class, args);
	}

	@Bean
	CommandLineRunner backfillGameResults(HighScoreRepository highScoreRepository,
										 GameResultRepository gameResultRepository,
										 PlayerRepository playerRepository) {
		return args -> {
			highScoreRepository.findAll().forEach(highScore -> {
				if (highScore.getPlayedAt() == null) {
					highScore.setPlayedAt(LocalDateTime.now());
					highScoreRepository.save(highScore);
				}
				boolean exists = gameResultRepository.existsByPlayerNameAndScoreAndPlayedAt(
						highScore.getPlayerName(),
						highScore.getScore(),
						highScore.getPlayedAt()
				);
				if (!exists) {
					GameResult gameResult = new GameResult();
					gameResult.setPlayerName(highScore.getPlayerName());
					gameResult.setScore(highScore.getScore());
					gameResult.setPlayedAt(highScore.getPlayedAt());
					if (gameResult.getPlayerName() != null && !gameResult.getPlayerName().isBlank()) {
						setPlayer(gameResult, playerRepository);
					}
					gameResultRepository.save(gameResult);
				}
			});
			gameResultRepository.findAll().forEach(gameResult -> {
			boolean changed = false;
			if (gameResult.getPlayedAt() == null) {
				gameResult.setPlayedAt(LocalDateTime.now());
				changed = true;
			}
			if (gameResult.getPlayer() == null && gameResult.getPlayerName() != null && !gameResult.getPlayerName().isBlank()) {
				setPlayer(gameResult, playerRepository);
				changed = true;
			}
			if (changed) {
				gameResultRepository.save(gameResult);
			}
		});
		};
	}

	private static void setPlayer(GameResult gameResult, PlayerRepository playerRepository) {
		Player player = playerRepository.findByName(gameResult.getPlayerName())
				.orElseGet(() -> playerRepository.save(new Player(gameResult.getPlayerName())));
		gameResult.setPlayer(player);
	}

	private static int findPort(int port) {
		while (true) {
			try (ServerSocket ignored = new ServerSocket(port)) {
				return port;
			} catch (IOException e) {
				port++;
			}
		}
	}

}
