package com.mindhubweb.salvo;

import com.mindhubweb.salvo.model.*;
import com.mindhubweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



@SpringBootApplication
public class SalvoApplication {
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository,
									  GameRepository gameRepository,
									  GamePlayerRepository gamePlayerRepository,
									  ShipRepository shipRepository,
									  ScoreRepository scoreRepository)
	{
		return (args) -> {
			Player player1 =new Player("felipe",passwordEncoder.encode("felipe123"),PlayerSide.PLANTS);
			Player player2 =new Player( "miriam", passwordEncoder.encode("miriam123"), PlayerSide.ZOMBIES);
			Player player3 =new Player("carlos",passwordEncoder.encode("carlos123"),PlayerSide.PLANTS);
			Player player4 =new Player("rodri",passwordEncoder.encode("rodri123"), PlayerSide.ZOMBIES);
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			Game game1 = new Game(LocalDateTime.parse("2018-09-15T15:23:50.411"));
			Game game2 = new Game(LocalDateTime.parse("2018-10-15T15:23:50.411"));
			Game game3 = new Game(LocalDateTime.parse("2018-11-15T15:23:50.411"));
			Game game4 = new Game(LocalDateTime.parse("2018-08-15T15:23:50.411"));
			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);

			// comienza los sets de SHIPS
			Set<Ship> shipSet1 = new HashSet<>();
			shipSet1.add(new Ship(ShipType.MELONPULTA,new ArrayList<>(Arrays.asList("H2","H3","H4","H5"))));
			shipSet1.add(new Ship(ShipType.HIELAGUISANTES,new ArrayList<>(Arrays.asList("E1","E2","E3"))));
			shipSet1.add(new Ship(ShipType.LANZAMAIZ,new ArrayList<>(Arrays.asList("B4","B5","B6"))));
			shipSet1.add(new Ship(ShipType.LANZAGUISANTES,new ArrayList<>(Arrays.asList("H1","J2"))));
			shipSet1.add(new Ship(ShipType.CARRONIVERA,new ArrayList<>(Arrays.asList("C2","C3","C4","C5","C6"))));

			Set<Ship> shipSet2 = new HashSet<>();
			shipSet2.add(new Ship(ShipType.ZOMBIEBAILARIN,new ArrayList<>(Arrays.asList("B5","C5","D5"))));
			shipSet2.add(new Ship(ShipType.LANZAGUISANTES,new ArrayList<>(Arrays.asList("F1","F2"))));
			Set<Ship> shipSet3 = new HashSet<>();
			shipSet3.add(new Ship(ShipType.ZOMBIEBAILARIN,new ArrayList<>(Arrays.asList("B5","C5","D5"))));
			shipSet3.add(new Ship(ShipType.LANZAGUISANTES,new ArrayList<>(Arrays.asList("C6","C7"))));
			Set<Ship> shipSet4 = new HashSet<>();
			shipSet4.add(new Ship(ShipType.HIELAGUISANTES,new ArrayList<>(Arrays.asList("A2","A3","A4"))));
			shipSet4.add(new Ship(ShipType.LANZAGUISANTES,new ArrayList<>(Arrays.asList("G6","H6"))));
			Set<Ship> shipSet5 = new HashSet<>();
			shipSet5.add(new Ship(ShipType.MELONPULTA,new ArrayList<>(Arrays.asList("B5","C5","D5","E5"))));
			shipSet5.add(new Ship(ShipType.LANZAGUISANTES,new ArrayList<>(Arrays.asList("C6","C7"))));
			Set<Ship> shipSet6 = new HashSet<>();
			shipSet6.add(new Ship(ShipType.HIELAGUISANTES,new ArrayList<>(Arrays.asList("A2","A3","A4"))));
			shipSet6.add(new Ship(ShipType.ZOMBIEBAILARIN,new ArrayList<>(Arrays.asList("G6","H6"))));
			Set<Ship> shipSet7 = new HashSet<>();
			shipSet7.add(new Ship(ShipType.MELONPULTA,new ArrayList<>(Arrays.asList("B5","C5","D5"))));
			shipSet7.add(new Ship(ShipType.LANZAGUISANTES,new ArrayList<>(Arrays.asList("C6","C7"))));
			Set<Ship> shipSet8 = new HashSet<>();
			shipSet8.add(new Ship(ShipType.ZOMBISTEIN,new ArrayList<>(Arrays.asList("A2","A3","A4","A5","A6"))));
			shipSet8.add(new Ship(ShipType.LANZAGUISANTES,new ArrayList<>(Arrays.asList("G6","H6"))));

			//aca comienza los sets de SALVO
			Set<Salvo> salvoSet1 = new HashSet<>();
			salvoSet1.add(new Salvo(1, new ArrayList<>(Arrays.asList("B5", "C5", "F1"))));
			salvoSet1.add(new Salvo(2,new ArrayList<>(Arrays.asList("F2","D5"))));
			Set<Salvo> salvoSet2 = new HashSet<>();
			salvoSet2.add(new Salvo(1,new ArrayList<>(Arrays.asList("B4","B5","B6"))));
			salvoSet2.add(new Salvo(2,new ArrayList<>(Arrays.asList("E1","H3","A2"))));
			Set<Salvo> salvoSet3 = new HashSet<>();
			salvoSet3.add(new Salvo(1,new ArrayList<>(Arrays.asList("A2","A4","G6"))));
			salvoSet3.add(new Salvo(2,new ArrayList<>(Arrays.asList("A3","H6"))));
			Set<Salvo> salvoSet4 = new HashSet<>();
			salvoSet4.add(new Salvo(1,new ArrayList<>(Arrays.asList("B5","D5","C7"))));
			salvoSet4.add(new Salvo(2,new ArrayList<>(Arrays.asList("A3","H6"))));
			Set<Salvo> salvoSet5 = new HashSet<>();
			salvoSet5.add(new Salvo(1,new ArrayList<>(Arrays.asList("G6","H6","A4"))));
			salvoSet5.add(new Salvo(2,new ArrayList<>(Arrays.asList("A2","A3","D8"))));
			Set<Salvo> salvoSet6 = new HashSet<>();
			salvoSet6.add(new Salvo(1,new ArrayList<>(Arrays.asList("H1","H2","H3"))));
			salvoSet6.add(new Salvo(2,new ArrayList<>(Arrays.asList("E1","F2","G3"))));
			Set<Salvo> salvoSet7 = new HashSet<>();
			salvoSet7.add(new Salvo(1,new ArrayList<>(Arrays.asList("A3","A4","F7"))));
			salvoSet7.add(new Salvo(2,new ArrayList<>(Arrays.asList("A2","G5","H6"))));
			Set<Salvo> salvoSet8 = new HashSet<>();
			salvoSet8.add(new Salvo(1,new ArrayList<>(Arrays.asList("B5","C6","H1"))));
			salvoSet8.add(new Salvo(2,new ArrayList<>(Arrays.asList("C5","C7","D5"))));


			gamePlayerRepository.save(new GamePlayer(player1,game1,LocalDateTime.now(),shipSet1,salvoSet1));
			gamePlayerRepository.save(new GamePlayer(player2,game1,LocalDateTime.now(),shipSet2,salvoSet2));
			gamePlayerRepository.save(new GamePlayer(player3,game2,LocalDateTime.now(),shipSet3,salvoSet3));
			gamePlayerRepository.save(new GamePlayer(player4,game2,LocalDateTime.now(),shipSet4,salvoSet4)); //new HashSet<>() crea un salvo vacio
			gamePlayerRepository.save(new GamePlayer(player1,game3,LocalDateTime.now(),shipSet5,salvoSet5));
			gamePlayerRepository.save(new GamePlayer(player3,game3,LocalDateTime.now(),shipSet6,salvoSet6));
			gamePlayerRepository.save(new GamePlayer(player2,game4,LocalDateTime.now(),shipSet7,salvoSet7));
			gamePlayerRepository.save(new GamePlayer(player4,game4,LocalDateTime.now(),shipSet8,salvoSet8));

			scoreRepository.save(new Score(player1,game1,1,LocalDateTime.now()));
			scoreRepository.save(new Score(player2,game1,1,LocalDateTime.now()));
			scoreRepository.save(new Score(player3,game2,3,LocalDateTime.now()));
			scoreRepository.save(new Score(player4,game2,0,LocalDateTime.now()));
			scoreRepository.save(new Score(player1,game3,0,LocalDateTime.now()));
			scoreRepository.save(new Score(player3,game3,3,LocalDateTime.now()));
			scoreRepository.save(new Score(player2,game4,1,LocalDateTime.now()));
			scoreRepository.save(new Score(player4,game4,1,LocalDateTime.now()));
		};
	}

}



