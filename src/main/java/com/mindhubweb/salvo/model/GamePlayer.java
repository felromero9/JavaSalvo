package com.mindhubweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Entity
public class GamePlayer {

    @Id // this is all id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Salvo> salvoes = new HashSet<>();


    //private LocalDateTime date;

    public GamePlayer(){ }

    public GamePlayer(Player player, Game game, LocalDateTime localDateTime,Set<Ship> ships,Set<Salvo> salvoes){// estoo es el constructor
       this.player = player;
        this.game = game;
        this.joinDate = localDateTime;
        this.addShips(ships);
        this.addSalvoes(salvoes);

    }
    public GamePlayer(Player player, Game game, LocalDateTime localDateTime){// estoo es el constructor
        this.player = player;
        this.game = game;
        this.joinDate = localDateTime;


    }


    public Score getScore (){
        return this.player.getGameScore(this.game);

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    public void addShips(Set<Ship> ships){
        ships.stream().forEach(this::addShip);
    }

    public void addSalvo(Salvo salvo){
        salvo.setGamePlayer(this);
        salvoes.add(salvo);
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    public void addSalvoes(Set<Salvo> salvoes){
        salvoes.stream().forEach(this::addSalvo);
    }

    public Map<String, Object> gamePlayerDto(){
        Map<String, Object> dto = new HashMap<>();
        dto.put("id",this.getId());
        dto.put("player",this.getPlayer().playerDto());
        if (this.getScore()!= null)
            dto.put("score", this.getScore().getPoints());
        else
            dto.put("score", this.getScore());
          return dto;
    }

    public Map<String, Object> gameViewDto(){
        Map<String,Object> dto = new HashMap<>();
        dto.put("id",this.game.getId());
        dto.put("date",this.game.getDate());
        dto.put("gamePlayer",this.game.getGamePlayers()
            .stream().map(GamePlayer::gamePlayerDto)
        );
        dto.put("ships",this.getShips().stream().map(Ship::shipsDto));
        dto.put("salvoes", this.getGame().getGamePlayers()
                .stream()
                .flatMap(gamePlayer -> gamePlayer.salvoes
                        .stream()
                        .map(Salvo::salvoDto)));

        return dto;
    }


}
