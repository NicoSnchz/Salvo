package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Salvo;
import com.codeoftheweb.salvo.models.Ship;

import java.util.Set;

public class GamePlayerDTO {
    private Long id;
    private PlayerDTO player;

    public GamePlayerDTO() {
    }

    public GamePlayerDTO(GamePlayer gamePlayer){
        this.id = gamePlayer.getId();
        this.player = new PlayerDTO(gamePlayer.getPlayer());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }
}
