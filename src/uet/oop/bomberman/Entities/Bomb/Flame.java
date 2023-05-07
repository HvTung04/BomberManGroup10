package uet.oop.bomberman.Entities.Bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Entities.Character.Bomber;
import uet.oop.bomberman.Entities.Character.Character;
import uet.oop.bomberman.Entities.Character.Enemy.Enemy;
import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Graphic.Screen;

public class Flame extends Entity {

    protected Board board;
    protected int _direction;
    private int _radius;
    protected int xOrigin, yOrigin;
    protected FlameSegment[] _flameSegments = new FlameSegment[0];

    public Flame(int x, int y, int direction, int radius, Board board) {
        xOrigin = x;
        yOrigin = y;
        this.x = x;
        this.y = y;
        _direction = direction;
        _radius = radius;
        this.board = board;
        createFlameSegments();
    }

    /**
     * create Flame segments
     */
    private void createFlameSegments() {

        _flameSegments = new FlameSegment[calculatePermitedDistance()];

        /**
            last for last segment
         */
        boolean last = false;

        // tạo các segment dưới đây
        int _x = (int)x;
        int _y = (int)y;
        for (int i = 0; i < _flameSegments.length; i++) {
            last = i == _flameSegments.length -1 ? true : false;

            switch (_direction) {
                case 0: _y--; break;
                case 1: _x++; break;
                case 2: _y++; break;
                case 3: _x--; break;
            }
            _flameSegments[i] = new FlameSegment(_x, _y, _direction, last);
        }
    }

    private int calculatePermitedDistance() {
        // thực hiện tính toán độ dài của Flame
        int radius = 0;
        int _x = (int)x;
        int _y = (int)y;
        while(radius < _radius) {
            if(_direction == 0) _y--;
            if(_direction == 1) _x++;
            if(_direction == 2) _y++;
            if(_direction == 3) _x--;

            Entity a = board.getEntity(_x, _y, null);

            if(a instanceof Character) ++radius;

            if(a.collide(this) == false)
                break;

            ++radius;
        }
        return radius;
    }

    public FlameSegment flameSegmentAt(int x, int y) {
        for (int i = 0; i < _flameSegments.length; i++) {
            if(_flameSegments[i].getX() == x && _flameSegments[i].getY() == y)
                return _flameSegments[i];
        }
        return null;
    }

    @Override
    public void update() {}

    @Override
    public void render(Screen screen) {
        for (int i = 0; i < _flameSegments.length; i++) {
            _flameSegments[i].render(screen);
        }
    }

    @Override
    public boolean collide(Entity e) {
        // Bomber Enemy collision
        if (e instanceof Bomber) {
            ((Bomber) e).kill();
            return false;
        }

        if (e instanceof Enemy) {
            ((Enemy) e).kill();
            return false;
        }

        return true;
    }
}
