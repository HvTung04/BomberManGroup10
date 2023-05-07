package uet.oop.bomberman.Entities.Tile;

import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Graphic.Sprite;

public class Grass extends Tile {
    public Grass(int x, int y, Sprite sprite) {
        super(x,y,sprite);
    }

    /**
     * Let any pass thru.
     * @param other
     * @return
     */
    @Override
    public boolean collide (Entity other) {
        return true;
    }
}
