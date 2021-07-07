package com.codeoftheweb.salvo.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long salvoID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="salvoLocations")
    private List<String> salvoLocations = new ArrayList<>();

    private Integer turn;

    //CONSTRUCTOR
    public Salvo() {
    }

    public Salvo(GamePlayer gamePlayer, List<String> salvoLocations, Integer turn) {
        this.gamePlayer = gamePlayer;
        this.salvoLocations = salvoLocations;
        this.turn = turn;
    }

    //GETTER
    public Long getSalvoID() {
        return salvoID;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public Integer getTurn() {
        return turn;
    }

    //SETTER

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }
}
