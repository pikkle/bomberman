package ch.heigvd.bomberman.client.views.tabs.mapEditor;

import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.common.game.Box;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;

/**
 * Created by matthieu.villard on 21.05.2016.
 */
public class InsertHandler extends Observable {

	private ImageView dragImageView;
	private Class clazz;
	private ArenaRenderer renderer;
	private Node src;
	private Node dst;

	public InsertHandler(ArenaRenderer renderer, Node src, Node dst, Class clazz) {
		this.renderer = renderer;
		this.src = src;
		this.dst = dst;
		this.clazz = clazz;

		dragImageView = new ImageView(((ImageView) src).getImage());
		dragImageView.setFitHeight(renderer.getCellHeight());
		dragImageView.setFitWidth(renderer.getCellWidth());

		registerDragEvent();
		registerDropEvent();
	}


	public void start(MouseEvent t) {
		Pane root = (Pane) src.getScene().getRoot();
		root.getChildren().add(dragImageView);

		dragImageView.setOpacity(0.5);
		dragImageView.toFront();
		dragImageView.setMouseTransparent(true);
		dragImageView.setVisible(true);
		dragImageView.relocate((int) (t.getSceneX() - dragImageView.getBoundsInLocal().getWidth() / 2),
		                       (int) (t.getSceneY() - dragImageView.getBoundsInLocal().getHeight() / 2));

		Dragboard db = src.startDragAndDrop(TransferMode.ANY);
		ClipboardContent content = new ClipboardContent();

		content.putString("insert");

		db.setContent(content);

		t.consume();
	}

	public void dragOver(DragEvent e) {
		Point2D localPoint = src.getScene().getRoot().sceneToLocal(new Point2D(e.getSceneX(), e.getSceneY()));
		dragImageView.relocate((int) (localPoint.getX() - dragImageView.getBoundsInLocal().getWidth() / 2),
		                       (int) (localPoint.getY() - dragImageView.getBoundsInLocal().getHeight() / 2));
		e.consume();
	}

	public void stop(DragEvent e) {
		((Pane) dragImageView.getParent()).getChildren().remove(dragImageView);
		unRegisterDragEvent();
		unRegisterDropEvent();
		e.consume();
	}

	private void registerDragEvent() {
		src.getScene().getRoot().setOnDragOver(this::dragOver);

		src.setOnDragDone(this::stop);
	}

	private void unRegisterDragEvent() {
		src.setOnDragOver(null);

		src.setOnDragDone(null);
	}

	private void registerDropEvent() {
		dst.setOnDragOver(t -> {
			try {
				if (!(clazz.newInstance() instanceof PowerUp) || renderer.getArena()
				                                                         .elements(
						                                                         renderer.getCell(t.getX(), t.getY())).stream()
				                                                         .filter(element -> element instanceof Box)
				                                                         .findFirst()
				                                                         .isPresent()) {
					t.acceptTransferModes(TransferMode.ANY);
					DropShadow dropShadow = new DropShadow();
					dropShadow.setRadius(5.0);
					dropShadow.setOffsetX(3.0);
					dropShadow.setOffsetY(3.0);
					dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
					dst.setEffect(dropShadow);
					//Don't consume the event.  Let the layers below process the DragOver event as well so that the
					//translucent container image will follow the cursor.
					//t.consume();
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});

		dst.setOnDragExited(t -> {
			dst.setEffect(null);
			t.consume();
		});

		dst.setOnDragDropped(t -> {
			Dragboard db = t.getDragboard();
			if (db.getString().equals("insert")) {
				try {
					Constructor constructor = findConstructor(clazz);
					Element element = (Element) constructor.newInstance(renderer.getCell(t.getX(), t.getY()),
					                                                    renderer.getArena());
					if (element instanceof PowerUp) {
						element.delete();
						((Box) element.arena().elements(element.position()).get(0)).setPowerUp((PowerUp) element);
					}


					setChanged();
					notifyObservers(element);
				} catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
					e.printStackTrace();
				}

				t.setDropCompleted(true);
			}
		});
	}

	private void unRegisterDropEvent() {
		dst.setOnDragOver(null);

		dst.setOnDragExited(null);

		dst.setOnDragDropped(null);
	}

	private <T extends Element> Constructor findConstructor(Class<T> dataClass) {
		Constructor[] constructors;
		Constructor[] arr$;
		try {
			arr$ = dataClass.getDeclaredConstructors();
			constructors = arr$;
		} catch (Exception var8) {
			throw new IllegalArgumentException("Can\'t lookup declared constructors for " + dataClass, var8);
		}

		arr$ = constructors;
		int len$ = constructors.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			Constructor con = arr$[i$];
			if (con.getParameterTypes().length == 2) {
				if (!con.isAccessible()) {
					try {
						con.setAccessible(true);
					} catch (SecurityException var7) {
						throw new IllegalArgumentException("Could not open access to constructor for " + dataClass);
					}
				}

				return con;
			}
		}

		if (dataClass.getEnclosingClass() == null) {
			throw new IllegalArgumentException("Can\'t find a constructor for " + dataClass);
		} else {
			throw new IllegalArgumentException(
					"Can\'t find a constructor for " + dataClass + ".  Missing static on inner class?");
		}
	}
}
