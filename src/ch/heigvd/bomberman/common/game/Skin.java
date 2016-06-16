package ch.heigvd.bomberman.common.game;

public enum Skin {

	SKIN1 {
		@Override
		public String path() {
			return src + "skin1.png";
		}
	}, SKIN2 {
		@Override
		public String path() {
			return src + "skin2.png";
		}
	}, SKIN3 {
		@Override
		public String path() {
			return src + "skin3.png";
		}
	}, SKIN4 {
		@Override
		public String path() {
			return src + "skin4.png";
		}
	};

	private final static String src = "ch/heigvd/bomberman/common/game/img/skins/";

	public abstract String path();
}
