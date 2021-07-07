package com.codeoftheweb.salvo.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long gameId;

    private LocalDateTime gameStartDate;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<Score> scores;

    //Constructor
    public Game() {
    }

    public Game (LocalDateTime date){
        this.gameStartDate = date;
    }

    @JsonIgnore
    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }

    //GETTER
    @JsonIgnore
    public List<Player> getPlayers() {
        return gamePlayers.stream().map(GamePlayer::getPlayer).collect(toList());
    }

    public LocalDateTime getGameStartDate() {
        return gameStartDate;
    }

    public Long getGameId() {
        return gameId;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    //SETTER
    public void setGameStartDate(LocalDateTime gameStartDate) {
        this.gameStartDate = gameStartDate;
    }
}