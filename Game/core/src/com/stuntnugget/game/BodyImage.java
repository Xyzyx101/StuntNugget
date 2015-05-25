package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class BodyImage {
	private final TextureRegion region;
	private final Body body;
	private final Vector2 center;
	private final Vector2 size = new Vector2();
	private final Vector2 halfSize = new Vector2();
	private final Vector2 regionCenter = new Vector2();
	private static final Vector2 tmp = new Vector2();

	public BodyImage(TextureRegion region, Body body, Vector2 center,
			float width, float height, float rubeScale) {
		this.region = region;
		this.body = body;
		this.center = center;
		size.set(width, height);
		regionCenter.set(region.getRegionWidth() * 0.5f * StuntNugget.MPP,
				region.getRegionHeight() * 0.5f * StuntNugget.MPP);
		halfSize.set(width * 0.5f, height * 0.5f);
	}

	public void draw(SpriteBatch batch) {
		if (body != null) {
			float angle = body.getAngle() * MathUtils.radiansToDegrees;
			Vector2 pos = body.getPosition();
			Gdx.app.log("BodyImage", "bodyX:" + pos.x + " bodyY:" + pos.y);
			tmp.set(center).rotate(angle).add(body.getPosition()).sub(halfSize);
			batch.draw(region, tmp.x * StuntNugget.PPM,
					tmp.y * StuntNugget.PPM, halfSize.x * StuntNugget.PPM,
					halfSize.y * StuntNugget.PPM, size.x * StuntNugget.PPM,
					size.y * StuntNugget.PPM, 1f, 1f, angle);
		}
	}
}
