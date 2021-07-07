package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository,
									  GameRepository gameRepository,
									  GamePlayerRepository gamePlayerRepository,
									  ShipRepository shipRepository,
									  SalvoRepository salvoRepository,
									  ScoreRepository scoreRepository) {
		return (args) -> {

			//Player: Email of the player
			Player player1 = new Player("player1@gmail.com");
			Player player2 = new Player("player2@gmail.com");
			Player player3 = new Player("player3@gmail.com");
			Player player4 = new Player("player4@gmail.com");
			//Game:  Time of creation.
			Game game1 = new Game(LocalDateTime.now());
			Game game2 = new Game(LocalDateTime.now().plusHours(1));
			//GamePlayer: Player, Game, Join Date.
			GamePlayer gamePlayer1 = new GamePlayer(player1, game1, LocalDateTime.now());
			GamePlayer gamePlayer2 = new GamePlayer(player2, game1, LocalDateTime.now());
			GamePlayer gamePlayer3 = new GamePlayer(player3, game2, LocalDateTime.now());
			GamePlayer gamePlayer4 = new GamePlayer(player4, game2, LocalDateTime.now());
			//Ships: GamePlayer, Type of ship, Locations of the ship in the board.
			Ship ship1 = new Ship(gamePlayer1, "Carrier", List.of("B5", "B6", "B7", "B8", "B9"));
			Ship ship2 = new Ship(gamePlayer1, "Submarine", List.of("F3", "G3", "H3"));
			Ship ship3 = new Ship(gamePlayer2, "Carrier", List.of("G1", "G2", "G3", "G4", "G5"));
			Ship ship4 = new Ship(gamePlayer2, "Submarine", List.of("J9", "I9", "H9"));
			Ship ship5 = new Ship(gamePlayer3, "Patrol Boat", List.of("J2", "I2"));
			Ship ship6 = new Ship(gamePlayer3, "Submarine", List.of("A1", "B1", "C1"));
			Ship ship7 = new Ship(gamePlayer4, "Battleship", List.of("H1", "H2", "H3", "H4"));
			Ship ship8 = new Ship(gamePlayer4, "Destroyer", List.of("J1", "J2", "J3"));
			//Salvoes: GamePlayer, Location of de Salvo, turn.
			Salvo salvo1 = new Salvo(gamePlayer1, List.of("D3", "B5", "C3"), 1);
			Salvo salvo2 = new Salvo(gamePlayer1, List.of("G2", "E7", "G1"), 2);
			Salvo salvo3 = new Salvo(gamePlayer1, List.of("H6","J6","A1"), 3);
			Salvo salvo4 = new Salvo(gamePlayer2, List.of("E9", "C2", "E1"), 1);
			Salvo salvo5 = new Salvo(gamePlayer2, List.of("B5", "B6", "B7"), 2);
			Salvo salvo6 = new Salvo(gamePlayer2, List.of("J1", "J2", "J3"), 3);
			Salvo salvo7 = new Salvo(gamePlayer3, List.of("E9", "C2", "E1"), 1);
			Salvo salvo8 = new Salvo(gamePlayer3, List.of("B5", "B6", "B7"), 2);
			Salvo salvo9 = new Salvo(gamePlayer4, List.of("E9", "C2", "E1"), 1);
			Salvo salvo0 = new Salvo(gamePlayer4, List.of("B5", "B6", "B7"), 2);
			//Scores:
			Score score1 = new Score(player1, game1, 1f, LocalDateTime.now().plusMinutes(30L));
			Score score2 = new Score(player2, game1, 0f, LocalDateTime.now().plusMinutes(30L));
			Score score3 = new Score(player3, game2, .5f, LocalDateTime.now().plusMinutes(30L));
			Score score4 = new Score(player4, game2, .5f, LocalDateTime.now().plusMinutes(30L));

			//Player Repository save
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			//Game Repository save
			gameRepository.save(game1);
			gameRepository.save(game2);
			//GamePlayer Repository save
			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			//Ship Repository save
			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);
			shipRepository.save(ship6);
			shipRepository.save(ship7);
			shipRepository.save(ship8);
			//Salvoes Repository save
			salvoRepository.save(salvo1);
			salvoRepository.save(salvo2);
			salvoRepository.save(salvo3);
			salvoRepository.save(salvo4);
			salvoRepository.save(salvo5);
			salvoRepository.save(salvo6);
			salvoRepository.save(salvo7);
			salvoRepository.save(salvo8);
			salvoRepository.save(salvo9);
			salvoRepository.save(salvo0);
			//Scores Repository save
			scoreRepository.save(score1);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
		};
	}
}