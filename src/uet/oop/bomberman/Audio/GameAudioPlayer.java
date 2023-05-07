package uet.oop.bomberman.Audio;

import uet.oop.bomberman.Game;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Store audio
 */

public class GameAudioPlayer implements Runnable {
    // Audio Files:
    public static final String BACKGROUND_MUSIC = "bg";
    public static final String PLACE_BOMB = "place_bomb";
    public static final String POWER_UP = "power_up";
    public static final String EXPLOSION = "explosion";
    public static final String DEAD = "dead";

    private Clip clip;

    @Override
    public void run() {
        switch (loopable) {
            case LOOP:
                this.loop();
                break;
            case NONELOOP:
                this.play();
                break;
        }
    }

    public enum Loopable {
        NONELOOP,
        LOOP;
    }

    // Set NONELOOP on default
    private Loopable loopable = Loopable.NONELOOP;

    public GameAudioPlayer(String fileName) {
        String path = "/audio/" + fileName + ".wav";

        try {
            URL defaultSound = getClass().getResource(path);
            AudioInputStream sound = AudioSystem.getAudioInputStream(defaultSound);

            // Store sound in clip
            clip = AudioSystem.getClip();
            clip.open(sound);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Malformed URL: " + e);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Input/Output Error: " + e);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Line Unavailable Exception Error: " + e);
        }
    }

    public Loopable getLoopable() {
        return loopable;
    }

    public void setLoopable(Loopable loopable) {
        this.loopable = loopable;
    }

    public void play() {
        clip.setFramePosition(0);   // rewind
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
