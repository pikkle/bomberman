package ch.heigvd.bomberman.client.views.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Created by julien on 27.05.16.
 */
public class UserTabsController
{
    @FXML
    private Tab roomsTabs;

    @FXML
    private TabPane tabs;

    @FXML
    private void initalize(){
        tabs.getSelectionModel().clearAndSelect(0);

    }
}
