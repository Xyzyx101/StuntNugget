package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
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
	    initFilters(scene);
	    initTransform(scene, x, y, world);
	    body = scene.getBodies().get(0);
	    body.setTransform(x, y, 0f);
	    
	    scene.clear();
	}

	private void initTransform(RubeScene scene, float x, float y, World world2) {
		Array<Body> bodies = scene.getBodies();
		for(int i = 0; i < bodies.size; ++i) {
			Body body = bodies.get(i);
			Vector2 pos = body.getTransform().getPosition();
			pos.x += x;
			pos.y += y;
			float angle = body.getAngle();
			body.setTransform(pos, angle);
		}
	}

	private void initFilters(RubeScene scene) {
		Filter mainFilter = new Filter();
		mainFilter.categoryBits = GameScreen.PLAYER_BODY_LAYER;
		mainFilter.maskBits = GameScreen.GROUND_LAYER;
		
		Filter cosmeticFilter = new Filter();
		cosmeticFilter.categoryBits = GameScreen.PLAYER_COSMETICS;
		cosmeticFilter.maskBits = GameScreen.NO_COLLISION;
		
		Array<Fixture> fixtures = scene.getFixtures();
		for(int i = 0; i < fixtures.size; ++i) {
			Fixture fixture = fixtures.get(i);
			String type = (String) scene.getCustom(fixture, "type");
			if(type.equals("main")) {
				fixtures.get(i).setFilterData(mainFilter);
			} else if(type.equals("cosmetic")) {
				fixtures.get(i).setFilterData(cosmeticFilter);
			}
		}
	}
	
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void update() {
		// TODO Auto-generated method stub
		Vector2 pos = body.getTransform().getPosition();
		Gdx.app.log("Player", "x:" + pos.x + " y:" + pos.y);
	}

	public void draw(SpriteBatch spriteBatch) {
		// TODO Auto-generated method stub
	}
}