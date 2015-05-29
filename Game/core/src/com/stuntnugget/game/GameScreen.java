package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneLoader;

public class GameScreen extends ScreenAdapter {
	public static short NO_COLLISION = 0x0000;
	public static short GROUND_LAYER = 0x0001;
	public static short PLAYER_BODY_LAYER = 0x0010;
	public static short PLAYER_COSMETICS = 0x0100;
	public static short STAR_TRIGGER = 0x1000;

	private enum State {
		Start, Control, Shoot, End
	}

	private State state = State.Start;
	private GameCamera camera;
	private GL20 gl;
	private SpriteBatch spriteBatch;
	private Controller controller;

	private Box2DDebugRenderer debugRenderer;
	float accumulator;
	float secondsPerStep = 1f / 60f;
	int velocityIter = 8;
	int positionIter = 3;

	private World world;
	private Player player;
	private Array<Star> stars;

	private boolean dirtyStars = false;

	private long cluckId;

	Vector3 touchPoint;

	private int score = 0;
	private Vector2 levelSize;
	private float endTimer;
	private float threeStarEndTime;
	private float normalEndTime;
	

	public GameScreen(StuntNugget game, int level) {
		camera = game.getCamera();
		gl = Gdx.gl;
		gl.glClearColor(0.01567f, 0.60784f, 0.94118f, 1);
		spriteBatch = new SpriteBatch();
		touchPoint = new Vector3();

		debugRenderer = new Box2DDebugRenderer();

		LevelLoader levelLoader = new LevelLoader(0, this);
		player = levelLoader.getPlayer();
		Vector2 playerPosition = player.getPosition();
		controller = new Controller(playerPosition);
		world = levelLoader.getWorld();
		stars = levelLoader.getStars();
		world.setContactListener(new CollisionListener());
		levelSize = levelLoader.getLevelSize();
		SoundManager.startMusic(SoundManager.MUSIC.WINNER_WINNER);
		cluckId = SoundManager.play(SoundManager.SFX.CLUCKING);
	}

	@Override
	public void dispose() {
		debugRenderer.dispose();
		spriteBatch.dispose();
		player.dispose();
	}

	public void update(float dt) {
		if (dirtyStars) {
			removeStars();
		}
		switch (state) {
		case Start:
			if (Gdx.input.justTouched()) {
				state = State.Control;
			}
			break;
		case Control:
			controller.update(dt);
			if (Gdx.input.isTouched(0)) {
				camera.unproject(touchPoint.set(Gdx.input.getX(),
						Gdx.input.getY(), 0));
				controller.setTouchPoint(touchPoint.x, touchPoint.y);
			} else {
				SoundManager.stop(SoundManager.SFX.CLUCKING, cluckId);
				controller.fire();
				player.fire(controller.getPower(), controller.getAngle());
				state = State.Shoot;
			}
			break;
		case Shoot:
			float velocitySquared = player.getVelocity().x * player.getVelocity().x + player.getVelocity().y * player.getVelocity().y;
			Gdx.app.log("GameScreen",  "" + velocitySquared );
			if(velocitySquared < 1f) {
				state = State.End;
			}
			break;
		case End:
			Gdx.app.log("GameScreen", "End");
			break;
		}

		accumulator += dt;
		while (accumulator >= secondsPerStep) {
			world.step(secondsPerStep, velocityIter, positionIter);
			accumulator -= secondsPerStep;
		}
		player.update();
		camera.setPositionWithLevelBounds(player.getPosition(), levelSize);
		camera.update();
	}

	public void draw() {
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Matrix4 scaledMat = new Matrix4(camera.combined);
		Matrix4.mul(scaledMat.val, StuntNugget.spriteToBox2DMatrix);
		spriteBatch.setProjectionMatrix(scaledMat);
		spriteBatch.begin();
		for (int i = 0; i < stars.size; ++i) {
			stars.get(i).draw(spriteBatch);
		}
		if (state == State.Start || state == State.Control) {
			controller.draw(spriteBatch);
		}
		player.draw(spriteBatch);
		spriteBatch.end();
		debugRenderer.render(world, camera.combined);
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}

	public void checkDirtyStars() {
		dirtyStars = true;
	}

	private void removeStars() {
		dirtyStars = false;
		Array<Star> tempStars = new Array<Star>();
		for (int i = 0; i < stars.size; ++i) {
			Star thisStar = stars.get(i);
			if (thisStar.isDead()) {
				score += 1;
				thisStar.kill();
				Gdx.app.log("GameScreen", "Score:" + score);
			} else {
				tempStars.add(stars.get(i));
			}
		}
		stars = tempStars;
	}
}
