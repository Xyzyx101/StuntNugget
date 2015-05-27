package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class Controller {
	TextureAtlas atlas;
	TextureRegion pointerRegion;
	TextureRegion markerRegion;
	Vector2 position;
	float angle = 0f;
	float power = 0f;
	boolean goingUp = true;
	Vector2 origin;
	Vector2 size;
	Vector2 markerSize;
	static final Vector2 tmp = new Vector2();
	boolean active = true;
	Vector2 markerStart;
	Vector2 markerEnd;
	float markTime;
	float markTimer;
	Interpolation interpolation;
	Vector2 markerOffset;

	Controller(Vector2 position) {
		TextureAtlas atlas = new TextureAtlas("gameobjects.txt");
		pointerRegion = atlas.findRegion("Pointer");
		markerRegion = atlas.findRegion("Marker");
		this.position = position;
		size = new Vector2(500f, 175f);
		origin = new Vector2(size.x * 0.18f, size.y * 0.5f);
		markerSize = new Vector2(110f, 215);

		markerStart = new Vector2(size.x * 0.35f, size.y * 0.5f);
		markerEnd = new Vector2(size.x * 0.75f, size.y * 0.5f);
		markTime = 1.2f;
		markTimer = 0;
		//interpolation = new Interpolation.PowIn(3);
		interpolation = new Interpolation.SwingIn(1);
		
		markerOffset = markerStart;
	}

	public void update(float dt) {
		if (goingUp) {
			markTimer += dt;
			if (markTimer > markTime) {
				goingUp = false;
			}
		} else {
			markTimer -= dt;
			if (markTimer < 0f) {
				goingUp = true;
			}
		}
		float value = interpolation.apply(markTimer / markTime);

		markerOffset = markerStart.cpy().lerp(markerEnd, value);
		Gdx.app.log("Controller", "" + markerOffset);
	}

	public void draw(SpriteBatch batch) {
		Vector2 pointerPos = position.cpy().scl(StuntNugget.PPM).sub(origin);
		
		batch.draw(pointerRegion, pointerPos.x,
				pointerPos.y, origin.x, origin.y,
				size.x, size.y, 1f, 1f, angle);
	
		Vector2 tmp = new Vector2(-markerSize.x * 0.5f,-markerSize.y * 0.5f).add(markerOffset).rotate(angle).add(pointerPos);
		batch.draw(markerRegion, tmp.x, tmp.y, markerSize.x * 0.5f, markerSize.y * 0.5f, markerSize.x, markerSize.y, 1f,
				1f, angle);
				
	}

	public void setTouchPoint(float xTouch, float yTouch) {
		Vector2 relative = new Vector2(xTouch, yTouch).sub(position);
		angle = relative.angle();

		if (angle > 135f && angle < 225f) {
			angle = 135f;
		} else if (angle > 225 && angle < 340f) {
			angle = 340f;
		}
	}

	public void fire() {
		active = false;
	}

	public float getPower() {
		return power;
	}

	public float getAngle() {
		return angle;
	}
}