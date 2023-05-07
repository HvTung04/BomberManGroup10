package uet.oop.bomberman.Entities;

/**
 * Entities that move
 */

public abstract class AnimatedEntity extends Entity {
    protected int Animate = 0;
    protected final int MAX_ANIMATE = 7500;

    protected void animate() {
        if (Animate < MAX_ANIMATE)
            Animate++;
        else Animate = 0;
    }
}
