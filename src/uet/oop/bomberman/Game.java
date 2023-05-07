package uet.oop.bomberman;

import uet.oop.bomberman.GUI.Frame;
import uet.oop.bomberman.Graphic.Screen;
import uet.oop.bomberman.Inputs.KeyboardInputs;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Create game loop
 * store Game global variables
 * update game state by render(), update()
 */

public class Game extends Canvas {
    public static final int TILES_SIZE = 16;
    public static final int WIDTH = TILES_SIZE * (31 / 2);
    public static final  int HEIGHT = 13 * TILES_SIZE;

    public static int SCALE = 3;

    public static final String TITLE = "BOMBERMAN";

    public static final int BOMB_RATE = 1;
    private static final int BOMB_RADIUS = 1;
    private static final float BOMBER_SPEED = 1.0f;

    public static final int TIME = 200;
    public static final int POINTS = 0;
    public static final int LIVES = 999;

    protected static int SCREEN_DELAY = 3;

    // For subClass:
    protected static int bomb_Rate = BOMB_RATE;
    protected static int bomb_Radius = BOMB_RADIUS;
    protected static double bomber_Speed = BOMBER_SPEED;
    protected int screenDelay = SCREEN_DELAY;

    private KeyboardInputs keyboardInputs;
    private boolean running = false;
    private boolean paused = true;

    private Board board;
    private Screen screen;
    private uet.oop.bomberman.GUI.Frame frame;

    private BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();

    public Game(Frame frame) {
        this.frame = frame;
        frame.setTitle(TITLE);

        this.screen = new Screen(WIDTH, HEIGHT);
        this.keyboardInputs = new KeyboardInputs();

        this.board = new Board(this, keyboardInputs, screen);
        addKeyListener(keyboardInputs);
    }

    private void renderGame() {
        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();
        board.render(screen);

        for (int i = 0; i < pixels.length; i++)
            this.pixels[i] = screen.pixels[i];

        Graphics g = bs.getDrawGraphics();

        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);

        board.renderMessages(g);

        g.dispose();
        bs.show();
    }

    private void renderScreen() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();

        Graphics g = bs.getDrawGraphics();

        board.drawScreen(g);

        g.dispose();
        bs.show();
    }

    private void update() {
        keyboardInputs.update();
        board.update();
    }

    public void start() {
        running = true;

        // Calc fps, ups
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double nanoSec = 1000000000.0 / 60.0;

        double delta = 0;
        int frames = 0;
        int updates = 0;

        requestFocus();

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nanoSec;
            lastTime = now;
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }

            if (paused) {
                if (screenDelay <= 0) {
                    board.setShow(-1);
                    paused = false;
                }

                renderScreen();
            } else
                renderGame();   // continue Game

            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                frame.setTime(board.subtractTime());
                frame.setPoints(board.getPoints());
                frame.setLives(board.getLives());

                timer += 1000;

                frame.setTitle(TITLE + "|UPS: " + updates + " |FPS:" + frames);
                updates = 0;
                frames = 0;

                if (board.getShow() == 2)
                    screenDelay--;
            }
        }
    }

    public static double getBomberSpeed() {
        return BOMBER_SPEED;
    }

    public static int getBombRadius() {
        return BOMB_RADIUS;
    }

    public static int getBombRate() {
        return BOMB_RATE;
    }

    public static void buffBomberSpeed(double addSpeed) {
        bomber_Speed += addSpeed;
    }

    public static void addBombRadius(int addRad) {
        bomb_Radius += addRad;
    }

    public static void addBombRate(int addRate) {
        bomb_Rate += addRate;
    }

    public void resetScreenDelay() {
        this.screenDelay = SCREEN_DELAY;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isPaused() {
        return paused;
    }

    public void pauseGame() {
        this.paused = true;
    }
}
