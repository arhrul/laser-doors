package com.mygdx.game.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundSettings {

    private Sound doorSound;
    private Music bgMusic;
    private Sound click;

    private float soundsVolume;
    private float bgMusicVolume;

    public SoundSettings() {
        this.bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/bgMusic.mp3"));
        this.bgMusic.setLooping(true);
        this.doorSound = Gdx.audio.newSound(Gdx.files.internal("sounds/doorSound.mp3"));
        this.click = Gdx.audio.newSound(Gdx.files.internal("sounds/click.mp3"));
        this.bgMusicVolume = 0.2f;
        this.soundsVolume = 0.2f;
    }

    public void playClickSound() {
        click.play(soundsVolume);
    }

    public void playBgMusic() {
        bgMusic.setVolume(bgMusicVolume);
        bgMusic.play();
    }

    public void playDoorSound() {
        doorSound.play(soundsVolume);
    }

    public float getSoundsVolume() {
        return soundsVolume;
    }

    public void setSoundsVolume(float soundsVolume) {
        this.soundsVolume = soundsVolume;
    }

    public float getBgMusicVolume() {
        return bgMusicVolume;
    }

    public void setBgMusicVolume(float bgMusicVolume) {
        this.bgMusicVolume = bgMusicVolume;
        this.bgMusic.setVolume(bgMusicVolume);
    }
}
