package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneLoader;

public class Player {
	final String rubefile = "rube/chicken.json";
	World world;
	float xPos;
	float yPos;
	Body body;

	public Player(float x, float y, World world) {
		this.world = world;
		RubeSceneLoader loader = new RubeSceneLoader(world);
	    RubeScene scene = loader.loadScene(Gdx.files.internal("rube/chicken.json"));
	    body = scene.getBodies().get(0);
	    body.setTransform(x, y, 0f);
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	public void draw(SpriteBatch spriteBatch) {
		// TODO Auto-generated method stub

	}
}