package uet.oop.bomberman;

import uet.oop.bomberman.Audio.GameAudioPlayer;
import uet.oop.bomberman.Entities.Bomb.Bomb;
import uet.oop.bomberman.Entities.Bomb.FlameSegment;
import uet.oop.bomberman.Entities.Character.Bomber;
import uet.oop.bomberman.Entities.Character.Character;
import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Entities.Message;
import uet.oop.bomberman.Exceptions.LoadLevelException;
import uet.oop.bomberman.Graphic.RenderInterface;
import uet.oop.bomberman.Graphic.Screen;
import uet.oop.bomberman.Inputs.KeyboardInputs;
import uet.oop.bomberman.Level.FileLevelLoader;
import uet.oop.bomberman.Level.LevelLoader;


import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Controls, level loading and rendering
 */

public class Board implements RenderInterface {
    protected LevelLoader levelLoader;
    protected Game game;
    protected KeyboardInputs keyboardInputs;
    protected Screen screen;

    private GameAudioPlayer audioPlayer;

    public uet.oop.bomberman.Entities.Entity[] entities;
    public List<Character> characters = new ArrayList<>();
    protected List<Bomb> bombs = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    private int screenShowing = -1; // endGame-LevelChange-Pause

    private int time = Game.TIME;
    private int points = Game.POINTS;
    private int lives = Game.LIVES;

    public Board (Game game, KeyboardInputs keyboardInputs, Screen screen) {
        this.game = game;
        this.keyboardInputs = keyboardInputs;
        this.screen = screen;
        this.audioPlayer = new GameAudioPlayer(GameAudioPlayer.BACKGROUND_MUSIC);
        audioPlayer.loop();

        loadLevel(1);
    }

    public void loadLevel(int level) {
        this.time = Game.TIME;
        this.screenShowing = 2;
        this.game.resetScreenDelay();
        this.game.pauseGame();
        this.characters.clear();
        this.bombs.clear();
        this.messages.clear();

        try {
            levelLoader = new FileLevelLoader(this, level);
            entities = new Entity[levelLoader.getHeight() * levelLoader.getWidth()];

            levelLoader.createEntities();
        } catch (LoadLevelException e) {
            endGame();
        }
    }

    @Override
    public void update() {
        if (game.isPaused()) return;

        updateEntities();
        updateCharacters();
        updateBombs();
        detectEndGame();

        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i);
            if (c.isRemoved()) characters.remove(i);    // Check chars
        }
    }

    @Override
    public void render(Screen screen) {
        if (game.isPaused()) return;

        // Render visible part only

        int x0 = Screen.xOffset >> 4; //tile precision, -> left X
        int x1 = (Screen.xOffset + screen.getWidth() + Game.TILES_SIZE) / Game.TILES_SIZE; // -> right X
        int y0 = Screen.yOffset >> 4;
        int y1 = (Screen.yOffset + screen.getHeight()) / Game.TILES_SIZE; //render one tile plus to fix black margins

        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                entities[x + y * levelLoader.getWidth()].render(screen);
            }
        }

        renderBombs(screen);
        renderCharacter(screen);
    }

    private void resetProperties() {
        this.points = Game.POINTS;
        this.lives = Game.LIVES;

        this.game.bomber_Speed = 1.0;
        this.game.bomb_Radius = 1;
        this.game.bomb_Rate = 1;
    }

    public void restartLevel() {
        loadLevel(levelLoader.getLevel());
    }

    public void nextLevel() {
        loadLevel(levelLoader.getLevel() + 1);
    }

    protected void detectEndGame() {
        if (time <= 0) endGame();
    }

    public void endGame() {
        this.screenShowing = 1;
        this.game.resetScreenDelay();
        this.game.pauseGame();
    }

    public boolean detectNoEnemiesLeft() {
        int total = 0;
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i) instanceof  Bomber == false)
                total++;
        }
        return total == 0;
    }

    public void drawScreen(Graphics g) {
        switch (screenShowing) {
            case 1:
                screen.drawEndGame(g, points);
                break;
            case 2:
                screen.screenChangeLevel(g, levelLoader.getLevel());
                break;
            case 3:
                screen.screenPaused(g);
                break;
        }
    }

    public uet.oop.bomberman.Entities.Entity getEntity(double x, double y, Character c) {
        Entity res = null;

        res = getFlameSegmentAt((int) x, (int) y);
        if (res != null) return res;

        res = getBombAt(x, y);
        if( res != null) return res;

        res = getCharacterAtExcluding((int)x, (int)y, c);
        if( res != null) return res;

        res = getEntityAt((int)x, (int)y);

        return res;
    }

    public Bomb getBombAt(double x, double y) {
        Iterator<Bomb> bombPos = bombs.iterator();

        Bomb b;
        while(bombPos.hasNext()) {
            b = bombPos.next();
            if (b.getX() == (int) x && b.getY() == (int) y)
                return b;
        }

        return null;
    }

    public Character getCharacterAt(double x, double y) {
        Iterator p = this.characters.iterator();

        Character current;
        do {
            if (!p.hasNext())
                return null;

            current = (Character) p.next();
        } while ((double) current.getXTile() != x || (double) current.getYTile() != y);

        return current;
    }

    public Bomber getBomber() {
        Iterator<Character> ptr = characters.iterator();

        Character current;
        while (ptr.hasNext()) {
            current = ptr.next();

            if (current instanceof Bomber) {
                return (Bomber) current;
            }
        }

        return null;
    }

    public Character getCharacterAtExcluding(int x, int y, Character c) {
        Iterator<Character> ptr = characters.iterator();

        Character current;
        while (ptr.hasNext()) {
            current = ptr.next();

            if (current == c) {
                continue;
            }

            if (current.getXTile() == x && current.getYTile() == y) {
                return current;
            }
        }
        return null;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public FlameSegment getFlameSegmentAt(int x, int y) {
        Iterator<Bomb> bombPos = bombs.iterator();
        Bomb b;
        while (bombPos.hasNext()) {
            b = bombPos.next();

            FlameSegment e = b.flameAt(x, y);
            if (e != null)
                return e;
        }

        return null;
    }

    public uet.oop.bomberman.Entities.Entity getEntityAt(double x, double y) {
        return entities[(int) x + (int) y * levelLoader.getWidth()];
    }

    public void addEntity(int pos, Entity e) {
        entities[pos] = e;
    }

    public void addCharacter(Character c) {
        characters.add(c);
    }

    public void addBomb(Bomb b) {
        bombs.add(b);
    }

    public void addMessage(Message e) {
        messages.add(e);
    }

    protected void renderCharacter(Screen screen) {
        Iterator<Character> ptr = characters.iterator();

        while (ptr.hasNext()) {
            ptr.next().render(screen);
        }
    }

    protected void renderBombs(Screen screen) {
        Iterator<Bomb> ptr = bombs.iterator();

        while (ptr.hasNext()) {
            ptr.next().render(screen);
        }
    }

    public void renderMessages(Graphics g) {
        Message e;
        for (int i = 0; i < messages.size(); i++) {
            e = messages.get(i);

            g.setFont(new Font("Arial", Font.PLAIN, e.getSize()));
            g.setColor(e.getColor());
            g.drawString(e.getMessage(), (int)e.getX() - Screen.xOffset * Game.SCALE, (int) e.getY());
        }
    }

    protected void updateEntities() {
        if (game.isPaused()) return;

        for (int i = 0; i < entities.length; i++) {
            entities[i].update();
        }
    }

    protected void updateCharacters() {
        if (game.isPaused()) return;

        Iterator<Character> ptr = characters.iterator();

        while (ptr.hasNext() && !game.isPaused())
            ptr.next().update();
    }

    protected void updateBombs() {
        if (game.isPaused()) return;

        Iterator<Bomb> ptr = bombs.iterator();
        while (ptr.hasNext())
            ptr.next().update();
    }

    protected void updateMessages() {
        if (game.isPaused()) return;
        Message e;
        int left;
        for (int i = 0; i < messages.size(); i++) {
            e = messages.get(i);
            left = e.getDuration();

            if (left > 0)
                e.setDuration(left--);
            else messages.remove(i);
        }
    }

    public int subtractTime() {
        if (game.isPaused())
            return this.time;
        else return this.time--;
    }

    public GameAudioPlayer getMusicPlayer() {
        return audioPlayer;
    }

    public void setMusicPlayer(GameAudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public KeyboardInputs getKeyboardInput() {
        return keyboardInputs;
    }

    public LevelLoader getLevel() {
        return levelLoader;
    }

    public int getLives() {
        return lives;
    }

    public void addLives(int add) {
        this.lives += add;
    }

    public Game getGame() {
        return game;
    }

    public int getShow() {
        return screenShowing;
    }

    public void setShow(int screen) {
        this.screenShowing = screen;
    }

    public int getTime() {
        return time;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int add) {
        this.points += add;
    }

    public int getWidth() {
        return levelLoader.getWidth();
    }

    public int getHeight() {
        return levelLoader.getHeight();
    }
}
