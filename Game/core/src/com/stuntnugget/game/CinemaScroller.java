package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CinemaScroller {
	Vector2 cinemaPosition;
	boolean isDone;
	int target;
	float targetTimer;
	float targetTime;
	Array<Vector2> targets;

	public CinemaScroller(Array<Star> stars, Vector2 playerPosition) {
		targets = new Array<Vector2>();
		targets.add(stars.get(2).getPosition());
		targets.add(stars.get(1).getPosition());
		targets.add(stars.get(0).getPosition());
		targets.add(playerPosition);
		targetTime = 0.75f;
		targetTimer = targetTime;
		target = 0;
		isDone = false;
	}

	public void update(float dt) {
		if (isDone) {
			return;
		}
		targetTimer -= dt;
		cinemaPosition = targets.get(target);
		Vector2 targetPosition = targets.get(target + 1);
		float alpha = 1 - (targetTimer / targetTime);
		cinemaPosition.lerp(targetPosition, alpha);
		if (targetTimer <= 0) {
			++target;
			targetTimer = targetTime;
			if (target == 3) {
				isDone = true;
			}
		}
	}

	public Vector2 getCameraPosition() {
		return cinemaPosition;
	}

	public boolean isDone() {
		return isDone;
	}

}
