package ch.heigvd.bomberman.common.model;

import javafx.scene.image.Image;

public enum Skin {
    SKIN1("lib/img/skins/skin1.png"),
    SKIN2("lib/img/skins/skin2.png"),
    SKIN3("lib/img/skins/skin3.png"),
    SKIN4("lib/img/skins/skin4.png");

    private Image image;
    private Skin(String s) {
        // TODO: charger l'image
    }
}
