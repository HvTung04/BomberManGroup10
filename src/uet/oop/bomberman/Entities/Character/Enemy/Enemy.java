package uet.oop.bomberman.Entities.Character.Enemy;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Entities.Bomb.Flame;
import uet.oop.bomberman.Entities.Bomb.FlameSegment;
import uet.oop.bomberman.Entities.Character.Bomber;
import uet.oop.bomberman.Entities.Character.Character;
import uet.oop.bomberman.Entities.Character.Enemy.AI.AI;
import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Entities.Message;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Graphic.Screen;
import uet.oop.bomberman.Graphic.Sprite;
import uet.oop.bomberman.Level.Coordinates;

import java.awt.*;

public abstract class Enemy extends Character {
    protected int points;
    protected double speed;
    protected AI brain;

    protected final double MAX_STEPS;
    protected final double rest;
    protected double steps;

    protected int finalAnimation = 30;
    protected Sprite deadSprite;

    public Enemy(int x, int y, Board board, Sprite deadSprite, double speed, int points) {
        super(x,y,board);

        this.points = points;
        this.speed = speed;

        this.MAX_STEPS = Game.TILES_SIZE / this.speed;
        this.rest = (MAX_STEPS - (int) MAX_STEPS) / MAX_STEPS;
        this.steps = MAX_STEPS;

        this.timeAfter = 20;
        this.deadSprite = deadSprite;
    }


    @Override
    public void update() {
        animate();

        if (!alive) {
            afterKill();
            return;
        }

        if (alive)
            calcMove();

    }

    @Override
    public void render(Screen screen) {
        if (alive) chooseSprite();
        else {
            if (timeAfter > 0) {
                this.sprite = deadSprite;
                this.Animate = 0;
            } else this.sprite = Sprite.movingSprite(Sprite.mob_dead1,Sprite.mob_dead2,Sprite.mob_dead3,Animate,60);
        }

        screen.renderEntity((int)x,(int)y - sprite.SIZE, this);
    }

    protected abstract void chooseSprite();

    @Override
    public boolean collide(Entity other) {
        // Flame collision
        // Bomber collision
        if (other instanceof Flame) {
            kill();
            return false;
        }

        if (other instanceof Bomber) {
            ((Bomber) other).kill();
            return false;
        }

        return true;
    }

    @Override
    protected void calcMove() {
        /**
         * Move by ai
         * canMove() => move()
         */
        int xa = 0, ya = 0;
        if (steps <= 0) {
            this.direction = brain.calcDirection();
            this.steps = MAX_STEPS;
        }

        if (direction == 0) ya--;
        if (direction == 1) ya++;
        if (direction == 2) xa--;
        if (direction == 3) xa++;

        if (canMove(xa, ya)) {
            this.steps -= 1 + rest;
            move(xa * speed, ya * speed);
            moving = true;
        } else {
            this.steps = 0;
            moving = false;
        }
    }

    @Override
    protected void move(double xa, double ya) {
        if (!alive) return;
        this.y += ya;
        this.x += xa;
    }

    @Override
    public void kill() {
        if (!alive) return;;
        alive = false;
        board.addPoints(points);

        Message msg = new Message("+" + points, getXMessage(), getYMessage(), 2, Color.white,14);
        board.addMessage(msg);
    }

    @Override
    protected void afterKill() {
        if (timeAfter > 0) timeAfter--;
        else {
            if (finalAnimation > 0) finalAnimation--;
            else remove();
        }
    }

    @Override
    protected boolean canMove(double x, double y) {
        double xr = this.x, yr = this.y - 16;
        if (direction == 0) {
            yr += sprite.getSize() - 1;
            xr += sprite.getSize() / 2;
        }
        if (direction == 1) {
            yr += sprite.getSize() / 2;
            xr += 1;
        }
        if (direction == 2) {
            xr += sprite.getSize() / 2;
            yr += 1;
        }
        if (direction == 3) {
            xr += sprite.getSize() - 1;
            yr += sprite.getSize() / 2;
        }

        int xx = Coordinates.pixelToTile(xr) + (int) x;
        int yy = Coordinates.pixelToTile(yr) + (int) y;

        Entity e = board.getEntity(xx, yy, this);

        return e.collide(this);
    }
}
