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
	public static final short NO_COLLISION = 0x0;
	public static final short GROUND_LAYER = 0x1;
	public static final short PLAYER_BODY_LAYER = 0x2;
	public static final short PLAYER_COSMETICS = 0x4;
	public static final short STAR_TRIGGER = 0x8;
	public static final short STAND_PROP = 0x10;
	public static final short PROP_WEIGHT = 0x20;

	private enum State {
		Cinema, Start, Control, Shoot, End
	}

	private StuntNugget game;
	private int level;
	private State state = State.Cinema;
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
	private LevelRenderer levelRenderer;

	private boolean dirtyStars = false;

	private long cluckId;

	Vector3 touchPoint;

	private int score = 0;
	private Vector2 levelSize;
	private float endTimer;
	private float threeStarEndTime = 5.0f;
	private float normalEndTime = 3.0f;
	
	private CinemaScroller cinemaScroller;

	public GameScreen(StuntNugget game, int level) {
		this.game = game;
		this.level = level;
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
		levelRenderer = levelLoader.getLevelRenderer();
		cinemaScroller = new CinemaScroller(stars, playerPosition);
		SoundManager.startMusic(SoundManager.MUSIC.WINNER_WINNER);
		cluckId = SoundManager.play(SoundManager.SFX.CLUCKING);
		player.changeState(Player.State.Stand);
	}

	@Override
	public void dispose() {
		debugRenderer.dispose();
		spriteBatch.dispose();
		player.dispose();
		levelRenderer.dispose();
	}

	public void update(float dt) {
		if (dirtyStars) {
			removeStars();
		}
		switch (state) {
		case Cinema:
			cinemaScroller.update(dt);
			if (cinemaScroller.isDone()) {
				state = State.Start;
			}
			break;
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
				player.changeState(Player.State.Flop);
				player.fire(controller.getPower(), controller.getAngle());
				state = State.Shoot;
			}
			break;
		case Shoot:
			float velocitySquared = player.getVelocity().x
					* player.getVelocity().x + player.getVelocity().y
					* player.getVelocity().y;
			
			// End level criteria
			if (player.getVelocity().x < 1.0f ||
					player.getPosition().x < 0 ||
					player.getPosition().x > levelSize.x ||
					player.getPosition().y < 0) {
				state = State.End;
				if(score == 3) {
					endTimer = threeStarEndTime;
					player.changeState(Player.State.Cheer);
					SoundManager.play(SoundManager.SFX.FANFARE);
				} else {
					endTimer = normalEndTime;
				}
			}
			break;
		case End:
			endTimer -= dt;
			if(endTimer <= 0) {
				Settings.instance().setScore(level, score);
				SoundManager.stopMusic();
				game.setScreen(new LevelSelectScreen(game));
			}
			break;
		}

		accumulator += dt;
		while (accumulator >= secondsPerStep) {
			world.step(secondsPerStep, velocityIter, positionIter);
			accumulator -= secondsPerStep;
		}
		player.update();
		if (state == State.Cinema) {
			camera.setPositionWithLevelBounds(cinemaScroller.getCameraPosition(), levelSize);
		} else {
			camera.setPositionWithLevelBounds(player.getPosition(), levelSize);
		}
		camera.update();
	}

	public void draw() {
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Matrix4 scaledMat = new Matrix4(camera.combined);
		Matrix4.mul(scaledMat.val, StuntNugget.spriteToBox2DMatrix);
		spriteBatch.setProjectionMatrix(scaledMat);
		levelRenderer.draw(scaledMat);
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
			} else {
				tempStars.add(stars.get(i));
			}
		}
		stars = tempStars;
	}
}
