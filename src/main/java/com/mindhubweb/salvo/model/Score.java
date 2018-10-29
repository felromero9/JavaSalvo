package com.mindhubweb.salvo.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Entity
public class Score {

    @Id // this is all id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private LocalDateTime finishDate;
    private int points;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    public Score(){ }

    public Score(Player player, Game game, int points, LocalDateTime finishDate){// estoo es el constructor
        this.player = player;
        this.game = game;
        this.finishDate = finishDate;
        this.points = points;
    }

    public Map<String, Object> scoreDto(){
        Map<String, Object> dto = new HashMap<>();
        dto.put("id",this.getId());
        dto.put("player",this.getPlayer().playerDto());
        dto.put("points", this.getPoints());
        dto.put("finishGame", this.getFinishDate());

        return dto;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


}