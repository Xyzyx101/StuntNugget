package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Settings {
	static final int levels = 5;
	
	final static String filename = ".StuntNugget";
	boolean musicEnabled;
	boolean sfxEnabled;
	int[] levelComplete;
	
	private static Settings _instance = null;
	
	public static Settings instance() {
		if (_instance == null) {
			_instance = new Settings();
		}
		return _instance;
	}
	
	private Settings() {
		levelComplete = new int[levels];
	}
	
	public void load() {
		try {
			FileHandle filehandle = Gdx.files.external(filename);
			String[] strings = filehandle.readString().split("\n");
			musicEnabled = Boolean.parseBoolean(strings[0]);
			sfxEnabled = Boolean.parseBoolean(strings[1]);
			for (int i = 0; i < levels; ++i) {
				levelComplete[i] = Integer.parseInt(strings[i+2]);
			}
		} catch (Throwable e) {
			// Defaults
			musicEnabled = true;
			sfxEnabled = true;
			for (int i = 0; i < levels; ++i) {
				levelComplete[i] = -1;
			}
		}
	}

	public void save() {
		try {
			FileHandle filehandle = Gdx.files.external(filename);

			filehandle
					.writeString(Boolean.toString(musicEnabled) + "\n", false);
			filehandle.writeString(Boolean.toString(sfxEnabled) + "\n", true);
			for (int i = 0; i < levels; ++i) {
				filehandle.writeString(Integer.toString(levelComplete[i]) + "\n", true);
			}

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
	
	// status is -1 for incomplete and 1, 2 or 3 for the number of stars received
	public void setLevelStatus(int level, int status) {
		levelComplete[level] = status;
	}
	
	public int getLevelStatus(int level) {
		return levelComplete[level];
	}
}
