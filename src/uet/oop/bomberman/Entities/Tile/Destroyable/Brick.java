package uet.oop.bomberman.Entities.Tile.Destroyable;

import uet.oop.bomberman.Graphic.Screen;
import uet.oop.bomberman.Graphic.Sprite;
import uet.oop.bomberman.Level.Coordinates;

public class Brick extends DestroyableTile {

    public Brick(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(Screen screen) {
        int _x = Coordinates.tileToPixel(this.x);
        int _y = Coordinates.tileToPixel(this.y);

        if (destroyed) {
            this.sprite = movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1, Sprite.brick_exploded2);

            screen.renderEntityBelowSprite(_x,_y,this,belowSprite);
        } else
            screen.renderEntity(_x,_y,this);
    }
}
