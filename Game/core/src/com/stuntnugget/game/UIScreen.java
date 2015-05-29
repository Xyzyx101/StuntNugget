package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class UIScreen extends ScreenAdapter {
	StuntNugget game;
	GameCamera camera;
	GL20 gl;
	SpriteBatch guiBatch;
	Vector3 touchPoint;
	Texture uiBackground;
	
	public UIScreen(StuntNugget game) {
		this.game = game;
		camera = game.getCamera();
		camera.position.set(StuntNugget.SCREEN_HEIGHT * 0.5f * StuntNugget.MPP, StuntNugget.SCREEN_HEIGHT * 0.5f  * StuntNugget.MPP, 0f);
		camera.update();
		this.guiBatch = game.guiBatch;
		gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		uiBackground =  new Texture(Gdx.files.internal("background.png"));
		touchPoint = new Vector3();
	}

	abstract void update();

	abstract void draw();

	@Override
	public void render(float delta) {
		update();
		draw();
	}

	@Override
	public void pause() {
		// Settings.save();
	}
	
	@Override
	public void resume() {
		// Settings.load();
	}
}
