package ch.heigvd.bomberman.common.game;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Created by matthieu.villard on 31.05.2016.
 */
@Entity
@Table(name = "statistic")
public class Statistic implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name="suvivalTime")
    private Duration survivalTime;

    @Column(name="kills")
    private int kills = 0;

    @Column(name="bombs")
    private int bombs = 0;

    public Statistic(){

    }

    public Statistic(Player player, Game game){
        this.player = player;
        this.game = game;
        game.addStatistic(this);
    }

    public Game getGame(){
        return game;
    }

    public void setSurvivalTime(Duration survivalTime){
        this.survivalTime = survivalTime;
    }

    public Duration getSurvivalTime(){
        return survivalTime;
    }

    public void kill(){
        kills++;
    }

    public int getKills(){
        return kills;
    }

    public void dropBomb(){
        bombs++;
    }

    public int getBombs(){
        return bombs;
    }

    public int getRank(){
        return game.getStatistics().stream().sorted(Comparator.nullsFirst((a, b) -> (a.survivalTime != null && b
                .survivalTime != null) ? b.survivalTime.compareTo(a.survivalTime) : 1)).collect(Collectors.toList()).indexOf(this) + 1;
    }
}
