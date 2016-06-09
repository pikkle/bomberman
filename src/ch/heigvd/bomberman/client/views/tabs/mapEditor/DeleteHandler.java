package ch.heigvd.bomberman.client.views.tabs.mapEditor;

import ch.heigvd.bomberman.client.views.render.ArenaRenderer;
import ch.heigvd.bomberman.client.views.render.ImageViewPane;
import ch.heigvd.bomberman.common.game.Element;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by matthieu.villard on 22.05.2016.
 */
public class DeleteHandler {
	private ImageView dragImageView;
	private Element element;
	private ArenaRenderer renderer;
	private Node src;
	private Node dst;
	private double initX;
	private double initY;

	public DeleteHandler(ArenaRenderer renderer, Node src, Node dst, Element element) {
		this.renderer = renderer;
		this.src = src;
		this.dst = dst;
		this.element = element;

		dragImageView = new ImageView(((ImageViewPane) src).getImageView().getImage());
		dragImageView.setFitHeight(renderer.getCellHeight());
		dragImageView.setFitWidth(renderer.getCellWidth());

		registerDropEvent();
		registerDragEvent();
	}


	public void start(MouseEvent t) {
		Pane root = (Pane) src.getScene().getRoot();
		root.getChildren().add(dragImageView);
		src.setVisible(false);

		dragImageView.setOpacity(0.5);
		dragImageView.toFront();
		dragImageView.setMouseTransparent(true);
		dragImageView.setVisible(true);
		dragImageView.relocate((int) (t.getSceneX() - dragImageView.getBoundsInLocal().getWidth() / 2),
		                       (int) (t.getSceneY() - dragImageView.getBoundsInLocal().getHeight() / 2));

		Dragboard db = src.startDragAndDrop(TransferMode.ANY);
		ClipboardContent content = new ClipboardContent();

		content.putString("delete");

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
		src.setVisible(true);
		dragImageView.setY(0);
		dragImageView.setX(0);
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
			t.acceptTransferModes(TransferMode.ANY);
			DropShadow dropShadow = new DropShadow();
			dropShadow.setRadius(5.0);
			dropShadow.setOffsetX(3.0);
			dropShadow.setOffsetY(3.0);
			dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
			dst.setEffect(dropShadow);
			dragImageView.setFitWidth(((ImageView) dst).getFitWidth());
			dragImageView.setFitHeight(((ImageView) dst).getFitHeight());
			//Don't consume the event.  Let the layers below process the DragOver event as well so that the
			//translucent container image will follow the cursor.
			//t.consume();
		});

		dst.setOnDragExited(t -> {
			dst.setEffect(null);
			dragImageView.setFitHeight(renderer.getCellHeight());
			dragImageView.setFitWidth(renderer.getCellWidth());
			t.consume();
		});

		dst.setOnDragDropped(t -> {
			Dragboard db = t.getDragboard();
			if (db.getString().equals("delete")) {
				renderer.getArena().remove(element);
				t.setDropCompleted(true);
			}
		});
	}

	private void unRegisterDropEvent() {
		dst.setOnDragOver(null);

		dst.setOnDragExited(null);

		dst.setOnDragDropped(null);
	}
}
