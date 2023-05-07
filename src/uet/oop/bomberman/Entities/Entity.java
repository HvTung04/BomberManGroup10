package uet.oop.bomberman.Entities;

import uet.oop.bomberman.Graphic.RenderInterface;
import uet.oop.bomberman.Graphic.Screen;
import uet.oop.bomberman.Graphic.Sprite;
import uet.oop.bomberman.Level.Coordinates;

/**
 * Abstract class Entity
 */

public abstract class Entity implements RenderInterface {
    protected double x, y;

    protected boolean removed = false;
    protected Sprite sprite;

    @Override
    public abstract void update();  // to update state of entity

    @Override
    public abstract void render(Screen screen); // to render entity in each state

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public abstract boolean collide(Entity other);

    public double getX() {
        return  x;
    }

    public double getY() {
        return y;
    }

    public int getXTile() {
        return Coordinates.pixelToTile(x + sprite.SIZE / 2);
    }

    public int getYTile() {
        return Coordinates.pixelToTile(y - sprite.SIZE / 2);
    }
}
