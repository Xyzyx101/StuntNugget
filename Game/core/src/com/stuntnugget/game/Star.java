package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneLoader;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;
import com.sun.xml.internal.bind.v2.util.CollisionCheckStack;

public class Star {
	final String rubefile = "rube/chicken.json";
	GameScreen gameScreen;
	World world;
	Body body;
	BodyImage bodyImage;
	boolean dead = false;

	Star(float x, float y, World world, GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		this.world = world;

		RubeSceneLoader loader = new RubeSceneLoader(world);
		RubeScene scene = loader
				.loadScene(Gdx.files.internal("rube/star.json"));

		Filter filter = new Filter();
		filter.categoryBits = GameScreen.STAR_TRIGGER;
		filter.maskBits = (short) (GameScreen.PLAYER_BODY_LAYER | GameScreen.PLAYER_COSMETICS);
		Array<Fixture> fixtures = scene.getFixtures();
		for (int i = 0; i < fixtures.size; ++i) {
			Fixture fixture = fixtures.get(i);
			fixture.setFilterData(filter);
			fixture.setUserData(this);
		}

		Array<Body> bodies = scene.getBodies();
		for (int i = 0; i < bodies.size; ++i) {
			body = bodies.get(i);
			Vector2 pos = body.getTransform().getPosition();
			pos.x += x;
			pos.y += y;
			float angle = body.getAngle();
			body.setTransform(pos, angle);
		}

		TextureAtlas atlas = new TextureAtlas("gameobjects.txt");
		Array<RubeImage> images = scene.getImages();

		if ((images != null) && (images.size > 0)) {
			RubeImage image = images.get(0);
			TextureRegion region = atlas.findRegion(image.name);
			bodyImage = new BodyImage(region, image.body, image.center,
					image.width, image.height, image.scale);
		}
		scene.clear();
	}

	public void draw(SpriteBatch spriteBatch) {
		bodyImage.draw(spriteBatch);
	}
	
	public void hit() {
		Gdx.app.log("Star", "Hit!!!");
		dead = true;
		gameScreen.checkDirtyStars();
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void kill() {
		world.destroyBody(body);
	}
}
