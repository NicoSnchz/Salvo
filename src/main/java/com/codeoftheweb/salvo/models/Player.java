package com.codeoftheweb.salvo.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.toList;


@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String userName;

    private String password;

    @OneToMany(mappedBy = "player", fetch= FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<Score> scores;

    //Constructor
    public Player() {}

    public Player (String userName, String password){
        this.userName = userName;
        this.password = password;
    }


    //Add GamePlayer
    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    //GETTER
    @JsonIgnore
    public List<Game> getGames() {
        return gamePlayers.stream().map(GamePlayer::getGame).collect(toList());
    }

    public Set<GamePlayer> getPlayers() {
        return gamePlayers;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public Optional<Score> getScore(Game game){
        return this.scores.stream().filter(s -> s.getGame().getGameId().equals(game.getGameId())).findFirst();
    }
    //SETTER
    public void setUserName (String userName){
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}