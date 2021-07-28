package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.utilities.Util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DamagesDTO {
    private Long carrierHits, battleshipHits, destroyerHits, patrolboatHits, submarineHits;
    private Long carrier, battleship, submarine, destroyer, patrolboat;

    public DamagesDTO() {
    }

    public DamagesDTO(Long carrierHits, Long battleshipHits, Long destroyerHits, Long patrolboatHits, Long submarineHits, Long carrier, Long battleship, Long submarine, Long destroyer, Long patrolboat) {
        this.carrierHits = carrierHits;
        this.battleshipHits = battleshipHits;
        this.destroyerHits = destroyerHits;
        this.patrolboatHits = patrolboatHits;
        this.submarineHits = submarineHits;
        this.carrier = carrier;
        this.battleship = battleship;
        this.submarine = submarine;
        this.destroyer = destroyer;
        this.patrolboat = patrolboat;
    }

    public Long getCarrierHits() {
        return carrierHits;
    }

    public void setCarrierHits(Long carrierHits) {
        this.carrierHits = carrierHits;
    }

    public Long getBattleshipHits() {
        return battleshipHits;
    }

    public void setBattleshipHits(Long battleshipHits) {
        this.battleshipHits = battleshipHits;
    }

    public Long getDestroyerHits() {
        return destroyerHits;
    }

    public void setDestroyerHits(Long destroyerHits) {
        this.destroyerHits = destroyerHits;
    }

    public Long getPatrolboatHits() {
        return patrolboatHits;
    }

    public void setPatrolboatHits(Long patrolboatHits) {
        this.patrolboatHits = patrolboatHits;
    }

    public Long getSubmarineHits() {
        return submarineHits;
    }

    public void setSubmarineHits(Long submarineHits) {
        this.submarineHits = submarineHits;
    }

    public Long getCarrier() {
        return carrier;
    }

    public void setCarrier(Long carrier) {
        this.carrier = carrier;
    }

    public Long getBattleship() {
        return battleship;
    }

    public void setBattleship(Long battleship) {
        this.battleship = battleship;
    }

    public Long getSubmarine() {
        return submarine;
    }

    public void setSubmarine(Long submarine) {
        this.submarine = submarine;
    }

    public Long getDestroyer() {
        return destroyer;
    }

    public void setDestroyer(Long destroyer) {
        this.destroyer = destroyer;
    }

    public Long getPatrolboat() {
        return patrolboat;
    }

    public void setPatrolboat(Long patrolboat) {
        this.patrolboat = patrolboat;
    }
}
