package com.game.zenpen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
	private Sound catchSound;
	private Music backgroundMusic;

	public Sounds(Game game) {
		setCatchSound(Gdx.audio.newSound(Gdx.files.internal("beep.wav")));
		setBackgroundMusic(Gdx.audio.newMusic(Gdx.files.internal("benny.mp3")));

		getBackgroundMusic().setLooping(true);
		getBackgroundMusic().play();
	}

	public Sound getCatchSound() {
		return catchSound;
	}
	public void setCatchSound(Sound catchSound) {
		this.catchSound = catchSound;
	}
	public Music getBackgroundMusic() {
		return backgroundMusic;
	}
	public void setBackgroundMusic(Music backgroundMusic) {
		this.backgroundMusic = backgroundMusic;
	}

	public void dispose() {
		getCatchSound().dispose();
		getBackgroundMusic().dispose();
	}


}
