package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class LevelSelectScreen extends UIScreen {
	UIButton[] uiButtons;
	LevelButton[] levelButtons;

	public LevelSelectScreen(StuntNugget game) {
		super(game);
		touchPoint = new Vector3();

		float buttonWidth = 233f;
		float buttonHeight = 88f;
		uiButtons = new UIButton[1];
		uiButtons[0] = new UIButton("Back", 10f, 10f, buttonWidth, buttonHeight);
		int numLevels = Settings.levels;
		levelButtons = new LevelButton[numLevels];
		float levelButtonWidth = 250f;
		float levelButtonHeight = 250f;
		float levelButtonPadding = 75f;
		float levelButtonGap = 50f;
		for (int i = 0; i < numLevels; ++i) {
			int col = i % 3;
			int row = i / 3;
			levelButtons[i] = new LevelButton(i, levelButtonPadding + col
					* (levelButtonWidth + levelButtonGap),
					StuntNugget.SCREEN_HEIGHT - levelButtonPadding
							- levelButtonHeight - row
							* (levelButtonHeight + levelButtonGap),
					levelButtonWidth, levelButtonHeight);
		}
	}

	@Override
	public void update() {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			if (uiButtons[0].contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(new MainMenuScreen(game));
			}
			for(int i = 0; i < levelButtons.length; ++i) {
				if(levelButtons[i].contains(touchPoint.x, touchPoint.y)) {
					if(Settings.instance().getLevelStatus(i) == -1) {
						return;
					}
					SoundManager.stopMusic();
					SoundManager.play(SoundManager.SFX.DING);
					game.setScreen(new GameScreen(game, i));
				}
			}
		}
	}

	@Override
	public void draw() {
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Matrix4 scaledMat = new Matrix4(camera.combined);
		Matrix4.mul(scaledMat.val, StuntNugget.spriteToBox2DMatrix);
		camera.position.set(StuntNugget.SCREEN_HEIGHT * 0.5f * StuntNugget.MPP, StuntNugget.SCREEN_HEIGHT * 0.5f  * StuntNugget.MPP, 0f);
		camera.update();
		guiBatch.setProjectionMatrix(scaledMat);
		guiBatch.setProjectionMatrix(camera.combined);
		guiBatch.begin();
		guiBatch.draw(uiBackground, 0f, 0f, 10f, 10f);
		for (int i = 0; i < uiButtons.length; ++i) {
			uiButtons[i].draw(guiBatch);
		}
		for (int i = 0; i < levelButtons.length; ++i) {
			levelButtons[i].draw(guiBatch);
		}
		guiBatch.end();
	}
}
