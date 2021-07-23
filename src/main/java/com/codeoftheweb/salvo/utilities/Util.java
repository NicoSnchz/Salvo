package com.codeoftheweb.salvo.utilities;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Salvo;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.*;
import java.util.stream.Collectors;

public class Util {


    public static Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public static List<String> hits(Salvo salvo) {
        GamePlayer gamePlayer = salvo.getGamePlayer();
        GamePlayer opponent = gamePlayer.getOpponent(gamePlayer).orElse(null);

        if (opponent != null) {
            List<String> opponentShips = opponent.getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());
            List<String> salvoLocations = salvo.getSalvoLocations();

            return salvoLocations.stream().filter(opponentShips::contains).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static Long missed(Salvo salvo) {
        GamePlayer gamePlayer = salvo.getGamePlayer();
        GamePlayer opponent = gamePlayer.getOpponent(gamePlayer).orElse(null);

        if (opponent != null) {
            List<String> opponentShips = opponent.getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());
            List<String> salvoLocations = salvo.getSalvoLocations();

            return salvoLocations.stream().filter(s -> !opponentShips.contains(s)).count();
        }
        return 0L;
    }

    public static String getGameState(GamePlayer gamePlayer) {
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

    public static boolean allOppShipsSunk(GamePlayer gamePlayer){
        Optional<GamePlayer> opponent = gamePlayer.getOpponent(gamePlayer);

        List<String> gamePlayerSalvoes = gamePlayer.getSalvoes().stream().flatMap(salvo -> salvo.getSalvoLocations().stream()).collect(Collectors.toList());
        List<String> opponentShips = opponent.get().getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());

        return gamePlayerSalvoes.containsAll(opponentShips);
    }
}
