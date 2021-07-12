package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
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
			Player admin = new Player("admin@admin",passwordEncoder.encode("admin"));
			Player player1 = new Player("player1@gmail.com",passwordEncoder.encode("24"));
			Player player2 = new Player("player2@gmail.com", passwordEncoder.encode("123"));
			Player player3 = new Player("player3@gmail.com", passwordEncoder.encode("hola"));
			Player player4 = new Player("player4@gmail.com", passwordEncoder.encode("kb"));
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
			playerRepository.save(admin);
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

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		//User Authentication
		auth.userDetailsService(inputUserName -> {
			Player player = playerRepository.findByUserName(inputUserName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputUserName);
			}
		});
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/api/game_view/**").hasAuthority("USER")
				.antMatchers("/web/games.html" , "/web/css/**", "/web/img/**", "/web/js/**").permitAll()
				.antMatchers("/api/games").permitAll()
				.antMatchers(HttpMethod.POST, "/api/players").permitAll();

		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}
