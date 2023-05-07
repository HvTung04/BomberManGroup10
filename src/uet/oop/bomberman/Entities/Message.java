package uet.oop.bomberman.Entities;

import uet.oop.bomberman.Graphic.Screen;

import java.awt.*;

/**
 * Deliver in-game messages
 */

public class Message extends Entity {
    protected String message;
    protected int duration;
    protected Color color;
    protected int size;

    public Message(String message, double x, double y, int duration, Color color, int size) {
        this.x = x;
        this.y = y;
        this.message = message;
        this.duration = duration;
        this.color = color;
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMessage() {
        return message;
    }

    public Color getColor() {
        return color;
    }

    public int getSize() {
        return size;
    }


    @Override
    public void update() {

    }

    @Override
    public void render(Screen screen) {

    }

    @Override
    public boolean collide(Entity other) {
        return true;
    }
}
