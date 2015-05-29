package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameCamera extends OrthographicCamera {
	GameCamera() {}

	public void resize(float aspectRatio) {
		viewportWidth = StuntNugget.SCREEN_WIDTH * aspectRatio;
		viewportHeight = StuntNugget.SCREEN_HEIGHT;
		new FitViewport(StuntNugget.SCREEN_WIDTH, StuntNugget.SCREEN_HEIGHT, this);
		update();
	}
	
	public void setPosition(Vector2 newPosition) {
		position.set(newPosition.x, newPosition.y, 0);
	}
	
	public void setPositionWithLevelBounds(Vector2 newPosition, Vector2 levelSize) {
		Vector2 cameraPosition = new Vector2(newPosition);
		float halfWidth = StuntNugget.SCREEN_WIDTH * 0.5f * StuntNugget.MPP;
		float halfHeight = StuntNugget.SCREEN_HEIGHT * 0.5f * StuntNugget.MPP;
		cameraPosition.x = Math.max(Math.min(cameraPosition.x, levelSize.x - halfWidth), halfWidth);
		cameraPosition.y = Math.min(cameraPosition.y, levelSize.y - halfHeight);
		cameraPosition.y = Math.max(cameraPosition.y, halfHeight);
		position.set(cameraPosition.x, cameraPosition.y, 0);
	}
}
