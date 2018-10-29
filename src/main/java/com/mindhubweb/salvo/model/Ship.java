package com.mindhubweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {

    @Id // this is all id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    // here star the relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="gamePlayer_id")
    private GamePlayer gamePlayer;
    // end relationships

    private ShipType type;

    @ElementCollection
    @Column(name="cell")
    private List<String> cells = new ArrayList<>();

    public Ship(){}

    public Ship(ShipType type, List<String> cells ){// estoo es el constructor
        this.type =type;
        this.cells=cells;
    }

    public List<String> getCells() {
        return cells;
    }

    public void addCells (List<String> cells){this.cells.addAll(cells);}


    public void setCells(List<String> cells) {
        this.cells = cells;
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

    public ShipType getType() {
        return type;
    }

    public void setType(ShipType type) {
        this.type = type;
    }

    public Map<String, Object> shipsDto(){
        Map<String, Object> dto = new HashMap<>();
        dto.put("ships",this.type);
        dto.put("cells",this.cells);
        return dto;
    }


}
