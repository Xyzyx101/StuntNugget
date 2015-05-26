package com.stuntnugget.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class UIButton {
	Rectangle rect;
	TextureRegion region;
	
	public UIButton(String spriteName, float xPos, float yPos, float width, float height) {
		rect = new Rectangle(
				xPos * StuntNugget.MPP
				, yPos * StuntNugget.MPP
				, width * StuntNugget.MPP
				, height * StuntNugget.MPP);
		TextureAtlas atlas = new TextureAtlas("buttons.txt");
		region = atlas.findRegion(spriteName);
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(region, rect.x, rect.y, rect.width, rect.height); 
	}
	
	public boolean contains(float x, float y) {
		return rect.contains(x, y);
	}
}
