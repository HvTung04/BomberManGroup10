package uet.oop.bomberman.Entities.Character;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Entities.AnimatedEntity;
import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Graphic.Screen;

public abstract class Character extends AnimatedEntity {
    protected Board board;
    protected int direction = -1;
    protected boolean alive = true;
    protected boolean moving = false;
    public int timeAfter = 40;

    public Character(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
    }

    @Override
    public abstract void update();

    @Override
    public abstract void render(Screen screen);

    protected abstract void calcMove();
    protected abstract void move(double xa, double ya);

    public abstract void kill();

    protected abstract void afterKill();

    protected abstract boolean canMove(double x, double y);

    protected double getXMessage() {
        return (x * Game.SCALE) * (sprite.SIZE / 2 * Game.SCALE);
    }

    protected double getYMessage() {
        return (y * Game.SCALE) * (sprite.SIZE / 2 * Game.SCALE);
    }
}
