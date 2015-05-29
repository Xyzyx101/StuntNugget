package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.utils.Array;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneLoader;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;
import com.stuntnugget.game.Poser.POSE;

public class Player {
	final String rubefile = "rube/chicken.json";
	World world;
	Body primaryBody;
	Array<BodyImage> bodyImages;
	Vector2 maxPower;

	private Body standPropBody;
	private Fixture standPropFixture;
	private Fixture propWeightFixture;

	private Poser poser;

	public enum State {
		Flop, Fly, Stand, Cheer
	}

	private State currentState;

	public Player(float x, float y, World world) {
		this.world = world;
		maxPower = new Vector2(50f, 0f);
		RubeSceneLoader loader = new RubeSceneLoader(world);
		RubeScene scene = loader.loadScene(Gdx.files
				.internal("rube/chicken.json"));
		initFilters(scene);
		initTransform(scene, x, y);
		initImages(scene);
		initJoints(scene);

		poser = new Poser(scene.getJoints());

		changeState(State.Flop);

		scene.clear();
	}

	private void initJoints(RubeScene scene) {
		Array<Joint> joints = scene.getJoints();
		for (int i = 0; i < joints.size; ++i) {
			String jointName = (String) scene.getCustom(joints.get(i),
					"jointName");
			joints.get(i).setUserData(jointName);
		}

	}

	private void initTransform(RubeScene scene, float x, float y) {
		Array<Body> bodies = scene.getBodies();
		for (int i = 0; i < bodies.size; ++i) {
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
		mainFilter.maskBits = (short) (GameScreen.GROUND_LAYER | GameScreen.STAR_TRIGGER);

		Filter cosmeticFilter = new Filter();
		cosmeticFilter.categoryBits = GameScreen.PLAYER_COSMETICS;
		cosmeticFilter.maskBits = GameScreen.STAR_TRIGGER;

		Filter standPropFilter = new Filter();
		standPropFilter.categoryBits = GameScreen.STAND_PROP;
		standPropFilter.maskBits = GameScreen.GROUND_LAYER;

		Filter propWeightFilter = new Filter();
		propWeightFilter.categoryBits = (short) GameScreen.PROP_WEIGHT;
		propWeightFilter.maskBits = GameScreen.NO_COLLISION;

		Array<Fixture> fixtures = scene.getFixtures();
		for (int i = 0; i < fixtures.size; ++i) {
			Fixture fixture = fixtures.get(i);
			String type = (String) scene.getCustom(fixture, "type");
			if (type.equals("main")) {
				fixtures.get(i).setFilterData(mainFilter);
			} else if (type.equals("cosmetic")) {
				fixtures.get(i).setFilterData(cosmeticFilter);
			} else if (type.equals("standProp")) {
				fixtures.get(i).setFilterData(standPropFilter);
				standPropFixture = fixtures.get(i);
			} else if (type.equals("propWeight")) {
				fixtures.get(i).setFilterData(propWeightFilter);
				propWeightFixture = fixtures.get(i);
			} else {
				Gdx.app.log("Player", "Unknown fixture type");
			}
		}
		Array<Body> bodies = scene.getBodies();
		for (int i = 0; i < bodies.size; ++i) {
			Body thisBody = bodies.get(i);
			String bodyType = (String) scene.getCustom(thisBody, "bodyType",
					"ignore");
			if (bodyType.equals("main")) {
				primaryBody = thisBody;
			} else if (bodyType.equals("standProp")) {
				standPropBody = thisBody;
			}
		}
		Array<Fixture> primaryFixtures = primaryBody.getFixtureList();
		for (int i = 0; i < primaryFixtures.size; ++i) {
			primaryFixtures.get(i).setUserData(this);
		}
	}

	private void initImages(RubeScene scene) {
		TextureAtlas atlas = new TextureAtlas((String) scene.getCustom(world,
				"atlas"));
		Array<RubeImage> images = scene.getImages();

		if ((images != null) && (images.size > 0)) {
			bodyImages = new Array<BodyImage>();
			for (int i = 0; i < images.size; ++i) {
				RubeImage image = images.get(i);
				TextureRegion region = atlas.findRegion(image.name);
				BodyImage bodyImage = new BodyImage(region, image.body,
						image.center, image.width, image.height, image.scale);
				// BodyImage bodyImage = new BodyImage(region, image.body,
				// image.center, region.getRegionWidth(),
				// region.getRegionHeight());

				bodyImages.add(bodyImage);
			}
		}
	}

	public void dispose() {
		// TODO Auto-generated method stub
	}

	public void changeState(State newState) {
		if(currentState == newState) {
			return;
		}
		currentState = newState;
		switch (newState) {
		case Flop:
			setStandMode(false);
			poser.changePose(POSE.FLOP);
			break;
		case Stand:
			setStandMode(true);
			poser.changePose(POSE.STAND);
			break;
		case Fly:
			setStandMode(false);
			break;
		case Cheer:
			poser.changePose(POSE.CHEER);
			setStandMode(true);
			break;
		}
	}

	public void update() {
		switch (currentState) {
		case Flop:
			break;
		case Stand:
			break;
		case Fly:
			break;
		case Cheer:
			break;
		}
		poser.update();
	}

	public void draw(SpriteBatch spriteBatch) {
		for (int i = 0; i < bodyImages.size; ++i) {
			bodyImages.get(i).draw(spriteBatch);
		}
	}

	public Vector2 getPosition() {
		return primaryBody.getPosition();
	}

	public void fire(float power, float angle) {
		power = Math.max(power, 0.25f);
		Gdx.app.log("Player", "fire(" + power + "," + angle + ")");
		Vector2 powerVector = maxPower.cpy().scl(power).rotate(angle);
		primaryBody.applyLinearImpulse(powerVector, primaryBody.getPosition(),
				true);
	}

	public Vector2 getVelocity() {
		return primaryBody.getLinearVelocity();
	}

	public void setStandMode(boolean stand) {
		if (stand) {
			Vector2 position = primaryBody.getPosition();
			float angle = primaryBody.getAngle();
			standPropBody.setTransform(position, angle);
			standPropBody.setActive(true);
		} else {
			standPropBody.setActive(false);
		}
		Gdx.app.log("Player", "" + standPropBody.getMass());
	}
}