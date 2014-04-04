package com.game.zenpen;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Game implements ApplicationListener {
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	SpriteBatch batch;
	Penis penis;
	Vector3 touchPos;
	Array<Condom> condoms;
	Array<Sperm> sperms;
	long lastDropTime;
	private TextureAtlas condomsTextureAtlas;
	TextureAtlas spermTextureAtlas;

	@Override
	public void create() {
		condomsTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/condoms/condoms.atlas"));
		spermTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/sperm/sperm.atlas"));

		dropSound = Gdx.audio.newSound(Gdx.files.internal("beep.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("benny.mp3"));

		rainMusic.setLooping(true);
		rainMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		batch = new SpriteBatch();

		penis = new Penis(this);
		penis.box.width = 64;
		penis.box.height = 64;
		penis.box.x = 800 / 2 - penis.box.width / 2;
		penis.box.y = 20;

		condoms = new Array<Condom>();
		sperms = new Array<Sperm>();
		spawnRaindrop();
	}

	@Override
	public void dispose() {
		penis.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
		condomsTextureAtlas.dispose();
		spermTextureAtlas.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(penis.texture, penis.box.x, penis.box.y);
		for (Condom condom : condoms) {
			batch.draw(condom.sprite, condom.box.x, condom.box.y);
		}
		for (Sperm sperm : sperms) {
			batch.draw(sperm.sprite, sperm.box.x, sperm.box.y);
		}
		batch.end();

		switch (Gdx.app.getType()) {
		case Android:
			boolean accelerometerAvailable = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
//			boolean clickInside = penis.box.contains(Gdx.input.getX(),(400 - Gdx.input.getY()) * -1);
			if (accelerometerAvailable) {
				float adjustedX = ( Gdx.input.getAccelerometerX() - 2f );
				if( adjustedX < - 2f ) adjustedX = - 2f; else if( adjustedX > 2f ) adjustedX = 2f;

				// y: -2 (left), 0 (still), 2 (right)
				float adjustedY = Gdx.input.getAccelerometerY();
				if( adjustedY < - 2f ) adjustedY = - 2f; else if( adjustedY > 2f ) adjustedY = 2f;

				// since 2 is 100% of movement speed, let's calculate the final speed percentage
				adjustedX /= 2;
				adjustedY /= 2;

				// notice the inverted axis because the game is displayed in landscape mode
				penis.box.x += ( adjustedY * penis.speedX * Gdx.graphics.getDeltaTime() );


				//penis.box.x = Gdx.input.getAccelerometerX() * penis.speedX - penis.box.width / 2;
			} else {
				if (Gdx.input.isTouched()) {
					touchPos = new Vector3();
					touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
					camera.unproject(touchPos);
					penis.box.x = touchPos.x - penis.box.width / 2;
				}
			}
			if (Gdx.input.justTouched()/* && clickInside */) {
				penis.shoot();
			}
			break;
		default:
		case Desktop:
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				penis.box.x -= penis.speed * Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				penis.box.x += penis.speed * Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Keys.SPACE))
				penis.shoot();
			break;

		}

		if (penis.box.x < 0)
			penis.box.x = 0;
		if (penis.box.x > 800 - penis.box.width)
			penis.box.x = 800 - penis.box.width;

		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();

		Iterator<Condom> iter = condoms.iterator();
		while (iter.hasNext()) {
			Condom condom = iter.next();
			condom.box.y -= condom.speed * Gdx.graphics.getDeltaTime();
			if (condom.box.y + condom.box.height < 0) {
				iter.remove();
				continue;
			}
			Iterator<Sperm> spermIter = sperms.iterator();
			while (spermIter.hasNext()) {
				Sperm sperm = spermIter.next();
				if (sperm.box.overlaps(condom.box)) {
					dropSound.play();
					if (Gdx.input.isPeripheralAvailable(Peripheral.Vibrator)) {
						Gdx.input.vibrate(60);
					}
					spermIter.remove();
					iter.remove();
					continue;
				}
			}
			if (condom.box.overlaps(penis.box)) {
				dropSound.play();
				if (Gdx.input.isPeripheralAvailable(Peripheral.Vibrator)) {
					Gdx.input.vibrate(60);
				}
				iter.remove();
			}

		}

		Iterator<Sperm> spermIter = sperms.iterator();
		while (spermIter.hasNext()) {
			Sperm sperm = spermIter.next();
			sperm.box.y += sperm.speed * Gdx.graphics.getDeltaTime();
			if (sperm.box.y + sperm.box.height > 800) {
				spermIter.remove();
				continue;
			}
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	private void spawnRaindrop() {
		Condom condom = new Condom();
		condom.box.width = 64;
		condom.box.height = 64;
		condom.box.x = MathUtils.random(0, 800 - condom.box.width);
		condom.box.y = 480;
		condom.speed = (MathUtils.random(0, 20) >= 15) ? condom.speed * 2
				: condom.speed;
		int index = MathUtils.random(1,
				condomsTextureAtlas.getRegions().size - 1);
		condom.sprite
				.setRegion(condomsTextureAtlas.findRegion("condom", index));
		condoms.add(condom);

		lastDropTime = TimeUtils.nanoTime();
	}
}
