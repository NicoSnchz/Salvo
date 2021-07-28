package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.models.Salvo;

import java.util.List;

public class SalvoesDTO {
    private Integer turn;
    private Long player;
    private List<String> locations;

    public SalvoesDTO() {
    }

    public SalvoesDTO(Salvo salvo){
        this.turn = salvo.getTurn();
        this.player = salvo.getGamePlayer().getPlayer().getId();
        this.locations = salvo.getSalvoLocations();
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public Long getPlayer() {
        return player;
    }

    public void setPlayer(Long player) {
        this.player = player;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
