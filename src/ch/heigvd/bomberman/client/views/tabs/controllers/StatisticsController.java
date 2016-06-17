package ch.heigvd.bomberman.client.views.tabs.controllers;

import ch.heigvd.bomberman.client.ResponseManager;
import ch.heigvd.bomberman.common.game.Statistic;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.Duration;

/**
 * Created by matthieu.villard on 09.06.2016.
 */
public class StatisticsController {

	private ResponseManager rm;
	private ObservableList<Statistic> statistics = FXCollections.observableArrayList();

	@FXML
	private TextField total;

	@FXML
	private TextField tSurvivalTime, aSurvivalTime;

	@FXML
	private TextField tKills, aKills;

	@FXML
	private TextField tDeaths, aDeaths;

	@FXML
	private TextField tBombs, aBombs;

	@FXML
	private TableView<Statistic> table;

	@FXML
	private TableColumn<Statistic, Long> deaths, game;

	@FXML
	private TableColumn<Statistic, String> survivalTime;


	@FXML
	public void initalize() {
		table.setItems(statistics);
		game.setCellValueFactory(param -> {
			return new SimpleObjectProperty<Long>(param.getValue().getGame().getId());
		});
		deaths.setCellValueFactory(param -> {
			return new SimpleObjectProperty<Long>(param.getValue().getRank() != 1 ? 1l : 0);
		});
		survivalTime.setCellValueFactory(param -> {
			return new SimpleObjectProperty<String>(durationToString(param.getValue().getSurvivalTime()));
		});

		rm = ResponseManager.getInstance();
		rm.playerRequest(player -> {
			total.setText(String.valueOf(player.getStatistics().size()));

			tSurvivalTime.setText(durationToString(Duration.ofSeconds(
					player.getStatistics()
					      .stream()
					      .mapToLong(s -> s.getSurvivalTime().getSeconds()).sum()
			)));
			player.getStatistics()
			      .stream()
			      .mapToLong(s -> s.getSurvivalTime().getSeconds())
			      .average()
			      .ifPresent(avg -> aSurvivalTime.setText(durationToString(Duration.ofSeconds((long) avg))));

			tKills.setText(String.valueOf(player.getStatistics().stream().mapToLong(s -> s.getKills()).sum()));
			player.getStatistics().stream().mapToLong(s -> s.getKills()).average().ifPresent(avg -> aKills.setText(String.valueOf(avg)));

			tDeaths.setText(String.valueOf(player.getStatistics().stream().mapToLong(s -> s.getRank() == 1 ? 1 : 0).sum()));
			player.getStatistics().stream().mapToLong(s -> s.getRank() == 1 ? 1 : 0).average().ifPresent(avg -> aDeaths.setText(String.valueOf(avg)));

			tBombs.setText(String.valueOf(player.getStatistics().stream().mapToLong(s -> s.getBombs()).sum()));
			player.getStatistics().stream().mapToLong(s -> s.getBombs()).average().ifPresent(avg -> aBombs.setText(String.valueOf(avg)));

			statistics.setAll(player.getStatistics());
		});
	}

	private String durationToString(Duration duration) {
		long time = duration.getSeconds();
		return (time / 60 < 10 ? "0" : "") + time / 60 + ":" + (time % 60 < 10 ? "0" : "") + time % 60;
	}
}