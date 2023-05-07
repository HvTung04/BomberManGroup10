package uet.oop.bomberman.Entities.Tile.Item;

import uet.oop.bomberman.Audio.GameAudioPlayer;
import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Graphic.Sprite;

public class BombItem extends Item {
    protected boolean active;

    public BombItem(int x, int y, Sprite sprite) {
        super(x,y,sprite);
        active = false;
    }

    @Override
    public boolean collide (Entity other) {
        /**
         * Active when bomber step on
         */

        if (other instanceof BombItem) {
            GameAudioPlayer powerUpAudio = new GameAudioPlayer(GameAudioPlayer.POWER_UP);
            powerUpAudio.play();

            if (!active) {
                active = true;
                Game.addBombRate(1);
            }
            remove();
            return true;
        }

        return false;
    }
}
