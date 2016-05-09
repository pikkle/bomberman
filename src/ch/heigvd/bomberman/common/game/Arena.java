package ch.heigvd.bomberman.common.game;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class Arena
{
    private int width;
    private int height;
    private List<Element> elements = new LinkedList<Element>();

    public Arena(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Bomberman addPlayer(Bomberman bomberman){
        for(int i = 0; i < 4; i++){
            Point position = new Point(1 + i / 2, 1 + i % 2);
            if(isEmpty(position)){
                bomberman.move(position);
                Bomberman bomberman1 = new Bomberman(position, Skin.values()[i]);
                elements.add(bomberman);
                return bomberman;
            }
        }
        return null;
    }

    public boolean isEmpty(Point position){
        return elements.stream().noneMatch(element -> element.getPosition().equals(position));
    }

    public List<Element> getElements(){
        return elements;
    }

    protected void add(Element element) throws Exception {
        Point position = element.getPosition();
        if(!isEmpty(position)){
            throw new Exception("Cell already occupied!");
        }
        elements.add(element);
    }
}
