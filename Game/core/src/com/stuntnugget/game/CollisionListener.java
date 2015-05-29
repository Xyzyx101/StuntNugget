package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionListener implements ContactListener {

	@SuppressWarnings("rawtypes")
	@Override
	public void endContact(Contact contact) {
		Class fixtureAClass;
		Class fixtureBClass;
		Object fixtureAObject = contact.getFixtureA().getUserData();
		if (fixtureAObject == null) {
			return;
		} else {
			fixtureAClass = fixtureAObject.getClass();
		}
		Object fixtureBObject = contact.getFixtureB().getUserData();
		if (fixtureBObject == null) {
			return;
		} else {
			fixtureBClass = fixtureBObject.getClass();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void beginContact(Contact contact) {
		Class fixtureAClass;
		Class fixtureBClass;
		Object fixtureAObject = contact.getFixtureA().getUserData();
		if (fixtureAObject == null) {
			return;
		} else {
			fixtureAClass = fixtureAObject.getClass();
		}
		Object fixtureBObject = contact.getFixtureB().getUserData();
		if (fixtureBObject == null) {
			return;
		} else {
			fixtureBClass = fixtureBObject.getClass();
		}
		if (fixtureAClass == Player.class && fixtureBClass == Star.class) {
			Star star = (Star) fixtureBObject;
			star.hit();
		} else if (fixtureAClass == Star.class && fixtureBClass == Player.class) {
			Star star = (Star) fixtureAObject;
			star.hit();
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		Class fixtureAClass;
		Class fixtureBClass;
		Object fixtureAObject = contact.getFixtureA().getUserData();
		if (fixtureAObject == null) {
			return;
		} else {
			fixtureAClass = fixtureAObject.getClass();
		}
		Object fixtureBObject = contact.getFixtureB().getUserData();
		if (fixtureBObject == null) {
			return;
		} else {
			fixtureBClass = fixtureBObject.getClass();
		}

		if ((fixtureAClass == Player.class && fixtureBClass == GameScreen.class)
				|| (fixtureAClass == GameScreen.class && fixtureBClass == Player.class)) {
			float[] impulses = impulse.getNormalImpulses();
			float maxImpulse = 0;
			for (int i = 0; i < impulses.length; ++i) {
				maxImpulse = Math.max(maxImpulse, impulses[i]);
			}
			// float pitch;
			if (maxImpulse > 3f) {
				// pitch = Math.max(Math.min(maxImpulse * 0.015f, 1f), 0);
				SoundManager.play(SoundManager.SFX.BOING);
				Gdx.app.log("CollisionListener", "" + maxImpulse);
			}
		}
	}
}
