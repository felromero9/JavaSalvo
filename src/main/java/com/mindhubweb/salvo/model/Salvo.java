package com.mindhubweb.salvo.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo {

    @Id // this is all id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    // here star the relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="gamePlayer_id")
    private GamePlayer gamePlayer;
    // end relationships


    private String turn;

    @ElementCollection
    @Column(name="cell")
    private List<String> cells = new ArrayList<>();

    public Salvo(){}

    public Salvo(String turn, List<String> cells ){// estoo es el constructor
        this.turn =turn;
        this.cells=cells;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getCells() {
        return cells;
    }

    public void setCells(List<String> cells) {
        this.cells = cells;
    }

    public String getTurn() {
        return turn;
    }

    public void salvoDTO(String turn) {
        this.turn = turn;
    }

    public Map<String, Object> salvoDto(){
        Map<String, Object> dto = new HashMap<>();
        dto.put("turn",this.turn);
        dto.put("player", this.gamePlayer.gamePlayerDto());
        dto.put("cells",this.cells);

        return dto;
    }
}
