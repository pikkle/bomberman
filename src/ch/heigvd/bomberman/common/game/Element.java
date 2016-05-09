package ch.heigvd.bomberman.common.game;

import javafx.scene.image.ImageView;

import java.awt.*;
import java.util.Observable;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public abstract class Element extends Observable {
    protected Point position;
    private ImageView renderView;

    public Element(Point position, ImageView renderView){
        this.position = position;
        this.renderView = renderView;
    }

    public Point getPosition(){
        return position;
    }

    public ImageView render(){
        return renderView;
    }
}
