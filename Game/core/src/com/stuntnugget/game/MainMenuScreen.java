package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen extends ScreenAdapter {
	StuntNugget game;
	GameCamera camera;
	GL20 gl; 
	SpriteBatch guiBatch;
	
	Rectangle soundBounds;
	Rectangle playBounds;
	Rectangle highscoresBounds;
	Rectangle helpBounds;
	Vector3 touchPoint;
	
	//FIXME
	Texture img;
	
	public MainMenuScreen (StuntNugget game) {
		this.game = game;
		camera = game.getCamera();
		this.guiBatch = game.guiBatch;
		gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		
		soundBounds = new Rectangle(0, 0, 64, 64);
		playBounds = new Rectangle(160 - 150, 200 + 18, 300, 36);
		highscoresBounds = new Rectangle(160 - 150, 200 - 18, 300, 36);
		helpBounds = new Rectangle(160 - 150, 200 - 18 - 36, 300, 36);
		touchPoint = new Vector3();
		
		//FIXME
		img = new Texture("badlogic.jpg");
	}
	
	public void update () {
		
//		if (Gdx.input.justTouched()) {
//			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
//
//			if (playBounds.contains(touchPoint.x, touchPoint.y)) {
//				Assets.playSound(Assets.clickSound);
//				game.setScreen(new GameScreen(game));
//				return;
//			}
//			if (highscoresBounds.contains(touchPoint.x, touchPoint.y)) {
//				Assets.playSound(Assets.clickSound);
//				game.setScreen(new HighscoresScreen(game));
//				return;
//			}
//			if (helpBounds.contains(touchPoint.x, touchPoint.y)) {
//				Assets.playSound(Assets.clickSound);
//				game.setScreen(new HelpScreen(game));
//				return;
//			}
//			if (soundBounds.contains(touchPoint.x, touchPoint.y)) {
//				Assets.playSound(Assets.clickSound);
//				Settings.soundEnabled = !Settings.soundEnabled;
//				if (Settings.soundEnabled)
//					Assets.music.play();
//				else
//					Assets.music.pause();
//			}
//		}
		
	}
	
	public void draw () {
		
		
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.position.set(500f, 500f, 0);
		camera.update();
		
		guiBatch.setProjectionMatrix(camera.combined);
		guiBatch.begin();
		guiBatch.draw(img, 400f, 400f);
		guiBatch.end();
		
//		game.batcher.setProjectionMatrix(guiCam.combined);
//
//		game.batcher.disableBlending();
//		game.batcher.begin();
//		game.batcher.draw(Assets.backgroundRegion, 0, 0, 320, 480);
//		game.batcher.end();
//
//		game.batcher.enableBlending();
//		game.batcher.begin();
//		game.batcher.draw(Assets.logo, 160 - 274 / 2, 480 - 10 - 142, 274, 142);
//		game.batcher.draw(Assets.mainMenu, 10, 200 - 110 / 2, 300, 110);
//		game.batcher.draw(Settings.soundEnabled ? Assets.soundOn : Assets.soundOff, 0, 0, 64, 64);
//		game.batcher.end();	
	}

	@Override
	public void render (float delta) {
		update();
		draw();
	}

	@Override
	public void pause () {
//		Settings.save();
	}
}
