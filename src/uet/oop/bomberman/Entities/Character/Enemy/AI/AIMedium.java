package uet.oop.bomberman.Entities.Character.Enemy.AI;

import uet.oop.bomberman.Entities.Character.Bomber;
import uet.oop.bomberman.Entities.Character.Enemy.Enemy;
import uet.oop.bomberman.Entities.Entity;

public class AIMedium extends AI {
    Bomber bomber;
    Enemy enemy;


    public AIMedium(Bomber bomber, Enemy enemy) {
        this.bomber = bomber;
        this.enemy = enemy;
    }
    @Override
    public int calcDirection() {
        if (bomber == null)
            return random.nextInt(4);

        int vertical = random.nextInt(2);

        if (vertical == 1) {
            int v = calcRowDir();
            if (v != -1) return v;
            else return calcColDir();
        } else {
            int h = calcColDir();

            if (h != -1) return h;
            else return calcRowDir();
        }
    }

    private int calcRowDir() {
        if (bomber.getYTile() < enemy.getYTile()) return 0;
        else if (bomber.getYTile() > enemy.getYTile()) return 2;

        return -1;
    }

    private int calcColDir() {
        if (bomber.getXTile() < enemy.getXTile()) return 3;
        else if (bomber.getYTile() > enemy.getYTile()) return 1;

        return -1;
    }
}
