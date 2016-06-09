package ch.heigvd.bomberman.common.game;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by matthieu.villard on 09.06.2016.
 */
@Entity
@Table(name = "game")
public class Game implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(targetEntity = Statistic.class, fetch = FetchType.EAGER, mappedBy="game")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE})
    private List<Statistic> statistics = new LinkedList<>();

    public Game(){

    }

    public Long getId(){
        return id;
    }

    public void addStatistic(Statistic statistic){
        if(!statistics.contains(statistic))
            statistics.add(statistic);
    }

    public void removeStatistic(Statistic statistic){
        statistics.remove(statistic);
    }

    public List<Statistic> getStatistics(){
        return statistics;
    }
}