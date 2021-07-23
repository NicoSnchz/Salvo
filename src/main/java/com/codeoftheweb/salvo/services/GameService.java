package com.codeoftheweb.salvo.services;

import com.codeoftheweb.salvo.dtos.GameViewDTO;
import com.codeoftheweb.salvo.models.GamePlayer;

public interface GameService {

     String getGameState (GamePlayer gamePlayer);
     GameViewDTO makeGameViewDTO(GamePlayer gamePlayer);
}
