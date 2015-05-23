package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneLoader;

public class GameScreen extends ScreenAdapter {
	public static short NO_COLLISION = 0x0000;
	public static short GROUND_LAYER = 0x0001;
	public static short PLAYER_BODY_LAYER = 0x0010;
	public static short PLAYER_COSMETICS = 0x0100;
		
	private GameCamera camera;
	private GL20 gl;
	private SpriteBatch spriteBatch;

	private Box2DDebugRenderer debugRenderer;
	float accumulator;
	float secondsPerStep = 1f / 60f;
	int velocityIter = 8;
	int positionIter = 3;

	// Your scene
	private RubeScene scene;	
	private String sceneFileName;
	private Player player;

	Vector3 touchPoint;

	// FIXME
	Texture img;

	public GameScreen(StuntNugget game) {
		camera = game.getCamera();
		gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		spriteBatch = new SpriteBatch();
		touchPoint = new Vector3();
		
		debugRenderer = new Box2DDebugRenderer();

		// FIXME
		img = new Texture("badlogic.jpg");

		RubeSceneLoader loader = new RubeSceneLoader();
		// 2. Read your scene
		sceneFileName = "rube/floor.json";
		scene = loader.loadScene(Gdx.files.internal(sceneFileName));
		Filter filter = new Filter();
		filter.categoryBits = GROUND_LAYER;
		filter.maskBits = PLAYER_BODY_LAYER;
		Array<Fixture> fixtures = scene.getFixtures();
		for(int i = 0; i < fixtures.size; ++i) {
			fixtures.get(i).setFilterData(filter);
		}
		player = new Player(5f, 12f, scene.getWorld());
	}

	@Override
	public void dispose() {
		scene.clear();
		debugRenderer.dispose();
		spriteBatch.dispose();
		player.dispose();
	}

	public void update(float delta) {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			Gdx.app.debug("GameScreen", "Tap Coords: x:" + touchPoint.x + " y:"
					+ touchPoint.y);
		}
		accumulator += delta;
		while (accumulator >= secondsPerStep) {
			scene.getWorld()
					.step(secondsPerStep, velocityIter, positionIter);
			accumulator -= secondsPerStep;
		}

		player.update();
		camera.position.set(5f, 5f, 0);
		camera.update();
	}

	public void draw() {
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Matrix4 scaledMat = new Matrix4(camera.combined);
		Matrix4.mul(scaledMat.val, StuntNugget.spriteToBox2DMatrix);
		spriteBatch.setProjectionMatrix(scaledMat);
		spriteBatch.begin();
		spriteBatch.draw(img, 2f * StuntNugget.PPM, 2f * StuntNugget.PPM);
		player.draw(spriteBatch);
		spriteBatch.end();

		debugRenderer.render(scene.getWorld(), camera.combined);

		// game.batcher.setProjectionMatrix(guiCam.combined);
		//
		// game.batcher.disableBlending();
		// game.batcher.begin();
		// game.batcher.draw(Assets.backgroundRegion, 0, 0, 320, 480);
		// game.batcher.end();
		//
		// game.batcher.enableBlending();
		// game.batcher.begin();
		// game.batcher.draw(Assets.logo, 160 - 274 / 2, 480 - 10 - 142, 274,
		// 142);
		// game.batcher.draw(Assets.mainMenu, 10, 200 - 110 / 2, 300, 110);
		// game.batcher.draw(Settings.soundEnabled ? Assets.soundOn :
		// Assets.soundOff, 0, 0, 64, 64);
		// game.batcher.end();
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}
}
