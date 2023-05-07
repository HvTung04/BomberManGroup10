package uet.oop.bomberman.Entities.Tile.Item;

import uet.oop.bomberman.Audio.GameAudioPlayer;
import uet.oop.bomberman.Entities.Character.Bomber;
import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Graphic.Sprite;

public class SpeedItem extends Item {
    protected boolean active;
    public SpeedItem(int x, int y, Sprite sprite) {
        super(x, y, sprite);
        active = false;
    }

    @Override
    public boolean collide(Entity other) {
        if (other instanceof Bomber) {
            GameAudioPlayer powerUpAudio = new GameAudioPlayer(GameAudioPlayer.POWER_UP);
            powerUpAudio.play();

            if (!active) {
                active = true;
                Game.buffBomberSpeed(0.1);
            }
            remove();
            return true;
        }
        return false;
    }

}
