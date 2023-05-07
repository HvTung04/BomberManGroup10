package uet.oop.bomberman.Level;


import uet.oop.bomberman.Board;
import uet.oop.bomberman.Entities.Character.Bomber;
import uet.oop.bomberman.Entities.Character.Enemy.Balloon;
import uet.oop.bomberman.Entities.Character.Enemy.Oneal;
import uet.oop.bomberman.Entities.LayeredEntity;
import uet.oop.bomberman.Entities.Tile.Destroyable.Brick;
import uet.oop.bomberman.Entities.Tile.Grass;
import uet.oop.bomberman.Entities.Tile.Item.BombItem;
import uet.oop.bomberman.Entities.Tile.Item.FlameItem;
import uet.oop.bomberman.Entities.Tile.Item.SpeedItem;
import uet.oop.bomberman.Entities.Tile.Portal;
import uet.oop.bomberman.Entities.Tile.Wall;
import uet.oop.bomberman.Exceptions.LoadLevelException;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.Graphic.Screen;
import uet.oop.bomberman.Graphic.Sprite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class FileLevelLoader extends LevelLoader {

    /**
     * Matrix contains map
     */
    private static char[][] map;

    public FileLevelLoader(Board board, int level) throws LoadLevelException {
        super(board, level);
    }

    @Override
    public void loadLevel(int level) throws LoadLevelException {
        BufferedReader br = null;
        URL Path = FileLevelLoader.class.getResource("/levels/Level" + level + ".txt");
        try {
            br = new BufferedReader(new InputStreamReader(Path.openStream()));
            String[] firstLine = br.readLine().split(" ");
            level = Integer.parseInt(firstLine[0]);
            height = Integer.parseInt(firstLine[1]);
            width = Integer.parseInt(firstLine[2]);

            map = new char[height][width];
            String line;
            for (int i = 0; i < height; i ++) {
                line = br.readLine();
                for (int j = 0; j < width; j ++) map[i][j] = line.charAt(j);
            }
        } catch (IOException e) {
            throw new LoadLevelException("Error loading level " + level, e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void createEntities() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                addEnties(map[y][x], x, y );
            }
        }
    }

    public void addEnties(char c, int x, int y) {
        int pos = x + y * getWidth();

        switch(c) {
            //Wall
            case '#':
                board.addEntity(pos, new Wall(x, y, Sprite.wall));
                break;
            //Item
            case 'b':
                board.addEntity(pos,
                        new LayeredEntity(x, y,
                                new Grass(x ,y, Sprite.grass),
                                new BombItem(x, y, Sprite.powerup_bombs),
                                new Brick(x, y, Sprite.brick)
                        )
                );
                break;
            case 's':
                board.addEntity(pos,
                        new LayeredEntity(x, y,
                                new Grass(x ,y, Sprite.grass),
                                new SpeedItem(x, y, Sprite.powerup_speed),
                                new Brick(x, y, Sprite.brick)
                        )
                );
                break;
            case 'f':
                board.addEntity(pos,
                        new LayeredEntity(x, y,
                                new Grass(x ,y, Sprite.grass),
                                new FlameItem(x, y, Sprite.powerup_flames),
                                new Brick(x, y, Sprite.brick)
                        )
                );
                break;
            //Brick
            case '*':
                board.addEntity(pos, new LayeredEntity(x, y,
                        new Grass(x ,y, Sprite.grass),
                        new Brick(x ,y, Sprite.brick)) );
                break;
            // thêm Portal
            case 'x':
                board.addEntity(pos, new LayeredEntity(x, y,
                        new Grass(x ,y, Sprite.grass),
                        new Portal(x ,y, board, Sprite.portal),
                        new Brick(x ,y, Sprite.brick)) );
                break;
            //Grass
            case ' ':
                board.addEntity(pos, new Grass(x, y, Sprite.grass) );
                break;
            //Bomber
            case 'p':
                board.addCharacter( new Bomber(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board) );
                Screen.setOffset(0, 0);
                board.addEntity(pos, new Grass(x, y, Sprite.grass) );
                break;
            //Enemy
            case '1':
                board.addCharacter( new Balloon(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
                board.addEntity(pos, new Grass(x, y, Sprite.grass) );
                break;
            case '2':
                board.addCharacter( new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
                board.addEntity(pos, new Grass(x, y, Sprite.grass) );
                break;
            default:
                board.addEntity(pos, new Grass(x, y, Sprite.grass) );
                break;
        }
    }
}
