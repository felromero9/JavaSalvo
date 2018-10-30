package com.mindhubweb.salvo.controller;

import com.mindhubweb.salvo.model.*;
import com.mindhubweb.salvo.repository.GamePlayerRepository;
import com.mindhubweb.salvo.repository.GameRepository;
import com.mindhubweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;



@RestController
@RequestMapping("/api")
public class SalvoRestController {
  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private GamePlayerRepository gamePlayerRepository;
  private Object gameService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @GetMapping("/games")
  public Map<String, Object> getAllGames(Authentication authentication) {
    Map<String, Object> dto = new LinkedHashMap<>();
    if (isGuest(authentication))
      dto.put("user", "Guest");
    else
      dto.put("user", playerRepository.findByUserName(authentication.getName()).currentPlayerDTO());

    dto.put("games", gameRepository
            .findAll() //esto me trae todo
            .stream() // esto crea un "array imaginario"
            .sorted(Comparator.comparing(Game::getId)) //ordeno el stream
            .map(Game::gameDto)
            .collect(toList()));
    return dto;
  }

  @PostMapping(path = "/games")
  public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
    if (isGuest(authentication)) {
      return new ResponseEntity<>(makeMap("error", "you are guest"), HttpStatus.FORBIDDEN);
    }
    Player player = playerRepository.findByUserName(authentication.getName());
    if (player == null){
      return new ResponseEntity<>(makeMap("error", "no logged"), HttpStatus.UNAUTHORIZED);//409
    }

    Game game = gameRepository.save(new Game(LocalDateTime.now()));
    GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(player, game, LocalDateTime.now()));
    return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
  }


  @PostMapping(path = "/players")
  public ResponseEntity<Map<String, Object>> createUser(@RequestParam String userName,@RequestParam String password) {
    if (userName.isEmpty() || password.isEmpty()) {
      return new ResponseEntity<>(makeMap("error", "No name or password"), HttpStatus.FORBIDDEN);
    }
    Player player = playerRepository.findByUserName(userName);
    if (player != null) {
      return new ResponseEntity<>(makeMap("error", "userName in use, please try again!"), HttpStatus.CONFLICT);
    }
    Player newPlayer = playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
    return new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
  }



/*

  @PostMapping(path = "/api/game/{gameId}/players")
  public ResponseEntity<Map<String, Object>> createGamePlayer(@RequestParam String userName, @RequestParam long id){
    Player player = playerRepository.findByUserName(userName);
    if (player != null) {
      return new ResponseEntity<>(makeMap("error", "No user name."), HttpStatus.CONFLICT);//409
    }
    Game newGame = gameRepository.save(new Game(LocalDateTime.now()));
    return new ResponseEntity<>(makeMap("id", newGame.getId()), HttpStatus.CREATED);
  }
*/


  @GetMapping("/game_view/{gamePlayerId}")
    public ResponseEntity < Map<String, Object> > getGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication){
        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if(!gamePlayer.isPresent()){
            return new ResponseEntity<>(makeMap("error", "empty gameplayer"), HttpStatus.UNAUTHORIZED);
        }
        if (!gamePlayer.get().getPlayer().getUserName().equals(authentication.getName())){
            return new ResponseEntity <> (makeMap("error", "no have authority"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity <> (gamePlayer.get().gameViewDto(), HttpStatus.CREATED);
    }


    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

}



















/* @PostMapping(path = "/games")
    public ResponseEntity<Map<String, Object>> createGame(@RequestParam String userName, @RequestParam long id){
        Player player = playerRepository.findByUserName(userName);
        if (player != null) {
            return new ResponseEntity<>(makeMap("error", "No user name."), HttpStatus.CONFLICT);//409
        }
        Game newGame = gameRepository.save(new Game(LocalDateTime.now()));
        return new ResponseEntity<>(makeMap("id", newGame.getId()), HttpStatus.CREATED);
    }*/
