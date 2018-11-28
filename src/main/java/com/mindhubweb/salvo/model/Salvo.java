package com.mindhubweb.salvo.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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


    private int turn;

    @ElementCollection
    @Column(name="cell")
    private List<String> cells = new ArrayList<>();

    public Salvo(){}

    public Salvo(int turn, List<String> cells ){// estoo es el constructor
        this.turn =turn;
        this.cells=cells;
    }

    public Map<String, Object> salvoDto(){
        Map<String, Object> dto = new HashMap<>();
        dto.put("turn",this.turn);
        dto.put("player", this.gamePlayer.gamePlayerDto());
        dto.put("cells",this.cells);
        dto.put("hits", this.getHits());
        dto.put("sinks", this.getSinks());

        return dto;
    }



  private Optional<GamePlayer> getOpponentGP(){
        return  this.getGamePlayer().getGame()
                .getGamePlayers().stream()
                .filter(gamePlayer -> gamePlayer.getId() != this.gamePlayer.getId().intValue()).findFirst();// here is the opponent
  }


    public List<String> getHits(){

        return this.cells.stream().filter( cell -> getOpponentGP()
                                            .get()
                                            .getShips()
                                            .stream()
                                            .anyMatch(ship -> ship.getCells().contains(cell))).collect(Collectors.toList());
    };


    private List<Map<String, Object>> getSinks() {
        List<String> allShots = new ArrayList<>();
        this.gamePlayer.getSalvoes()
                .stream()
                .filter(salvo -> salvo.getTurn() <= this.getTurn())
                .forEach(salvo -> allShots.addAll(salvo.getCells())); //aca busca todos los shots

        List<Map<String, Object>> allsinks = new ArrayList<>();

        //comparacion con el gpoponent
        if(getOpponentGP().isPresent()) {
            allsinks = getOpponentGP()
                    .get()
                    .getShips()
                    .stream()
                    .filter(ship -> allShots.containsAll(ship.getCells())).map(Ship::shipsDto).collect(Collectors.toList());
        }
        return allsinks;
    };


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

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void salvoDTO(int turn) {
        this.turn = turn;
    }




}
