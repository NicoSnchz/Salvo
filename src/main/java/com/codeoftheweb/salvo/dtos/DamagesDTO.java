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

    /* public DamagesDTO(Salvo salvo){
        GamePlayer gamePlayer = salvo.getGamePlayer();
        GamePlayer opponent = gamePlayer.getOpponent(gamePlayer).orElse(null);

        if (opponent != null){

            //consigue cada barco del oponente.
            Ship carrier = opponent.getShips().stream().filter(ship -> ship.getType().equals("carrier")).findFirst().orElse(null);
            Ship battleship = opponent.getShips().stream().filter(ship -> ship.getType().equals("battleship")).findFirst().orElse(null);
            Ship patrolboat = opponent.getShips().stream().filter(ship -> ship.getType().equals("patrolboat")).findFirst().orElse(null);
            Ship destroyer = opponent.getShips().stream().filter(ship -> ship.getType().equals("destroyer")).findFirst().orElse(null);
            Ship submarine = opponent.getShips().stream().filter(ship -> ship.getType().equals("submarine")).findFirst().orElse(null);

            //Calculo cuantos hits hubo en el turno.
            long carrierDamage = carrier.getShipLocations().stream().filter(s -> salvo.getSalvoLocations().contains(s)).count();
            long battleshipDamage = battleship.getShipLocations().stream().filter(s -> salvo.getSalvoLocations().contains(s)).count();
            long patrolboatDamage = patrolboat.getShipLocations().stream().filter(s -> salvo.getSalvoLocations().contains(s)).count();
            long destroyerDamage = destroyer.getShipLocations().stream().filter(s -> salvo.getSalvoLocations().contains(s)).count();
            long submarineDamage = submarine.getShipLocations().stream().filter(s -> salvo.getSalvoLocations().contains(s)).count();

            List<String> totalDamage = gamePlayer.getSalvoes()
                    .stream()
                    .filter(salvo1 -> salvo1.getTurn() <= salvo.getTurn())
                    .flatMap(s -> Util.hits(s).stream())
                    .collect(Collectors.toList());

            //calcula el historial de todos los hits en los barcos.
            long carrierTotalDmg = carrier.getShipLocations().stream().filter(totalDamage::contains).count();
            long battleshipTotalDmg = battleship.getShipLocations().stream().filter(totalDamage::contains).count();
            long patroboatTotalDmg = patrolboat.getShipLocations().stream().filter(totalDamage::contains).count();
            long destroyerTotalDmg = destroyer.getShipLocations().stream().filter(totalDamage::contains).count();
            long submarineTotalDmg = submarine.getShipLocations().stream().filter(totalDamage::contains).count();
            //damage in turn
            this.carrierHits = carrierDamage;
            this.battleshipHits = battleshipDamage;
            this.patrolboatHits = patrolboatDamage;
            this.destroyerHits = destroyerDamage;
            this.submarineHits = submarineDamage;
            //total damage
            this.carrier = carrierTotalDmg;
            this.battleship = battleshipTotalDmg;
            this.patrolboat = patroboatTotalDmg;
            this.destroyer = destroyerTotalDmg;
            this.submarine = submarineTotalDmg;
        }
    }*/

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
