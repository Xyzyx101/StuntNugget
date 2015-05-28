package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StuntNugget extends Game {
	//used for a screen rendering
	public static final float SCREEN_WIDTH = 1000;
	public static final float SCREEN_HEIGHT = 1000;
	public static final float PPM = 100; // Pixels per meter to convert screen coords to Box2D
	public static final float MPP = 1f / PPM; // Meters per pixel
	public static final float[] spriteToBox2DMatrix = { MPP, 0f, 0f, 0f, 
														0f, MPP, 0f, 0f,
														0f, 0f, 1f, 0f,
														0f, 0f, 0f, 1f};
	public SpriteBatch guiBatch;
	
	private GameCamera camera;
	
	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		guiBatch = new SpriteBatch();
		camera = new GameCamera();
		
		Settings.instance().load();
		SoundManager.instance().load();
		
		//TODO
		//Assets.load();
		
		setScreen(new MainMenuScreen(this));
	}
	
	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void resize(int width, int height) {
		Gdx.app.debug("StuntNegget", "Screen Resized: Width:" + width + " Height:" + height);
		float aspectRatio = (float) width / (float) height;
		camera.resize(aspectRatio);
		camera.zoom = MPP;
		camera.update();
	}
	
	@Override
	public void pause() {
		Settings.instance().save();
		super.pause();
	}
	
	@Override
	public void resume() {
		Settings.instance().load();
		super.resume();
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
	public GameCamera getCamera() {
		return camera;
	}
}
