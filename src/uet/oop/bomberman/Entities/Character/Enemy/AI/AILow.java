package uet.oop.bomberman.Entities.Character.Enemy.AI;

public class AILow extends AI {

    @Override
    public int calcDirection() {
        return random.nextInt(4);
    }
}
