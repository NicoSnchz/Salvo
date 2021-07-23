package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Ship;
import com.codeoftheweb.salvo.utilities.Util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GameViewDTO {
    private Long id;
    private LocalDateTime created;
    private String gameState;
    private Set<GamePlayerDTO> gamePlayers;
    private Set<ShipDTO> ships;
    private Set<SalvoesDTO> salvoes;
    private HitsDTO hits;

    public GameViewDTO() {

    }

    public GameViewDTO(Long id, LocalDateTime created, String gameState, Set<GamePlayerDTO> gamePlayers, Set<ShipDTO> ships, Set<SalvoesDTO> salvoes, HitsDTO hits) {
        this.id = id;
        this.created = created;
        this.gameState = gameState;
        this.gamePlayers = gamePlayers;
        this.ships = ships;
        this.salvoes = salvoes;
        this.hits = hits;
    }

    public GameViewDTO(LocalDateTime created, String gameState, Set<GamePlayerDTO> gamePlayers, Set<ShipDTO> ships, Set<SalvoesDTO> salvoes, HitsDTO hits) {
        this.created = created;
        this.gameState = gameState;
        this.gamePlayers = gamePlayers;
        this.ships = ships;
        this.salvoes = salvoes;
        this.hits = hits;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public Set<GamePlayerDTO> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayerDTO> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<ShipDTO> getShips() {
        return ships;
    }

    public void setShips(Set<ShipDTO> ships) {
        this.ships = ships;
    }

    public Set<SalvoesDTO> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<SalvoesDTO> salvoes) {
        this.salvoes = salvoes;
    }

    public HitsDTO getHits() {
        return hits;
    }

    public void setHits(HitsDTO hits) {
        this.hits = hits;
    }

}