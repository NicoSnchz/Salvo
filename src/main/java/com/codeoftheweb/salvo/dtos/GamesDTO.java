package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Score;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class GamesDTO {
    private Long id;

    private LocalDateTime created;

    private Set<GamePlayerDTO> gamePlayers;

    private Set<ScoreDTO> scores;

    public GamesDTO() {
    }

    public GamesDTO(Game game) {
        this.id = game.getGameId();
        this.created = game.getGameStartDate();
        this.gamePlayers = game.getGamePlayers().stream().map(GamePlayerDTO::new).collect(Collectors.toSet());
        this.scores = game.getGamePlayers().stream().map(ScoreDTO::new).collect(Collectors.toSet());
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

    public Set<GamePlayerDTO> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayerDTO> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<ScoreDTO> getScores() {
        return scores;
    }

    public void setScores(Set<ScoreDTO> scores) {
        this.scores = scores;
    }
}
