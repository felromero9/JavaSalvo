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
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_GUEST), HttpStatus.FORBIDDEN);
    }
    Player player = playerRepository.findByUserName(authentication.getName());
    if (player == null){
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_NO_LOGGED), HttpStatus.UNAUTHORIZED);//409
    }

    Game game = gameRepository.save(new Game(LocalDateTime.now()));
    GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(player, game, LocalDateTime.now()));
    return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
  }


  @PostMapping(path = "/players")
  public ResponseEntity<Map<String, Object>> createUser(@RequestParam String userName,
                                                        @RequestParam String password,
                                                        @RequestParam PlayerSide playerSide) {
    if (userName.isEmpty() || password.isEmpty()) {
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_NO_NAME_NO_PASSWORD), HttpStatus.FORBIDDEN);
    }
    Player player = playerRepository.findByUserName(userName);
    if (player != null) {
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.CONFLICT_NAME), HttpStatus.CONFLICT);
    }
    Player newPlayer = playerRepository.save(new Player(userName, passwordEncoder.encode(password),playerSide));
    return new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
  }

  @PostMapping("/game/{gameId}/players")
  public ResponseEntity<Map<String, Object>> joinAGame(@PathVariable long gameId,
                                                       Authentication authentication){
      if (isGuest(authentication)){
        return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_NO_CONECTED), HttpStatus.UNAUTHORIZED);
      }
      Optional<Game> game = gameRepository.findById(gameId);
      if(!game.isPresent()){
        return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_NO_SUCH_GAME), HttpStatus.FORBIDDEN);
      }
      if(game.get().getGamePlayers().size()>1){
        return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_GAME_FULL), HttpStatus.FORBIDDEN);
      }
      Player player = playerRepository.findByUserName(authentication.getName());
      Optional<GamePlayer> firstGamePlayer = game.get()
                                                 .getGamePlayers()
                                                 .stream()
                                                 .filter(gamePlayer -> gamePlayer.getPlayer().getId() == player.getId()).findFirst();

      if (firstGamePlayer.isPresent()){
        return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_OPPONENT), HttpStatus.FORBIDDEN);
      }
      GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(player,game.get(),LocalDateTime.now()) );

    return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
  }

@PostMapping("/games/players/{gamePlayerId}/ships")
public ResponseEntity<Map<String, Object>> joinShips (@RequestBody Set<Ship> ships,
                                                        @PathVariable Long gamePlayerId,
                                                       Authentication authentication){

 if (isGuest(authentication)){
    return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_NO_CONECTED), HttpStatus.UNAUTHORIZED);
  }

  Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
  if(!gamePlayer.isPresent()){
    return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_NO_SUCH_GAME), HttpStatus.UNAUTHORIZED);
  }
  if(!gamePlayer.get().getPlayer().getUserName().equals(authentication.getName())){
    return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_GAME), HttpStatus.UNAUTHORIZED);
  }
  if(gamePlayer.get().getShips().size() > 0 || ships.size()!= 5){
    return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_SHIPS), HttpStatus.UNAUTHORIZED); // me dede retornar siempre algo
  }

  gamePlayer.get().addShips(ships);// adjunto los ships como se crean en el repository
  gamePlayerRepository.save(gamePlayer.get());

  return  new ResponseEntity<>(makeMap("newSet","created"), HttpStatus.CREATED);
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////








  @PostMapping("games/players/{gamePlayerId}/salvoes")
  public ResponseEntity<Map<String, Object>> addSalvoes(@PathVariable long gamePlayerId, Authentication authentication,
                                                        @RequestBody Salvo salvo) {


    Optional<GamePlayer> currentGamePlayer = gamePlayerRepository.findById(gamePlayerId);

    if (isGuest(authentication)) {
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_NO_LOGGED), HttpStatus.UNAUTHORIZED);
    }

    if(!currentGamePlayer.isPresent() ){
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_GAME), HttpStatus.FORBIDDEN);
    }

    Optional<GamePlayer> opponentGamePlayer = currentGamePlayer.get()
                                                              .getGame()
                                                              .getGamePlayers()
                                                              .stream()
                                                              .filter(gamePlayer -> gamePlayer.getId() != currentGamePlayer
                                                                      .get().getId().intValue()).findFirst();
    if (!opponentGamePlayer.isPresent()) {
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_OPPONENT), HttpStatus.FORBIDDEN);
    }
    int currentTurn = currentGamePlayer.get().getSalvoes().stream().sorted(Comparator.comparing(Salvo::getTurn).reversed())
                                                                  .findFirst()
                                                                  .orElse(new Salvo(0,null)).getTurn() + 1;

    int opponentTurn = opponentGamePlayer.get().getSalvoes().stream().sorted(Comparator.comparing(Salvo::getTurn).reversed())
                                                                  .findFirst()
                                                                  .orElse(new Salvo(0,null)).getTurn();


    /*if(currentTurn - opponentTurn > 1 || currentTurn - opponentTurn < -1) {
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_ERROR_OPPONENT_TURN), HttpStatus.FORBIDDEN);
    }*/

    if(currentGamePlayer.get().getId() < opponentGamePlayer.get().getId() && currentTurn - opponentTurn != 1){
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_ERROR_OPPONENT_TURN), HttpStatus.FORBIDDEN);
    }

    if(currentGamePlayer.get().getId() > opponentGamePlayer.get().getId() && currentTurn - opponentTurn != 0){
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_ERROR_OPPONENT_TURN), HttpStatus.FORBIDDEN);
    }

    Player player = playerRepository.findByUserName(authentication.getName());
    if (currentGamePlayer.get().getPlayer().getId() != player.getId()) {
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_SAVE_SALVOES), HttpStatus.UNAUTHORIZED);
    }
    if (salvo.getCells().size()==0) {
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_SHOTS), HttpStatus.FORBIDDEN);
    }
    if (salvo.getCells().size()==0) {
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_SEND_AT_LEAST_ONE_SALVO), HttpStatus.FORBIDDEN);
    }


    salvo.setTurn(currentTurn);
    currentGamePlayer.get().addSalvo(salvo);
    gamePlayerRepository.save(currentGamePlayer.get());

    return  new ResponseEntity<>(makeMap("salvo shot","created"), HttpStatus.CREATED);

}
     /*
    Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
    if (!gamePlayer.isPresent()) {
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_GAME), HttpStatus.UNAUTHORIZED);
    }
    if (!gamePlayer.get().getPlayer().getUserName().equals(authentication.getName())) {
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_GAME), HttpStatus.UNAUTHORIZED);
    }
    if (gamePlayer.get().getSalvoes().stream().mapToInt(item -> item.getTurn()).max().getAsInt() +1 !=  salvo.getTurn()){
      return  new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_SALVO_TURN), HttpStatus.FORBIDDEN);
    }

    Optional<GamePlayer> gamePlayerOpponent = gamePlayer.get()
                                                        .getGame()
                                                        .getGamePlayers()
                                                        .stream()
                                                        .filter(gp -> gp.getId() == gamePlayerId).findAny();

    if(!gamePlayerOpponent.isPresent() || salvo.getTurn() -1 > gamePlayerOpponent.get().getSalvoes().size()) {
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_SALVO_TURN), HttpStatus.FORBIDDEN);
    }

    if( gamePlayerOpponent.get().getShips().isEmpty()){
      return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_SHOTS), HttpStatus.FORBIDDEN);
    }

    return  new ResponseEntity<>(makeMap("salvo shot","created"), HttpStatus.CREATED);

  }*/


























//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  @GetMapping("/game_view/{gamePlayerId}")
    public ResponseEntity < Map<String, Object> > getGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication){
        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if(!gamePlayer.isPresent()){
            return new ResponseEntity<>(makeMap(Messages.ERROR_KEY, Messages.UNAUTHORIZED_GAMEPLAYER), HttpStatus.UNAUTHORIZED);
        }
        if (!gamePlayer.get().getPlayer().getUserName().equals(authentication.getName())){
            return new ResponseEntity <> (makeMap(Messages.ERROR_KEY, Messages.FORBIDDEN_AUTHORITY), HttpStatus.UNAUTHORIZED);
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
