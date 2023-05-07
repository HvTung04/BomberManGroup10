package uet.oop.bomberman.Entities.Character.Enemy.AI;

import java.util.Random;

public abstract class AI {
    protected Random random = new Random();

    public abstract int calcDirection();
}
