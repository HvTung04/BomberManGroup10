package uet.oop.bomberman.Graphic;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Entities.Character.Bomber;
import uet.oop.bomberman.Entities.Entity;
import uet.oop.bomberman.Game;

import java.awt.*;


/**
 * Entities render and screens on GamePanel
 */

public class Screen {
    protected int width, height;
    public int[] pixels;
    private int transparentColor = 0xffff00ff;

    public static int xOffset = 0, yOffset = 0;

    public Screen (int width, int height) {
        this.width = width;
        this.height = height;

        this.pixels = new int[width * height];
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = 0;
    }

    public void renderEntityBelowSprite (int xObject, int yObject, Entity entity, Sprite below) {
        xObject -= xOffset;
        yObject -= yOffset;

        for (int y = 0; y < entity.getSprite().getSize(); y++) {
            int _y = y + yObject;
            for (int x = 0; x < entity.getSprite().getSize(); x++) {
                int _x = x + xObject;
                if (_x < -entity.getSprite().getSize() || _x >= width || _y < 0 || _y >= height)
                    break; //
                if (_x < 0) _x = 0;
                int color = entity.getSprite().getPixel(x + y * entity.getSprite().getSize());
                if (color != transparentColor)
                    pixels[_x + _y * width] = color;
                else
                    pixels[_x + _y * width] = below.getPixel(x + y * below.getSize());
            }
        }
    }


    public void renderEntity (int xObject, int yObject, Entity entity) {    // Store Entity pixels
        xObject -= xOffset;
        yObject -= yOffset;

        for (int y = 0; y < entity.getSprite().getSize(); y++) {
            int _y = y + yObject;
            for (int x = 0; x < entity.getSprite().getSize(); x++) {
                int _x = x + xObject;
                if (_x < -entity.getSprite().getSize() || _x >= width || _y < 0 || _y >= height)
                    break; //
                if (_x < 0) _x = 0;
                int color = entity.getSprite().getPixel(x + y * entity.getSprite().getSize());
                if (color != transparentColor) pixels[_x + _y * width] = color;
            }
        }
    }

    public static void setOffset(int xO, int yO) {
        xOffset = xO;
        yOffset = yO;
    }

    public static int calcXOffset(Board board, Bomber bomber) {
        if (bomber == null) return 0;

        int temp = xOffset;

        double bomberX = bomber.getX() / 16; // pixels;
        double complement = 0.5;

        int firstBreakpoint = board.getWidth() / 4;
        int lastBreakpoint = board.getWidth() - firstBreakpoint;

        if (bomberX > firstBreakpoint + complement && bomberX < lastBreakpoint - complement) {
            temp = (int)bomber.getX() - (Game.WIDTH / 2);
        }

        return  temp;
    }

    public void drawEndGame(Graphics g, int points) {
        g.setColor(Color.BLACK);

        g.fillRect(0, 0, getScaleWidth(), getScaleHeight());

        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenterString("GAME OVER", getScaleWidth(), getScaleHeight(), g);

        font = new Font("Arial", Font.PLAIN, 10 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.YELLOW);
        drawCenterString("POINTS " + points, getScaleWidth(), getScaleHeight() + (Game.TILES_SIZE * 2) * Game.SCALE, g);

    }

    public void screenChangeLevel (Graphics g, int level) {
        g.setColor((Color.BLACK));
        g.fillRect(0,0, getScaleWidth(), getScaleHeight());

        Font font = new Font("Arial",Font.PLAIN,20 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.WHITE);
        drawCenterString("LEVEL " + level, getScaleWidth(), getScaleHeight(), g);
    }

    public void screenPaused(Graphics g) {
        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE);
        g.setFont(font);
        g.setColor(Color.WHITE);
        drawCenterString("PAUSED", getScaleWidth(), getScaleHeight(), g);
    }

    public void drawCenterString (String caption, int width, int height, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (width - fm.stringWidth(caption)) / 2;
        int y = (fm.getAscent() + (height - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(caption, x, y);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getScaleWidth() {
        return width * Game.SCALE;
    }

    public int getScaleHeight() {
        return height * Game.SCALE;
    }
}
