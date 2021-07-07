package com.codeoftheweb.salvo.models;


import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long scoreId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    private Float score;

    private LocalDateTime finishDate;

    //CONSTRUCTOR
    public Score() {
    }

    public Score(Player player, Game game, Float score, LocalDateTime finishDate) {
        this.player = player;
        this.game = game;
        this.score = score;
        this.finishDate = finishDate;
    }


    //GETTER
    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public Float getScore() {
        return score;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    //SETTER
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }
}
