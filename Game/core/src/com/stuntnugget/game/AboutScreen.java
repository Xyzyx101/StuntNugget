package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class AboutScreen extends UIScreen {
	UIButton[] uiButtons;

	public AboutScreen(StuntNugget game) {
		super(game);
		touchPoint = new Vector3();

		float buttonWidth = 233f;
		float buttonHeight = 88f;
		uiButtons = new UIButton[1];
		uiButtons[0] = new UIButton("Back", 10f, 10f, buttonWidth,
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
				game.setScreen(new MainMenuScreen(game));
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
