package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @OneToMany (mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Ship> ships;

    @OneToMany (mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Salvo> salvoes;


    private LocalDateTime joinDate;

    //Constructor
    public GamePlayer() {
    }

    public GamePlayer(Player player, Game game, LocalDateTime joinDate) {
        this.game = game;
        this.player = player;
        this.joinDate = joinDate;
    }

    //Add Salvos and Ships

    public void  addSalvoes(Salvo salvo){
        salvo.setGamePlayer(this);
        salvoes.add(salvo);
    }

    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    //GETTER
    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public Long getId() {
        return id;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public Optional<Score> getScore() {return this.player.getScore(game);}

    public  Optional<GamePlayer> getOpponent(GamePlayer gamePlayer){
        return gamePlayer.getGame().getGamePlayers().stream().filter(x -> !x.getId().equals(gamePlayer.getId())).findFirst();}

    //SETTER
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }
}