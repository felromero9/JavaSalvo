package com.mindhubweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Player {

    @Id // this is all id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id; // estas son las variable definidas



    // here star the relationships (de player a gameplayer)
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers;
    // end relationships

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<Score> scores;

    public void addScore(Score score) {
        score.setPlayer(this);
        scores.add(score);
    }

    private String userName;
    private String password;
    private PlayerSide playerSide;


    public Player() {
    }

    public Player(String userName, String password, PlayerSide playerSide) {// estos son los constructores, no olvidar los parametros
        this.userName = userName;
        this.password = password;
        this.playerSide = playerSide;

    }

    public PlayerSide getPlayerSide(){
        return playerSide;
    }

    public void setPlayerSide(PlayerSide playerSide){
        this.playerSide = playerSide;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }


    public Score getGameScore(Game game) {
        return scores
                .stream()
                .filter(score -> score.getGame().getId() == game.getId())
                .findAny().orElse(null);
    }

    public Map<String, Object> currentPlayerDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        //dto.put("id", playerRepository.findById(this.getId()));
        dto.put("userName", this.getUserName());
        dto.put("side", this.playerSide);

        return dto;
    }

    public Map<String, Object> playerDto() {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", this.getId());
        dto.put("username", this.getUserName());
        dto.put("playerSide", this.getPlayerSide());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
