package uet.oop.bomberman.Entities.Bomb;

import uet.oop.bomberman.Audio.GameAudioPlayer;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Entities.AnimatedEntity;
import uet.oop.bomberman.Entities.Character.Bomber;
import uet.oop.bomberman.Entities.Character.Character;
import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Graphic.Screen;
import uet.oop.bomberman.Graphic.Sprite;
import uet.oop.bomberman.Level.Coordinates;



public class Bomb extends AnimatedEntity {
    protected double timeToExplode = 120;
    public int timeAfter = 20;
    protected Board board;
    protected Flame[] flames;
    protected boolean exploded = false;
    protected boolean allowedToStepON = true;

    public Bomb(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
        this.sprite = Sprite.bomb;
    }

    @Override
    public void update() {
        if (timeToExplode > 0)
            timeToExplode--;
        else {
            if (!exploded)
                explode();
            else
                updateFlames();

            if (timeAfter > 0)
                timeAfter--;
            else remove();
        }
        animate();
    }

    @Override
    public void render(Screen screen) {
        if (exploded) {
            sprite = Sprite.bomb_exploded2;
            renderFlames(screen);
        } else sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, Animate, 60);

        int xt = (int) x << 4;
        int yt = (int) y << 4;

        screen.renderEntity(xt, yt, this);
    }

    @Override
    public boolean collide(Entity other) {
        // Bomber exit after place bomb
        if (other instanceof Bomber) {
            double diffX = other.getX() - Coordinates.tileToPixel(getX());
            double diffY = other.getY() - Coordinates.tileToPixel(getY());

            if (!(diffX >= -10 && diffX < 16 && diffY >= 1 && diffY <= 28)) allowedToStepON = false;

            return allowedToStepON;
        }

        // Catch other bomb's flame -> explode
        if (other instanceof Flame) {
            timeToExplode = 0;
            return true;
        }

        return false;
    }

    public void renderFlames(Screen screen) {
        for (int i = 0; i < flames.length; i++) {
            flames[i].render(screen);
        }
    }

    public void updateFlames() {
        for (int i = 0; i < flames.length; i++) {
            flames[i].update();
        }
    }

    public void explode() {
        exploded = true;

        // When hit characters

        Character c = board.getCharacterAt(x,y);
        if (c != null) c.kill();

        allowedToStepON = true;

        // create Flames;

        flames = new Flame[4];

        for (int i = 0; i < flames.length; i++) {
            flames[i] = new Flame((int)x, (int)y, i, Game.getBombRadius(), board);
        }

        GameAudioPlayer explodeAudio = new GameAudioPlayer(GameAudioPlayer.EXPLOSION);
        explodeAudio.play();
    }

    public FlameSegment flameAt(int x, int y) {
        if (!exploded) return null;

        for (int i = 0; i < flames.length; i++) {
            if (flames[i] == null) return null;
            FlameSegment e = flames[i].flameSegmentAt(x,y);
            if (e != null) return e;
        }

        return null;
    }
}
