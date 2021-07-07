package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.SalvoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/games")
    public Map<String, Object> getGames() {
        List<Game> gameList = gameRepo.findAll();
        Map<String, Object> dto = new LinkedHashMap<>();
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
        gameDto.put("scores", game.getGamePlayers().stream().map(z ->
                                                                    {if (z.getScore().isPresent()) {
                                                                        return makeScoreDTO(z.getScore().get());
                                                                    }
                                                                    else {return "Partida en curso..."; }
                                                                    }).collect(Collectors.toList()));;
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