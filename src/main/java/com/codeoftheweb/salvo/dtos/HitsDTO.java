package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Salvo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HitsDTO {

    private Set<HitRecordDTO> self;

    private Set<HitRecordDTO> opponent;

    public HitsDTO() {

    }

    public HitsDTO(Set<HitRecordDTO> self, Set<HitRecordDTO> opponent) {
        this.self = self;
        this.opponent = opponent;
    }

    public Set<HitRecordDTO> getSelf() {
        return self;
    }

    public void setSelf(Set<HitRecordDTO> self) {
        this.self = self;
    }

    public Set<HitRecordDTO> getOpponent() {
        return opponent;
    }

    public void setOpponent(Set<HitRecordDTO>  opponent) {
        this.opponent = opponent;
    }
}
