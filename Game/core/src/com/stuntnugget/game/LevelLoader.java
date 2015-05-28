package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.files.FileHandleStream;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneLoader;

public class LevelLoader {
	GameScreen gameScreen;
	World world;
	Player player;
	Json json;
	Level level;
	RubeScene scene;
	Array<Star> stars;

	LevelLoader(int levelNumber, GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		loadRubeScene(levelNumber);
		loadStars();
		player = new Player(5f, 2f, world);
		
	}

	private void loadRubeScene(int levelNumber) {
		String filename = "levels/level" + String.valueOf(levelNumber);
		try {
			FileHandle file = Gdx.files.internal(filename);
			String jsonValue = file.readString();
			json = new Json();
			level = json.fromJson(Level.class, jsonValue);
			Gdx.app.log("LevelLoader", "Level Loaded: " + filename);
		} catch (Exception e) {
			Gdx.app.error("LevelLoader", "Error loading \"" + filename + "\"",
					e);
		}
		RubeSceneLoader loader = new RubeSceneLoader();
		RubeScene scene = loader.loadScene(Gdx.files.internal("rube/" + level.rubeFile));
		Filter filter = new Filter();
		filter.categoryBits = GameScreen.GROUND_LAYER;
		filter.maskBits = GameScreen.PLAYER_BODY_LAYER;
		Array<Fixture> fixtures = scene.getFixtures();
		for (int i = 0; i < fixtures.size; ++i) {
			fixtures.get(i).setFilterData(filter);
		}
		world = scene.getWorld();
		scene.clear();
	}
	
	private void loadStars() {
		stars = new Array<Star>();
		for (int i = 0; i < level.stars.length; ++i) {
			Vector2 starDef = level.stars[i];
			stars.add(new Star(starDef.x, starDef.y, world, gameScreen));
		}
	}
	
	public Player getPlayer() {
		return player;
	}

	public World getWorld() {
		return world;
	}

	public Array<Star> getStars() {
		return stars;
	}
}
