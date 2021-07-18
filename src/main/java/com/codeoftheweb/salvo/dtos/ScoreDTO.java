package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Score;

import java.time.LocalDateTime;

public class ScoreDTO {
    private Long player;

    private Object score;

    private LocalDateTime finishDate;

    public ScoreDTO(GamePlayer gamePlayer) {
        if (gamePlayer.getScore().isPresent()) {
            this.player = gamePlayer.getPlayer().getId();
            this.score = gamePlayer.getScore().get().getScore();
            this.finishDate = gamePlayer.getScore().get().getFinishDate();
        } else {
            this.score = "Partida en curso";
        }
    }

    public Long getPlayer() {
        return player;
    }

    public void setPlayer(Long player) {
        this.player = player;
    }

    public Object getScore() {
        return score;
    }

    public void setScore(Object score) {
        this.score = score;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }
}
