package uet.oop.bomberman.Entities.Character.Enemy;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Entities.Character.Enemy.AI.AILow;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Graphic.Sprite;

public class Balloon extends Enemy {

    public Balloon (int x, int y, Board board) {
        super(x,y,board, Sprite.balloom_dead, Game.getBomberSpeed() / 2, 100);

        this.sprite = Sprite.balloom_left1;

        this.brain = new AILow();
        this.direction = this.brain.calcDirection();
    }

    @Override
    protected void chooseSprite() {
        switch (direction) {
            case 0:
            case 1:
                this.sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, Animate, 60);
                break;
            case 2:
            case 3:
                this.sprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, Animate, 60);
                break;
        }
    }

}
