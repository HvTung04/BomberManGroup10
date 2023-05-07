package uet.oop.bomberman.Entities.Tile;

import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Graphic.Screen;
import uet.oop.bomberman.Graphic.Sprite;
import uet.oop.bomberman.Level.Coordinates;

/**
 * Stable entities
 */

public abstract class Tile extends Entity {
    public Tile (int x, int y, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Screen screen) {
        screen.renderEntity(Coordinates.tileToPixel(this.x), Coordinates.tileToPixel(this.y), this);
    }

    /**
     * not allow pass thru.
     * @param other
     * @return
     */
    @Override
    public boolean collide(Entity other) {
        return false;
    }
}
