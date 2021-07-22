package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Salvo;
import com.codeoftheweb.salvo.models.Ship;
import com.codeoftheweb.salvo.utilities.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class HitRecordDTO {
    private Integer turn;
    private List<String> hitLocations;
    private damagesDTO damages;
    private Long missed;

    public HitRecordDTO() {
    }

    public HitRecordDTO(Salvo salvo){
        this.turn = salvo.getTurn();
        this.hitLocations = Util.hits(salvo);
        this.damages = new damagesDTO(salvo);
        this.missed = Util.missed(salvo);
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public List<String> getHitLocations() {
        return hitLocations;
    }

    public void setHitLocations(List<String> hitLocations) {
        this.hitLocations = hitLocations;
    }

    public damagesDTO getDamages() {
        return damages;
    }

    public void setDamages(damagesDTO damages) {
        this.damages = damages;
    }

    public Long getMissed() {
        return missed;
    }

    public void setMissed(Long missed) {
        this.missed = missed;
    }
}
