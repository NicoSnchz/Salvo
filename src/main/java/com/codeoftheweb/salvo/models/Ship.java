package com.codeoftheweb.salvo.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long shipId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    private String type;

    @ElementCollection
    @Column(name="locations")
    private List<String> shipLocations = new ArrayList<>();


    //Constructor
    public Ship() {
    }

    public Ship(GamePlayer gamePlayer, String shipType, List<String> shipLocations) {
        this.gamePlayer = gamePlayer;
        this.type = shipType;
        this.shipLocations = shipLocations;
    }

    //GETTER
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public String getType() {
        return type;
    }

    public List<String> getShipLocations() {
        return shipLocations;
    }


    //SETTER
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setShipLocations(List<String> shipLocations) {
        this.shipLocations = shipLocations;
    }
}