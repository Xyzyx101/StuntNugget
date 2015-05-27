package com.stuntnugget.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class LevelButton extends UIButton {
	TextureRegion lockRegion;
	TextureRegion starRegion;
	TextureRegion darkStarRegion;
	int level;
	Vector2 lockOffset;
	Vector2[] starOffsets;
	float lockWidth;
	float lockHeight;
	float starWidth;
	float starHeight;

	public LevelButton(int level, float xPos, float yPos, float width,
			float height) {
		super("Level", xPos, yPos, width, height);
		this.level = level;
		TextureAtlas atlas = new TextureAtlas("gameobjects.txt");
		lockRegion = atlas.findRegion("Lock");
		starRegion = atlas.findRegion("Star");
		darkStarRegion = atlas.findRegion("DarkStar");
		lockWidth = width * 0.8f;
		lockHeight = height * 0.8f;
		starWidth = width * 0.5f;
		starHeight = height * 0.5f;
		lockOffset = new Vector2((width - lockWidth) * 0.5f,
				(height - lockHeight) * 0.5f).scl(StuntNugget.MPP);
		starOffsets = new Vector2[3];
		starOffsets[0] = new Vector2((width - starWidth) * 0.5f,
				(height - starHeight) * 0.5f + height * 0.2f)
				.scl(StuntNugget.MPP);
		starOffsets[1] = new Vector2((width - starWidth) * 0.5f - width * 0.2f,
				(height - starHeight) * 0.5f - height * 0.2f)
				.scl(StuntNugget.MPP);
		starOffsets[2] = new Vector2((width - starWidth) * 0.5f + width * 0.2f,
				(height - starHeight) * 0.5f - height * 0.2f)
				.scl(StuntNugget.MPP);
		lockWidth *= StuntNugget.MPP;
		lockHeight *= StuntNugget.MPP;
		starWidth *= StuntNugget.MPP;
		starHeight *= StuntNugget.MPP;
	}

	public void draw(SpriteBatch batch) {
		super.draw(batch);
		int levelComplete = Settings.instance().getLevelStatus(level);
		if (levelComplete > 0) {
			batch.draw(starRegion, rect.x + starOffsets[0].x, rect.y
					+ starOffsets[0].y, starWidth, starHeight);
		} else {
			batch.draw(darkStarRegion, rect.x + starOffsets[0].x, rect.y
					+ starOffsets[0].y, starWidth, starHeight);
		}
		if (levelComplete > 1) {
			batch.draw(starRegion, rect.x + starOffsets[1].x, rect.y
					+ starOffsets[1].y, starWidth, starHeight);
		} else {
			batch.draw(darkStarRegion, rect.x + starOffsets[1].x, rect.y
					+ starOffsets[1].y, starWidth, starHeight);
		}
		if (levelComplete > 2) {
			batch.draw(starRegion, rect.x + starOffsets[2].x, rect.y
					+ starOffsets[2].y, starWidth, starHeight);
		} else {
			batch.draw(darkStarRegion, rect.x + starOffsets[2].x, rect.y
					+ starOffsets[2].y, starWidth, starHeight);
		}
		if (levelComplete == -1) {
			batch.draw(lockRegion, rect.x + lockOffset.x,
					rect.y + lockOffset.y, lockWidth, lockHeight);
		}
	}

	public boolean contains(float x, float y) {
		return rect.contains(x, y);
	}
}
