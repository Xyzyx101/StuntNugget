package com.stuntnugget.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameCamera extends OrthographicCamera {
	GameCamera() {}

	public void resize(float aspectRatio) {
		viewportWidth = StuntNugget.SCREEN_WIDTH * aspectRatio;
		viewportHeight = StuntNugget.SCREEN_HEIGHT;
		new FitViewport(StuntNugget.SCREEN_WIDTH, StuntNugget.SCREEN_HEIGHT, this);
		update();
	}
}
