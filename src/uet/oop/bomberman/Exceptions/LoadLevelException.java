package uet.oop.bomberman.Exceptions;

public class LoadLevelException extends GameException {

    public LoadLevelException() {
    }

    public LoadLevelException(String msg) {
        super(msg);
    }

    public LoadLevelException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public LoadLevelException(Throwable cause) {
        super(cause);
    }
}
