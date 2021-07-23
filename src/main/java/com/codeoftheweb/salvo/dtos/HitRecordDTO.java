package com.codeoftheweb.salvo.dtos;

import java.util.List;

public class HitRecordDTO {
    private Integer turn;
    private List<String> hitLocations;
    private DamagesDTO damages;
    private Long missed;

    public HitRecordDTO() {
    }

    public HitRecordDTO(Integer turn, List<String> hitLocations, DamagesDTO damages, Long missed) {
        this.turn = turn;
        this.hitLocations = hitLocations;
        this.damages = damages;
        this.missed = missed;
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

    public DamagesDTO getDamages() {
        return damages;
    }

    public void setDamages(DamagesDTO damages) {
        this.damages = damages;
    }

    public Long getMissed() {
        return missed;
    }

    public void setMissed(Long missed) {
        this.missed = missed;
    }
}
