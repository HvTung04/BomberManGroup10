package uet.oop.bomberman.Entities.Tile;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Entities.Character.Bomber;
import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Graphic.Sprite;

public class Portal extends Tile {
    protected Board board;

    public Portal(int x, int y, Board board, Sprite sprite) {
        super(x,y,sprite);
        this.board = board;
    }

    @Override
    public boolean collide(Entity other) {
        /**
         * Handle bomber Collision
         */
        if (other instanceof Bomber) {
            if (board.detectNoEnemiesLeft() == false) return false;

            if (other.getXTile() == getX() && other.getYTile() == getY()) {
                if (board.detectNoEnemiesLeft())
                    board.nextLevel();
            }

            return true;
        }
        return false;
    }
}
