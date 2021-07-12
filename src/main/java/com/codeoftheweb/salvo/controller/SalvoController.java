package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.SalvoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    public Player currentPlayer(Authentication authentication){
        return playerRepository.findByUserName(authentication.getName());
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @PostMapping("/players")
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password){
        if (email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (playerRepository.findByUserName(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

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
        dto.put("games", gameList.stream().map(this::makeGameDTO).collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping("/game_view/{id}")
    private Map<String, Object>  getGamePlayerId(@PathVariable Long id){
        GamePlayer gamePlayer = gamePlayerRepo.findById(id).get();
        Game game = gamePlayer.getGame();
        Map<String, Object> salvoShipDTO = makeGameDTO(game);
        salvoShipDTO.put("ships", gamePlayer.getShips().stream().map(this::makeShipDTO).collect(Collectors.toList()));
        salvoShipDTO.put("salvoes", game.getGamePlayers().stream().flatMap(x -> x.getSalvoes().stream().map(this::makeSalvoesDTO)).collect(Collectors.toList()));
        return salvoShipDTO;
    }

    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> gameDto = new LinkedHashMap<>();
        gameDto.put("id", game.getGameId());
        gameDto.put("created", game.getGameStartDate());
        gameDto.put("gamePlayers", game.getGamePlayers().stream().map(this::makeGamePlayerDTO).collect(Collectors.toList()));
        gameDto.put("scores", game.getGamePlayers().stream().map(gamePlayer ->
                                                                    {if (gamePlayer.getScore().isPresent()) {
                                                                        return makeScoreDTO(gamePlayer.getScore().get());
                                                                    }
                                                                    else {return null; }
                                                                    }).collect(Collectors.toList()));
                                                                    return gameDto;
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
}