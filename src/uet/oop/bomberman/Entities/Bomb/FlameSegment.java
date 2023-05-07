package uet.oop.bomberman.Entities.Bomb;

import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Graphic.Screen;
import uet.oop.bomberman.Graphic.Sprite;

public class FlameSegment extends Entity {
    protected boolean last;

    /**
     *
     * @param x
     * @param y
     * @param direction
     * @param last Since last segment sprite is diff from other segments
     */
    public FlameSegment(int x, int y, int direction, boolean last) {
        this.x = x;
        this.y = y;
        this.last = last;

        switch (direction) {
            case 0:
                if (!last)
                    this.sprite = Sprite.explosion_vertical2;
                else this.sprite = Sprite.explosion_vertical_top_last2;

                break;
            case 1:
                if (!last)
                    this.sprite = Sprite.explosion_horizontal2;
                else this.sprite = Sprite.explosion_horizontal_right_last2;

                break;
            case 2:
                if (!last)
                    this.sprite = Sprite.explosion_vertical2;
                else this.sprite = Sprite.explosion_vertical_down_last2;

                break;
            case 3:
                if (!last)
                    this.sprite = Sprite.explosion_horizontal2;
                else this.sprite = Sprite.explosion_horizontal_left_last2;

                break;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Screen screen) {
        int xt = (int) this.x << 4;
        int yt = (int) this.y << 4;

        screen.renderEntity(xt,yt,this);
    }

    @Override
    public boolean collide(Entity other) {
        return false;
    }
}
