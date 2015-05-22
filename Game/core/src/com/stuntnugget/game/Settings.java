package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Settings {
	static Settings _instance = null;
	final static String filename = ".StuntNugget";
	boolean musicEnabled;
	boolean sfxEnabled;

	public static Settings instance() {
		if (_instance == null) {
			_instance = new Settings();
		}
		return _instance;
	}

	public void load() {
		try {
			FileHandle filehandle = Gdx.files.external(filename);
			String[] strings = filehandle.readString().split("\n");
			musicEnabled = Boolean.parseBoolean(strings[0]);
			sfxEnabled = Boolean.parseBoolean(strings[1]);
		} catch (Throwable e) {
			musicEnabled = true;
			sfxEnabled = true;
		}
	}

	public void save() {
		try {
			FileHandle filehandle = Gdx.files.external(filename);

			filehandle
					.writeString(Boolean.toString(musicEnabled) + "\n", false);
			filehandle.writeString(Boolean.toString(sfxEnabled) + "\n", true);

		} catch (Throwable e) {
			Gdx.app.error("Settings", "Save failed");
		}
	}

	public boolean isMusicEnabled() {
		return musicEnabled;
	}

	public void setMusicEnabled(boolean soundEnabled) {
		this.musicEnabled = soundEnabled;
	}

	public boolean isSfxEnabled() {
		return sfxEnabled;
	}

	public void setSfxEnabled(boolean sfxEnabled) {
		this.sfxEnabled = sfxEnabled;
	}
}
