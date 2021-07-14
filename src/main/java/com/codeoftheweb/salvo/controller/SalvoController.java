package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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

    //Regresa el Player actualmente Logeado
    public Player currentPlayer(Authentication authentication){
        return playerRepository.findByUserName(authentication.getName());
    }

    //Pregunta si esta logeado o es Guest.
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    //Recibe una lista de ships objects y la guarda
    @PostMapping("/games/players/{gpid}/ships")
    public ResponseEntity<Map<String, Object>> saveShips(@PathVariable Long gpid, @RequestBody Set<Ship> ships, Authentication authentication){
        GamePlayer gamePlayer = gamePlayerRepo.findById(gpid).orElse(null);
        Player player = currentPlayer(authentication);

        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "User not logged"), HttpStatus.UNAUTHORIZED);
        }else if (gamePlayer == null){
            return new ResponseEntity<>(makeMap("error", "No such Gameplayer"), HttpStatus.UNAUTHORIZED);
        }else if (!gamePlayer.getPlayer().getId().equals(player.getId())){
            return new ResponseEntity<>(makeMap("error", "Player do not match Gameplayer"), HttpStatus.UNAUTHORIZED);
        }

        if (!ships.isEmpty()){
            return new ResponseEntity<>(makeMap("error", "Ships already Located"), HttpStatus.FORBIDDEN);
        }else {
            for (Ship ship:ships){
                shipRepository.save(new Ship(gamePlayer, ship.getShipType(), ship.getShipLocations()));
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }


    //Registro de nuevo player, con sus response entity
    @PostMapping("/players")
    public ResponseEntity<Map<String, Object>> register(@RequestParam String email, @RequestParam String password){
        if (email.isEmpty()){
            return new ResponseEntity<>(makeMap("error", "Missing name"), HttpStatus.FORBIDDEN);
        }else if (password.isEmpty()){
            return new ResponseEntity<>(makeMap("error", "Missing password"), HttpStatus.FORBIDDEN);
        }

        Player player = playerRepository.findByUserName(email);

        if (player != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exist"), HttpStatus.CONFLICT);
        }

        Player newPlayer = playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("name", newPlayer.getUserName()), HttpStatus.CREATED);
    }


    //Unirse a una partida
    @PostMapping("/game/{id}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long id, Authentication authentication){
        Optional<Game> game = gameRepo.findById(id);

        if (isGuest(authentication)){ //Pregunta si es un Guest

            return new ResponseEntity<>(makeMap("error", "Unauthorized to enter"), HttpStatus.UNAUTHORIZED);

        }else if (game.isEmpty()){ //Pregunta si el Game existe

            return new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.FORBIDDEN);

        }else if (game.get().getGamePlayers().size() == 2){ //Pregunta si la partida esta llena.

            return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);

        }else {

            GamePlayer newGamePlayer = gamePlayerRepo.save(new GamePlayer(currentPlayer(authentication), game.get(), LocalDateTime.now()));
            return new ResponseEntity<>(makeMap("gpid",  newGamePlayer.getId()), HttpStatus.CREATED);
        }
    }

    //Mostrar todos los juegos
    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {
        List<Game> gameList = gameRepo.findAll();
        Map<String, Object> dto = new LinkedHashMap<>();

        //Si el usuario no esta logeado se lo toma como Guest.
        if (isGuest(authentication)){
            dto.put("player", "Guest");
        }else{
            dto.put("player", makePlayerDTO(currentPlayer(authentication)));
        }
        dto.put("games", gameList.stream().map(this::makeGamesDTO).collect(Collectors.toList()));
        return dto;
    }

    //Crear una partida
    @PostMapping("/games")
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication){
        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "Log-in to create a game"), HttpStatus.UNAUTHORIZED);
        }else{
            Game game = gameRepo.save(new Game(LocalDateTime.now()));
            GamePlayer gamePlayer = gamePlayerRepo.save(new GamePlayer(currentPlayer(authentication), game, LocalDateTime.now()));
            return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        }
    }

    //Mostrar la game view de un Player
    @RequestMapping("/game_view/{id}")
    public ResponseEntity<Map<String, Object>>  getGamePlayerId(@PathVariable Long id, Authentication authentication){
        Player player = currentPlayer(authentication);
        GamePlayer gamePlayer = gamePlayerRepo.findById(id).get();
        Game game = gamePlayer.getGame();
        if (player.getId().equals(gamePlayer.getPlayer().getId())){ //Compara si son iguales la id del player con la id del player dentro de gamePlayer
            Map<String, Object> salvoShipDTO = makeGameDTOaux(game);
            salvoShipDTO.put("ships", gamePlayer.getShips().stream().map(this::makeShipDTO).collect(Collectors.toList()));
            salvoShipDTO.put("salvoes", game.getGamePlayers().stream().flatMap(x -> x.getSalvoes().stream().map(this::makeSalvoesDTO)).collect(Collectors.toList()));
            salvoShipDTO.put("hits", makeHitDTO());
            return new ResponseEntity<>(salvoShipDTO, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(makeMap("error", "Unauthorized access"), HttpStatus.UNAUTHORIZED);
        }
    }


    /**DATA TRANSFER OBJECTS (DTO)*/
    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Map<String, Object> makeGameDTOaux(Game game) {
        Map<String, Object> gameDto = new LinkedHashMap<>();
        gameDto.put("id", game.getGameId());
        gameDto.put("created", game.getGameStartDate());
        gameDto.put("gameState", "PLACESHIPS");
        gameDto.put("gamePlayers", game.getGamePlayers().stream().map(this::makeGamePlayerDTO).collect(Collectors.toList()));
        return gameDto;
    }

    private Map<String, Object> makeGamesDTO(Game game) {
        Map<String, Object> gamesDto = new LinkedHashMap<>();
        gamesDto.put("id", game.getGameId());
        gamesDto.put("created", game.getGameStartDate());
        gamesDto.put("gamePlayers", game.getGamePlayers().stream().map(this::makeGamePlayerDTO).collect(Collectors.toList()));
        gamesDto.put("scores", game.getGamePlayers().stream().map(gamePlayer ->
                                                                    {if (gamePlayer.getScore().isPresent()) {
                                                                        return makeScoreDTO(gamePlayer.getScore().get());
                                                                    }
                                                                    else {return "partida en curso";}
                                                                    }).collect(Collectors.toList()));
        return gamesDto;
    }

    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> gamePlayerDto = new LinkedHashMap<>();
        gamePlayerDto.put("id", gamePlayer.getId());
        gamePlayerDto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
        return gamePlayerDto;
    }

    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> playerDto = new LinkedHashMap<>();
        playerDto.put("id", player.getId());
        playerDto.put("email", player.getUserName());
        return playerDto;
    }

    private Map<String, Object> makeShipDTO(Ship ship) {
        Map<String, Object> shipDTO = new LinkedHashMap<>();
        shipDTO.put("type", ship.getShipType());
        shipDTO.put("locations", ship.getShipLocations());
        return shipDTO;
    }

    private Map<String, Object> makeSalvoesDTO(Salvo salvo){
        Map<String, Object> salvoDto = new LinkedHashMap<>();
        salvoDto.put("turn", salvo.getTurn());
        salvoDto.put("player", salvo.getGamePlayer().getPlayer().getId());
        salvoDto.put("locations", salvo.getSalvoLocations());
        return salvoDto;
    }
    private Map<String, Object> makeScoreDTO(Score score){
        Map<String, Object> scoreDto = new LinkedHashMap<>();
        scoreDto.put("player", score.getPlayer().getId());
        scoreDto.put("score", score.getScore());
        scoreDto.put("finishDate", score.getFinishDate());
        return scoreDto;
    }

    private Map<String, Object> makeHitDTO(){
        List<String> selfHitLocations = new ArrayList<>();
        List<String> opponentHitLocations = new ArrayList<>();
        Map<String, Object> hitDto = new LinkedHashMap<>();
        hitDto.put("self", selfHitLocations);
        hitDto.put("opponent", opponentHitLocations);
        return hitDto;
    }
}