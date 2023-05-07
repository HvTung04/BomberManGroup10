package uet.oop.bomberman.Entities.Character;

import uet.oop.bomberman.Audio.GameAudioPlayer;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Entities.Bomb.Bomb;
import uet.oop.bomberman.Entities.Bomb.Flame;
import uet.oop.bomberman.Entities.Character.Enemy.Enemy;
import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Entities.Message;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Graphic.Screen;
import uet.oop.bomberman.Graphic.Sprite;
import uet.oop.bomberman.Inputs.KeyboardInputs;
import uet.oop.bomberman.Level.Coordinates;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class Bomber extends Character {
    private List<Bomb> bombs;
    protected KeyboardInputs keyboardInputs;
    /**
     * if < 0 => able to place new BOMB
     * every time place a BOMB reset to 0
     * decrease each update()
     */
    protected int timeBetweenPlaceBombs = 0;

    public Bomber(int x, int y, Board board) {
        super(x,y,board);
        this.bombs = board.getBombs();
        this.keyboardInputs = board.getKeyboardInput();
        this.sprite = Sprite.player_right;
    }

    @Override
    public void update() {
        clearBombs();
        if (!alive) {
            afterKill();
            return;
        }

        if (timeBetweenPlaceBombs < -7500) timeBetweenPlaceBombs = 0;
        else timeBetweenPlaceBombs--;

        animate();
        calcMove();
        detectPlaceBomb();
    }

    // Check if can place BOMB or not
    private void detectPlaceBomb() {
        /**
         * CONDITIONS to place bombs:
         * 1.Button is pressed
         * 2.timeBetweenPlaceBombs and BombRate satisfy
         * (Prevent place multiple Bomb at current place in a short period of time)
         * (Number of bombs can place at this current time)
         * => placeBomb()
         * timeBetween = 0 && BombRate--;
         */

        if (keyboardInputs.space && Game.getBombRate() > 0 && timeBetweenPlaceBombs < 0) {
            int _x = Coordinates.pixelToTile(x + sprite.getSize() / 2);
            int _y = Coordinates.pixelToTile((y+sprite.getSize() / 2) - sprite.getSize());

            placeBomb(_x,_y);
            Game.addBombRate(-1);

            timeBetweenPlaceBombs = 30;
        }
    }

    private void placeBomb(int x, int y) {
        Bomb bomb = new Bomb(x,y,board);
        board.addBomb(bomb);

        GameAudioPlayer placeBombSound = new GameAudioPlayer(GameAudioPlayer.PLACE_BOMB);
        placeBombSound.play();
    }

    private void clearBombs() {
        Iterator<Bomb> ptr = bombs.iterator();

        Bomb bomb;
        while (ptr.hasNext()) {
            bomb = ptr.next();
            if (bomb.isRemoved()) {
                ptr.remove();
                Game.addBombRate(1);
            }
        }
    }

    @Override
    public void render(Screen screen) {
        calcXOffset();
        if (alive) {
            chooseSprite();
        } else this.sprite = Sprite.player_dead1;

        screen.renderEntity((int)x, (int) y - sprite.SIZE, this);
    }

    private void chooseSprite() {
        switch (direction) {
            case 0:
                sprite = Sprite.player_up;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, Animate, 20);
                break;
            case 1:
                sprite = Sprite.player_right;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, Animate, 20);
                break;
            case 2:
                sprite = Sprite.player_down;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_down_1,Sprite.player_down_2,Animate,20);
                break;
            case 3:
                sprite = Sprite.player_left;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_left_1,Sprite.player_left_2, Animate, 20);
                break;
            default:
                sprite = Sprite.player_right;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, Animate, 20);
                break;
        }
    }

    private void calcXOffset() {
        int xScroll = Screen.calcXOffset(board,this);
        Screen.setOffset(xScroll, 0);
    }

    @Override
    public boolean collide(Entity other) {
        // Flame collision
        // Enemy collision

        if (other instanceof Flame) {
            kill();
            return false;
        }

        if (other instanceof Enemy) {
            kill();
            return true;
        }

        return true;
    }

    @Override
    protected void calcMove() {
        /**
         * Regconize inputs then perform move()
         */

        int xa = 0, ya = 0;
        if (keyboardInputs.up) ya--;
        if (keyboardInputs.down) ya++;
        if (keyboardInputs.left) xa--;
        if (keyboardInputs.right) xa++;

        if (xa != 0 || ya != 0) {
            move(xa * Game.getBomberSpeed(), ya * Game.getBomberSpeed());
            moving = true;
        } else moving = false;
    }

    @Override
    protected void move(double xa, double ya) {
        /**
         * Check by canMove() => move()
         */

        if (canMove(0, ya)) this.y += ya;
        if (canMove(xa,0)) this.x += xa;

        if (keyboardInputs.up) direction = 0;
        if (keyboardInputs.right) direction = 1;
        if (keyboardInputs.down) direction = 2;
        if (keyboardInputs.left) direction = 3;
    }

    @Override
    public void kill() {
        if (!alive) return;
        alive = false;
        board.addLives(-1);
        Message msg = new Message("-1 LIVE", getXMessage(),getYMessage(),2, Color.white,14);
        board.addMessage(msg);
        board.getMusicPlayer().stop();

        GameAudioPlayer deadAudio = new GameAudioPlayer(GameAudioPlayer.DEAD);
        deadAudio.play();
    }

    @Override
    protected void afterKill() {
        if (timeAfter > 0) timeAfter--;
        else {
            if (bombs.size() == 0) {
                if (board.getLives() == 0)
                    board.endGame();
                else {
                    board.restartLevel();
                    board.getMusicPlayer().play();  // restart music
                }
            }
        }
    }

    @Override
    protected boolean canMove(double x, double y) {
        for (int c = 0; c < 4; c++) {
            double xt = ((this.x + x) + c % 2 *11) / Game.TILES_SIZE;
            double yt = ((this.y + y) + c / 2 * 12 - 13) / Game.TILES_SIZE;

            Entity e = board.getEntity(xt,yt,this);

            if (!e.collide(this)) return false;
        }

        return true;
    }

}
