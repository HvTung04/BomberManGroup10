package uet.oop.bomberman.Graphic;

import javax.imageio.ImageIO;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Get individual sprites from img
 */

public class SpriteSheet {
    private String path;
    public final int SIZE;
    public int[] pixels;
    public static SpriteSheet tiles = new SpriteSheet("/textures/classic.png", 256);

    public SpriteSheet(String path, int size) {
        this.path = path;
        this.SIZE = size;
        this.pixels = new int[SIZE * SIZE];
        load();
    }

    private void load() {
        try {
            URL url = SpriteSheet.class.getResource(path);
            BufferedImage img = ImageIO.read(url);

            int width = img.getWidth();
            int height = img.getHeight();

            img.getRGB(0,0,width,height,pixels,0,width);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
