package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Salvo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HitsDTO {

    private List<HitRecordDTO> self;

    private List<HitRecordDTO> opponent;

    public HitsDTO(GamePlayer gamePlayer){
        this.self = gamePlayer.getOpponent(gamePlayer).get().getSalvoes().stream().map(HitRecordDTO::new).collect(Collectors.toList());
        this.opponent = gamePlayer.getSalvoes().stream().map(HitRecordDTO::new).collect(Collectors.toList());
    }

    public List<HitRecordDTO> getSelf() {
        return self;
    }

    public void setSelf(List<HitRecordDTO> self) {
        this.self = self;
    }

    public List<HitRecordDTO> getOpponent() {
        return opponent;
    }

    public void setOpponent(List<HitRecordDTO>  opponent) {
        this.opponent = opponent;
    }
}
