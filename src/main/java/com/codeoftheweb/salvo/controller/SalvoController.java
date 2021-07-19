package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.dtos.*;
import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.*;
import com.codeoftheweb.salvo.utilities.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @Autowired
    private SalvoRepository salvoRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShipRepository shipRepository;

    /**  Recibe una lista de ships objects y la guarda */

    @PostMapping("/games/players/{gpid}/ships")
    public ResponseEntity<Object> saveShips(@PathVariable Long gpid, @RequestBody Set<Ship> ships, Authentication authentication){
        GamePlayer gamePlayer = gamePlayerRepo.findById(gpid).orElse(null);

        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "User not logged"), HttpStatus.UNAUTHORIZED);
        }
        Player player = playerRepository.findByUserName(authentication.getName());

        if (gamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error", "No such Gameplayer"), HttpStatus.UNAUTHORIZED);
        }

        if (!gamePlayer.getPlayer().getId().equals(player.getId())){
            return new ResponseEntity<>(Util.makeMap("error", "Player do not match Gameplayer"), HttpStatus.UNAUTHORIZED);
        }

        Set<Ship> shipSet = gamePlayer.getShips();
        //Chequea si ya tiene barcos ubicados
        if (shipSet.size() == 5){
            return new ResponseEntity<>(Util.makeMap("error", "Ships already Located"), HttpStatus.FORBIDDEN);
        }

        //Chequea que solo ubique 5 barcos
        if(ships.size() < 5){
            return new ResponseEntity<>(Util.makeMap("error", "Must place 5 ships"), HttpStatus.FORBIDDEN);
        }
        //Chequea si no ubique mas de 5 barcos
        if(ships.size() > 5){
            return new ResponseEntity<>(Util.makeMap("error", "Can only place 5 ships"), HttpStatus.FORBIDDEN);
        }

        for (Ship ship:ships){
            shipRepository.save(new Ship(gamePlayer, ship.getType(), ship.getShipLocations()));
        }
        return new ResponseEntity<>(Util.makeMap("OK", "Ships placed"),HttpStatus.CREATED);

    }
    /** Recibe una lista de disparos y la gurda */

    @PostMapping("/games/players/{gpid}/salvoes")
    public ResponseEntity<Object> saveSalvoes(@PathVariable Long gpid, @RequestBody Salvo salvo, Authentication authentication){
        GamePlayer gamePlayer = gamePlayerRepo.findById(gpid).orElse(null);


        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "User not logged"), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName());

        if (gamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error", "No such Gameplayer"), HttpStatus.UNAUTHORIZED);
        }

        if (!gamePlayer.getPlayer().getId().equals(player.getId())){
            return new ResponseEntity<>(Util.makeMap("error", "Player do not match Gameplayer"), HttpStatus.UNAUTHORIZED);
        }

        GamePlayer opponent = gamePlayer.getOpponent(gamePlayer).orElse(null);

        if (opponent == null){
            return new ResponseEntity<>(Util.makeMap("error", "There's no opponent"), HttpStatus.FORBIDDEN);
        }

        if (gamePlayer.getSalvoes().size() != opponent.getSalvoes().size() && gamePlayer.getSalvoes().size() > opponent.getSalvoes().size()){
            return new ResponseEntity<>(Util.makeMap("error", "Salvoes already submitted for turn "+ gamePlayer.getSalvoes().size()), HttpStatus.FORBIDDEN);
        }

        if (salvo.getSalvoLocations().size() == 0){
            return new ResponseEntity<>(Util.makeMap("error", "Must fire at least 1 salvo"), HttpStatus.FORBIDDEN);
        }
        if (salvo.getSalvoLocations().size() > 5){
            return new ResponseEntity<>(Util.makeMap("error", "Only can fire 5 salvoes in a turn"), HttpStatus.FORBIDDEN);
        }

        Integer salvoTurn = gamePlayer.getSalvoes().size() + 1;

        salvoRepo.save(new Salvo(gamePlayer, salvo.getSalvoLocations(), salvoTurn));

        return new ResponseEntity<>(Util.makeMap("OK", "Salvo fired!"),HttpStatus.CREATED);
    }


    /** Registro de nuevo player, con sus response entity */

    @PostMapping("/players")
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password){
        if (email.isEmpty()){
            return new ResponseEntity<>(Util.makeMap("error", "Missing name"), HttpStatus.FORBIDDEN);
        }else if (password.isEmpty()){
            return new ResponseEntity<>(Util.makeMap("error", "Missing password"), HttpStatus.FORBIDDEN);
        }

        Player player = playerRepository.findByUserName(email);

        if (player != null) {
            return new ResponseEntity<>(Util.makeMap("error", "Username already exist"), HttpStatus.CONFLICT);
        }

        Player newPlayer = playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(Util.makeMap("name", newPlayer.getUserName()), HttpStatus.CREATED);
    }

    /** Unirse a una partida */

    @PostMapping("/game/{id}/players")
    public ResponseEntity<Object> joinGame(@PathVariable Long id, Authentication authentication){
        Optional<Game> game = gameRepo.findById(id);
        GamePlayer gamePlayer = game.get().getGamePlayers().stream().findFirst().get();

        if (Util.isGuest(authentication)){ //Pregunta si es un Guest
            return new ResponseEntity<>(Util.makeMap("error", "Unauthorized to enter"), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName());

        if (game.isEmpty()){ //Pregunta si el Game existe
            return new ResponseEntity<>(Util.makeMap("error", "No such game"), HttpStatus.FORBIDDEN);
        }
        if (game.get().getGamePlayers().size() == 2){ //Pregunta si la partida esta llena.
            return new ResponseEntity<>(Util.makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);
        }


        if (player.getId().equals(gamePlayer.getPlayer().getId())){ //Pregunta si el player que intenta unirse es el mismo que el que ya esta dentro
            return new ResponseEntity<>(Util.makeMap("error", "Same player can't join twice!"), HttpStatus.CONFLICT);
        }else{
            GamePlayer newGamePlayer = gamePlayerRepo.save(new GamePlayer(player, game.get(), LocalDateTime.now()));
            return new ResponseEntity<>(Util.makeMap("gpid",  newGamePlayer.getId()), HttpStatus.CREATED);
        }
    }

    /** Mostrar todos los juegos */

    @GetMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {
        List<Game> gameList = gameRepo.findAll();
        Map<String, Object> dto = new LinkedHashMap<>();

        //Si el usuario no esta logeado se lo toma como Guest.
        if (Util.isGuest(authentication)){
            dto.put("player", "Guest");
        }else{
            Player player = playerRepository.findByUserName(authentication.getName());
            dto.put("player", new PlayerDTO(player));
        }
        dto.put("games", gameList.stream().map(GamesDTO::new).collect(Collectors.toList()));
        return dto;
    }

    /** Crear una partida */

    @PostMapping("/games")
    public ResponseEntity<Object> createGame(Authentication authentication){
        if (Util.isGuest(authentication)){
            return new ResponseEntity<>(Util.makeMap("error", "Log-in to create a game"), HttpStatus.UNAUTHORIZED);
        }else{
            Player player = playerRepository.findByUserName(authentication.getName());
            Game game = gameRepo.save(new Game(LocalDateTime.now()));
            GamePlayer gamePlayer = gamePlayerRepo.save(new GamePlayer(player, game, LocalDateTime.now()));
            return new ResponseEntity<>(Util.makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }
    }

    /** Mostrar la game view de un Player */

    @GetMapping("/game_view/{id}")
    public ResponseEntity<?>  getGamePlayerId(@PathVariable Long id, Authentication authentication){
        GamePlayer gamePlayer = gamePlayerRepo.findById(id).orElse(null);
        Player player = playerRepository.findByUserName(authentication.getName());

        if (gamePlayer == null){
            return new ResponseEntity<>(Util.makeMap("error", "there's not such Gameplayer"), HttpStatus.NOT_FOUND);
        }

        if (player.getId().equals(gamePlayer.getPlayer().getId())){ //Compara si son iguales la id del player con la id del player dentro de gamePlayer
            return new ResponseEntity<>(new GameViewDTO(gamePlayer), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(Util.makeMap("error", "Unauthorized access"), HttpStatus.UNAUTHORIZED);
        }
    }
}