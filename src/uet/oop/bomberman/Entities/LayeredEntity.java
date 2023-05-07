package uet.oop.bomberman.Entities;

import uet.oop.bomberman.Entities.Tile.Destroyable.DestroyableTile;
import uet.oop.bomberman.Graphic.Screen;

import java.util.LinkedList;

/**
 * Position which has multiple entities
 */

public class LayeredEntity extends Entity {
    protected LinkedList<Entity> entities = new LinkedList<>();

    public LayeredEntity(int x, int y, Entity ... entities) {
        this.x = x;
        this.y = y;

        for (int i = 0; i < entities.length; i++) {
            this.entities.add(entities[i]);

            if (i > 1) {
                if (entities[i] instanceof DestroyableTile)
                    ( (DestroyableTile)entities[i] ).addBelowSprite( entities[i-1].getSprite() );
            }
        }
    }

    @Override
    public void update() {
        clearRemoved();
        getTopEntity().update();
    }

    @Override
    public void render(Screen screen) {
        getTopEntity().render(screen);
    }

    public Entity getTopEntity() {
        return entities.getLast();
    }

    public void clearRemoved() {
        Entity top = getTopEntity();

        if (top.isRemoved()) {
            entities.removeLast();
        }
    }

    public void addBeforeTop(Entity other) {
        entities.add(entities.size() - 1, other);
    }

    @Override
    public boolean collide(Entity other) {
        // top entity collision
        return getTopEntity().collide(other);
    }
}
