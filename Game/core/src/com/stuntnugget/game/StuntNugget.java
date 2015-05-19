package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StuntNugget extends Game {
	//used for a screen rendering
	public final float SCREEN_WIDTH = 1000;
	public final float SCREEN_HEIGHT = 1000;
	
	public SpriteBatch guiBatch;
	
	private GameCamera camera;
	
	
	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		guiBatch = new SpriteBatch();
		camera = new GameCamera(this);
		
		//TODO
		//Settings.load();
		
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
	}
	
	@Override
	public void pause() {
		super.pause();
	}
	
	@Override
	public void resume() {
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
