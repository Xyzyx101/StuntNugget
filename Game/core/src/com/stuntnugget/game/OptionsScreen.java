package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class OptionsScreen extends UIScreen {
	UIButton[] uiButtons;
	TextureRegion noRegion;

	public OptionsScreen(StuntNugget game) {
		super(game);
		touchPoint = new Vector3();

		float buttonWidth = 466f;
		float buttonHeight = 176f;
		float centerX = (StuntNugget.SCREEN_WIDTH - buttonWidth) * 0.5f;
		uiButtons = new UIButton[3];
		uiButtons[0] = new UIButton("Back", 10f, 10f, 233f, 88f);
		uiButtons[1] = new UIButton("Music", centerX, 570f, buttonWidth,
				buttonHeight);
		uiButtons[2] = new UIButton("SFX", centerX, 315f, buttonWidth,
				buttonHeight);
		TextureAtlas atlas = new TextureAtlas("buttons.txt");
		noRegion = atlas.findRegion("No");
	}

	@Override
	public void update() {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			Gdx.app.debug("Options", "Tap Coords: x:" + touchPoint.x + " y:"
					+ touchPoint.y);
			if (uiButtons[0].contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(new MainMenuScreen(game));
			} else if (uiButtons[1].contains(touchPoint.x, touchPoint.y)) {
				toggleMusic();
			} else if (uiButtons[2].contains(touchPoint.x, touchPoint.y)) {
				toggleSFX();
			}
		}
	}

	@Override
	public void draw() {
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		Matrix4 scaledMat = new Matrix4(camera.combined);
		Matrix4.mul(scaledMat.val, StuntNugget.spriteToBox2DMatrix);
		camera.update();

		guiBatch.setProjectionMatrix(scaledMat);
		guiBatch.setProjectionMatrix(camera.combined);
		guiBatch.begin();
		for (int i = 0; i < uiButtons.length; ++i) {
			uiButtons[i].draw(guiBatch);
		}
		float noHeight = 220f * StuntNugget.MPP;
		float noWidth = 220f;
		float centerX = (StuntNugget.SCREEN_WIDTH - noWidth) * 0.5f
				* StuntNugget.MPP;
		noWidth *= StuntNugget.MPP;
		if (!Settings.instance().isMusicEnabled()) {
			guiBatch.draw(noRegion, centerX, 555f * StuntNugget.MPP, noWidth,
					noHeight);
		}
		if (!Settings.instance().isSfxEnabled()) {
			guiBatch.draw(noRegion, centerX, 300f * StuntNugget.MPP, noWidth,
					noHeight);
		}
		guiBatch.end();
	}

	private void toggleMusic() {
		boolean temp = Settings.instance().isMusicEnabled();
		Settings.instance().setMusicEnabled(!temp);
	}

	private void toggleSFX() {
		boolean temp = Settings.instance().isSfxEnabled();
		Settings.instance().setSfxEnabled(!temp);
	}
}
