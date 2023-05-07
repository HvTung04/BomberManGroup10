package uet.oop.bomberman.Entities.Tile.Destroyable;

import uet.oop.bomberman.Entities.Bomb.Flame;
import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Entities.Tile.Tile;
import uet.oop.bomberman.Graphic.Sprite;

public class DestroyableTile extends Tile {
    private final int MAX_ANIMATE = 7500;
    private int Animate = 0;
    protected boolean destroyed = false;
    protected int timeToDissappear = 20;
    protected Sprite belowSprite = Sprite.grass;

    public DestroyableTile(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public void update() {
        if (destroyed) {
            if (Animate < MAX_ANIMATE) Animate++;
            if (timeToDissappear > 0) timeToDissappear--;
            else remove();
        }
    }

    public void destroy() {
        destroyed = true;
    }

    @Override
    public boolean collide (Entity other) {
        // Flame collision
        if (other instanceof Flame)
            destroy();

        return false;
    }

    public void addBelowSprite(Sprite sprite) {
        this.belowSprite = sprite;
    }

    protected Sprite movingSprite(Sprite normal, Sprite x1, Sprite x2) {
        int calc = Animate % 30;

        if (calc < 10) {
            return normal;
        }

        if (calc < 20) {
            return x1;
        }

        return x2;
    }
}
