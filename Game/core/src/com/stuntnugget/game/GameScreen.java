package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends ScreenAdapter {
	StuntNugget game;
	GameCamera camera;
	GL20 gl;
	SpriteBatch spriteBatch;
	
	//FIXME
	Texture img;

	public GameScreen(StuntNugget game) {
		this.game = game;
		camera = game.getCamera();
		gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		spriteBatch = new SpriteBatch();

		// FIXME
		img = new Texture("badlogic.jpg");
	}

	public void update() {
		camera.position.set(500f, 500f, 0);
		camera.update();
	}

	public void draw() {

		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		spriteBatch.draw(img, 200f, 200f);
		spriteBatch.end();

		// game.batcher.setProjectionMatrix(guiCam.combined);
		//
		// game.batcher.disableBlending();
		// game.batcher.begin();
		// game.batcher.draw(Assets.backgroundRegion, 0, 0, 320, 480);
		// game.batcher.end();
		//
		// game.batcher.enableBlending();
		// game.batcher.begin();
		// game.batcher.draw(Assets.logo, 160 - 274 / 2, 480 - 10 - 142, 274,
		// 142);
		// game.batcher.draw(Assets.mainMenu, 10, 200 - 110 / 2, 300, 110);
		// game.batcher.draw(Settings.soundEnabled ? Assets.soundOn :
		// Assets.soundOff, 0, 0, 64, 64);
		// game.batcher.end();
	}

	@Override
	public void render(float delta) {
		update();
		draw();
	}

	@Override
	public void pause() {
		// Settings.save();
	}
}
