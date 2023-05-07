package uet.oop.bomberman.Entities.Character.Enemy;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Entities.Character.Enemy.AI.AIMedium;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Graphic.Sprite;

public class Oneal extends Enemy{

    public Oneal(int x, int y, Board board) {
        super(x,y,board, Sprite.oneal_dead, Game.getBomberSpeed(), 200);
        this.sprite = Sprite.oneal_left1;

        this.brain = new AIMedium(board.getBomber(), this);
        direction = brain.calcDirection();
    }
    @Override
    protected void chooseSprite() {
        switch (direction) {
            case 0:
            case 1:
                if (moving)
                    this.sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, Animate, 60);
                else this.sprite = Sprite.oneal_left1;
                break;
            case 2:
            case 3:
                if (moving)
                    this.sprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, Animate, 60);
                else this.sprite = Sprite.oneal_left1;
                break;
        }
    }
}
