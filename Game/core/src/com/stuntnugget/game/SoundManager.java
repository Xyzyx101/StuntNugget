package com.stuntnugget.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

public class SoundManager {
	static final int levels = 5;
	private static SoundManager _instance = null;
	private static Array<Sound> sounds;
	private static Array<Music> music;
	private static Random rand;

	public static enum MUSIC {
		SUPER_CIRCUS, WINNER_WINNER
	}

	public static enum SFX {
		BCAH, BOING, CLUCKING, DING, FANFARE, FIRE, FIREWORKS, MUSIC_CLIP, ROCKET
	}

	// MUSIC
	private final static int SUPER_CIRCUS = 0;
	private final static int WINNER_WINNER = 1;

	// SFX
	private final static int BCAH1 = 0;
	private final static int BCAH2 = 1;
	private final static int BCAH3 = 2;
	private final static int BOING1 = 3;
	private final static int BOING2 = 4;
	private final static int BOING3 = 5;
	private final static int CLUCKING = 6;
	private final static int DING = 7;
	private final static int FANFARE = 8;
	private final static int FIRE = 9;
	private final static int FIREWORKS = 10;
	private final static int MUSIC_CLIP = 11;
	private final static int ROCKET = 12;

	public static SoundManager instance() {
		if (_instance == null) {
			_instance = new SoundManager();
		}
		return _instance;
	}

	private SoundManager() {
		music = new Array<Music>(2);
		sounds = new Array<Sound>(13);
		rand = new Random();
	}

	public void dispose() {
		// TODO
	}

	public void load() {
		music.add(Gdx.audio.newMusic(Gdx.files
				.internal("music/super_circus.mp3")));
		music.add(Gdx.audio.newMusic(Gdx.files
				.internal("music/winner_winner.mp3")));
		for (int i = 0; i < music.size; ++i) {
			music.get(i).setLooping(true);
		}
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/bcah1.mp3")));
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/bcah2.mp3")));
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/bcah3.mp3")));
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/boing1.mp3")));
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/boing2.mp3")));
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/boing3.mp3")));
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/clucking.mp3")));
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/ding.mp3")));
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/fanfare.mp3")));
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/fire.mp3")));
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/fireworks.mp3")));
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/musicclip.mp3")));
		sounds.add(Gdx.audio.newSound(Gdx.files.internal("sfx/rocket.mp3")));
	}

	public static long play(SFX sfx) {
		long id = 0;
		if (!Settings.instance().isSfxEnabled()) {
			return id;
		}
		int randNum;
		switch (sfx) {
		case BCAH:
			randNum = randInt(0, 2);
			Gdx.app.log("SoundManager", "" + randNum);
			if (randNum == 0) {
				id = sounds.get(BCAH1).play();
			} else if (randNum == 1) {
				id = sounds.get(BCAH2).play();
			} else {
				id = sounds.get(BCAH3).play();
			}
			break;
		case BOING:
			randNum = randInt(0, 2);
			if (randNum == 0) {
				id = sounds.get(BOING1).play();
			} else if (randNum == 1) {
				id = sounds.get(BOING2).play();
			} else {
				id = sounds.get(BOING3).play();
			}
			break;
		case CLUCKING:
			id = sounds.get(CLUCKING).play();
			break;
		case DING:
			id = sounds.get(DING).play();
			break;
		case FANFARE:
			id = sounds.get(FANFARE).play();
			break;
		case FIRE:
			id = sounds.get(FIRE).play();
			break;
		case FIREWORKS:
			id = sounds.get(FIREWORKS).play();
			break;
		case MUSIC_CLIP:
			id = sounds.get(MUSIC_CLIP).play();
			break;
		case ROCKET:
			id = sounds.get(ROCKET).play();
			break;
		}
		return id;
	}

	public static void stop(SFX sfx, long id) {
		switch (sfx) {
		case BCAH:
			sounds.get(BCAH1).stop(id);
			sounds.get(BCAH2).stop(id);
			sounds.get(BCAH3).stop(id);
			break;
		case BOING:
			sounds.get(BOING1).stop(id);
			sounds.get(BOING2).stop(id);
			sounds.get(BOING3).stop(id);
			break;
		case CLUCKING:
			sounds.get(CLUCKING).stop(id);
			break;
		case DING:
			sounds.get(DING).stop(id);
			break;
		case FANFARE:
			sounds.get(FANFARE).stop(id);
			break;
		case FIRE:
			sounds.get(FIRE).stop(id);
			break;
		case FIREWORKS:
			sounds.get(FIREWORKS).stop(id);
			break;
		case MUSIC_CLIP:
			sounds.get(MUSIC_CLIP).stop(id);
			break;
		case ROCKET:
			sounds.get(ROCKET).stop(id);
		}
	}

	public static void startMusic(MUSIC id) {
		if (!Settings.instance().isMusicEnabled()) {
			return;
		}
		switch (id) {
		case WINNER_WINNER:
			music.get(WINNER_WINNER).play();
			break;
		case SUPER_CIRCUS:
			music.get(SUPER_CIRCUS).play();
			break;
		}
	}

	public static void stopMusic() {
		music.get(WINNER_WINNER).stop();
		music.get(SUPER_CIRCUS).stop();
	}

	public static void pauseMusic() {
		music.get(WINNER_WINNER).pause();
		music.get(SUPER_CIRCUS).pause();
	}

	public static int randInt(int min, int max) {
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
}
