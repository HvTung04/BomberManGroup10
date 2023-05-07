package uet.oop.bomberman.Entities.Tile.Item;

import uet.oop.bomberman.Entities.Tile.Tile;
import uet.oop.bomberman.Graphic.Sprite;

public abstract class Item extends Tile {

    public Item(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }
}
