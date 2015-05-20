package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.rube.RubeScene;
import com.badlogic.gdx.rube.reader.RubeSceneReader;

public class GameScreen extends ScreenAdapter {
	private StuntNugget game;
	private GameCamera camera;
	private GL20 gl;
	private SpriteBatch spriteBatch;
	private Box2DDebugRenderer debugRenderer;
	// Reader to load a scene from a file
	private RubeSceneReader reader;
	// Your scene
	private RubeScene scene;
	private String sceneFileName;

	Vector3 touchPoint;

	// FIXME
	Texture img;

	public GameScreen(StuntNugget game) {
		this.game = game;
		camera = game.getCamera();
		gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		spriteBatch = new SpriteBatch();
		debugRenderer = new Box2DDebugRenderer();

		// FIXME
		img = new Texture("badlogic.jpg");

		reader = new RubeSceneReader();
		// 2. Read your scene
		sceneFileName = "rube/floor.json";
		scene = reader.readScene(Gdx.files.internal(sceneFileName));
		touchPoint = new Vector3();
	}

	@Override
	public void dispose() {
		scene.world.dispose();
		scene.clear();
		debugRenderer.dispose();
		spriteBatch.dispose();
	}

	public void update() {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			Gdx.app.debug("GameScreen", "Tap Coords: x:" + touchPoint.x + " y:"
					+ touchPoint.y);
		}
		scene.world.step(1.0f / scene.stepsPerSecond, scene.velocityIterations,
				scene.positionIterations);
		camera.position.set(5f, 5f, 0);
		camera.update();
	}

	public void draw() {

		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Matrix4 scaledMat = new Matrix4(camera.combined);
		Matrix4.mul(scaledMat.val, game.spriteToBox2DMatrix);
		spriteBatch.setProjectionMatrix(scaledMat);
		spriteBatch.begin();
		spriteBatch.draw(img, 2f * game.PPM, 2f * game.PPM);
		spriteBatch.end();

		debugRenderer.render(scene.world, camera.combined);

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
		update();
		draw();
	}

	@Override
	public void pause() {
		// Settings.save();
	}
}
