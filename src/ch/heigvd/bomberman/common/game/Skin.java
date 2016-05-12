package ch.heigvd.bomberman.common.game;

import javafx.scene.image.Image;

public enum Skin {
	SKIN1("img/skins/skin1.png"),
	SKIN2("img/skins/skin2.png"),
	SKIN3("img/skins/skin3.png"),
	SKIN4("img/skins/skin4.png");

	private Image image;

	Skin(String s) {
		image = new Image("ch/heigvd/bomberman/client/" + s);
	}

	public Image getImage() {
		return image;
	}
}
