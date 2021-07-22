package com.codeoftheweb.salvo.utilities;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Salvo;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public static List<String>  hits(Salvo salvo){
        GamePlayer gamePlayer = salvo.getGamePlayer();
        GamePlayer opponent = gamePlayer.getOpponent(gamePlayer).orElse(null);

        if (opponent != null){
            List<String> opponentShips = opponent.getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());
            List<String> salvoLocations = salvo.getSalvoLocations();

            return salvoLocations.stream().filter(opponentShips::contains).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static Long missed(Salvo salvo){
        GamePlayer gamePlayer = salvo.getGamePlayer();
        GamePlayer opponent = gamePlayer.getOpponent(gamePlayer).orElse(null);

        if (opponent != null){
            List<String> opponentShips = opponent.getShips().stream().flatMap(ship -> ship.getShipLocations().stream()).collect(Collectors.toList());
            List<String> salvoLocations = salvo.getSalvoLocations();

            return salvoLocations.stream().filter(s -> !opponentShips.contains(s)).count();
        }
        return 0L;
    }
}
