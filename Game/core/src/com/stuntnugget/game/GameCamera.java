package com.stuntnugget.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameCamera extends OrthographicCamera {
	private StuntNugget game;
	
	GameCamera(StuntNugget game) {
		this.game = game;
	}

	public void resize(float aspectRatio) {
		viewportWidth = game.SCREEN_WIDTH * aspectRatio;
		viewportHeight = game.SCREEN_HEIGHT;
		new FitViewport(game.SCREEN_WIDTH, game.SCREEN_HEIGHT, this);
		update();
	}
}
