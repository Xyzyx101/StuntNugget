package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen extends UIScreen {
	UIButton[] uiButtons;

	public MainMenuScreen(StuntNugget game) {
		super(game);
		touchPoint = new Vector3();

		float buttonWidth = 466f;
		float buttonHeight = 176f;
		float centerX = (StuntNugget.SCREEN_WIDTH - buttonWidth) * 0.5f;
		uiButtons = new UIButton[4];
		uiButtons[0] = new UIButton("Play", centerX, 698f, buttonWidth,
				buttonHeight);
		uiButtons[1] = new UIButton("Options", centerX, 507f, buttonWidth,
				buttonHeight);
		uiButtons[2] = new UIButton("About", centerX, 316f, buttonWidth,
				buttonHeight);
		uiButtons[3] = new UIButton("Exit", centerX, 125f, buttonWidth,
				buttonHeight);
	}

	@Override
	public void update() {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			Gdx.app.debug("MainMenu", "Tap Coords: x:" + touchPoint.x + " y:"
					+ touchPoint.y);
			if (uiButtons[0].contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(new GameScreen(game));
			} else if (uiButtons[1].contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(new OptionsScreen(game));
			} else if (uiButtons[2].contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(new AboutScreen(game));
			} else if (uiButtons[3].contains(touchPoint.x, touchPoint.y)) {
				Gdx.app.error("MainMenu", "Impliment game exit");
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
		guiBatch.draw(uiBackground, 0f, 0f, 10f, 10f);
		for (int i = 0; i < uiButtons.length; ++i) {
			uiButtons[i].draw(guiBatch);
		}
		guiBatch.end();
	}
}
