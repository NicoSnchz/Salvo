package com.codeoftheweb.salvo.services;

import com.codeoftheweb.salvo.dtos.*;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Salvo;
import com.codeoftheweb.salvo.models.Ship;
import com.codeoftheweb.salvo.utilities.GameState;
import com.codeoftheweb.salvo.utilities.Util;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    public String getGameState(GamePlayer gamePlayer) {
        Optional<GamePlayer> opponent = gamePlayer.getOpponent(gamePlayer);

        if (gamePlayer.getShips().size() == 0){
            return GameState.PLACESHIPS.name();
        }

        if (gamePlayer.getShips().size() == 5 && opponent.isEmpty()){
            return GameState.WAITINGFOROPP.name();
        }
        if (opponent.get().getShips().size() == 0){
            return GameState.WAIT.name();
        }

        if (allOppShipsSunk(gamePlayer) && !allOppShipsSunk(opponent.get()) && gamePlayer.getSalvoes().size() == opponent.get().getSalvoes().size()){
            return GameState.WON.name();
        }

        if (allOppShipsSunk(gamePlayer) && allOppShipsSunk(opponent.get()) && gamePlayer.getSalvoes().size() == opponent.get().getSalvoes().size()){
            return GameState.TIE.name();
        }

        if (!allOppShipsSunk(gamePlayer) && allOppShipsSunk(opponent.get())  && gamePlayer.getSalvoes().size() == opponent.get().getSalvoes().size()){
            return GameState.LOST.name();
        }

        if (gamePlayer.getSalvoes().size() == opponent.get().getSalvoes().size()){
            return GameState.PLAY.name();
        }

        if (gamePlayer.getSalvoes().size() < opponent.get().getSalvoes().size()){
            return GameState.PLAY.name();
        }

        if (gamePlayer.getSalvoes().size() > opponent.get().getSalvoes().size()){
            return GameState.WAIT.name();
        }


        return GameState.UNDEFINED.name();
    }

    public boolean allOppShipsSunk(GamePlayer gamePlayer){
        Optional<GamePlayer> opponent = gamePlayer.getOpponent(gamePlayer);

        List<String> gamePlayerSalvoes = gamePlayer.getSalvoes().stream().flatMap(salvo -> salvo.getSalvoLocations().stream()).collect(Collectors.toList());
        List<String> opponentShips = opponent.get().getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());

        return gamePlayerSalvoes.containsAll(opponentShips);
    }

    public DamagesDTO makeDamageDTO(Salvo salvo){

        DamagesDTO damagesDTO = new DamagesDTO();

        GamePlayer gamePlayer = salvo.getGamePlayer();
        GamePlayer opponent = gamePlayer.getOpponent(gamePlayer).orElse(null);

        if (opponent != null) {

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

            damagesDTO.setCarrierHits(carrierDamage);
            damagesDTO.setBattleshipHits(battleshipDamage);
            damagesDTO.setPatrolboatHits(patrolboatDamage);
            damagesDTO.setDestroyerHits(destroyerDamage);
            damagesDTO.setSubmarineHits(submarineDamage);
            damagesDTO.setCarrier(carrierTotalDmg);
            damagesDTO.setBattleship(battleshipTotalDmg);
            damagesDTO.setPatrolboat(patroboatTotalDmg);
            damagesDTO.setDestroyer(destroyerTotalDmg);
            damagesDTO.setSubmarine(submarineTotalDmg);
        }
        return damagesDTO;
    }

    public HitRecordDTO makeHitsDTO (Salvo salvo){
        GamePlayer gamePlayer = salvo.getGamePlayer();
        GamePlayer opponent = gamePlayer.getOpponent(gamePlayer).orElse(null);

        List<String> opponentShips = opponent.getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());
        List<String> salvoLocations = salvo.getSalvoLocations();
        List<String> hitLocations = salvoLocations.stream().filter(opponentShips::contains).collect(Collectors.toList());

        Long missed = salvoLocations.stream().filter(s -> !opponentShips.contains(s)).count();

        HitRecordDTO hitRecordDTO = new HitRecordDTO();

        hitRecordDTO.setTurn(salvo.getTurn());
        hitRecordDTO.setHitLocations(hitLocations);
        hitRecordDTO.setDamages(makeDamageDTO(salvo));
        hitRecordDTO.setMissed(missed);

        return hitRecordDTO;
    }

    @Override
    public GameViewDTO makeGameViewDTO(GamePlayer gamePlayer) {
        HitsDTO hitsDTO = new HitsDTO();

        if (gamePlayer.getOpponent(gamePlayer).isPresent()){
            hitsDTO.setSelf(gamePlayer.getOpponent(gamePlayer).get().getSalvoes().stream().map(this::makeHitsDTO).collect(Collectors.toSet()));
            hitsDTO.setOpponent(gamePlayer.getSalvoes().stream().map(this::makeHitsDTO).collect(Collectors.toSet()));
        }else{
            hitsDTO.setSelf(new HashSet<>());
            hitsDTO.setOpponent(new HashSet<>());
        }


        GameViewDTO gameViewDTO = new GameViewDTO();

        gameViewDTO.setId(gamePlayer.getGame().getGameId());
        gameViewDTO.setGameState(getGameState(gamePlayer));
        gameViewDTO.setCreated(gamePlayer.getGame().getGameStartDate());
        gameViewDTO.setGamePlayers(gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(GamePlayerDTO::new)
                .collect(Collectors.toSet()));
        gameViewDTO.setShips(gamePlayer.getShips()
                .stream()
                .map(ShipDTO::new)
                .collect(Collectors.toSet()));

        if (gamePlayer.getOpponent(gamePlayer).isEmpty()){
            gameViewDTO.setSalvoes(gamePlayer.getGame().getGamePlayers()
                    .stream()
                    .flatMap(gamePlayer1 -> gamePlayer1.getSalvoes()
                            .stream()
                            .map(SalvoesDTO::new))
                    .collect(Collectors.toSet()));
        }else {
            gameViewDTO.setSalvoes(new HashSet<>());
        }
        gameViewDTO.setHits(hitsDTO);

        return gameViewDTO;
    }
}
