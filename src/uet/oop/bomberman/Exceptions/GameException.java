package uet.oop.bomberman.Exceptions;

public class GameException extends Exception {
    public GameException() {

    }

    public GameException(String msg) {
        super(msg);
    }

    public GameException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public GameException(Throwable cause) {
        super(cause);
    }
}
