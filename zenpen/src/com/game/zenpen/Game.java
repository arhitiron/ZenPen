package com.game.zenpen;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Game implements ApplicationListener {
	private Sounds sounds;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Penis penis;
	private Vector3 touchPos;
	private Array<Condom> condoms;
	private Array<Sperm> sperms;
	private long lastDropTime;
	private Textures textures;

	@Override
	public void create() {
		initTextures();
		initMusicAndSounds();
		initCamera();
		initCollections();
		initPenis();
		spawnRaindrop();
	}

	private void initPenis() {
		setPenis(new Penis(this, 800 / 2 - 64 / 2, 20, 64, 64));
	}

	private void initTextures() {
		setTextures(new Textures(this));
	}

	private void initCamera() {
		setCamera(new OrthographicCamera());
		getCamera().setToOrtho(false, 800, 480);
	}

	private void initMusicAndSounds() {
		setSounds(new Sounds(this));
	}

	private void initCollections() {
		setCondoms(new Array<Condom>());
		setSperms(new Array<Sperm>());
	}

	@Override
	public void dispose() {
		getPenis().dispose();
		getSounds().dispose();
		getTextures().dispose();
	}

	@Override
	public void render() {
		glClear();

		getCamera().update();
		getBatch().setProjectionMatrix(getCamera().combined);

		getBatch().begin();
		getPenis().render(getBatch());
		for (Condom condom : getCondoms()) {
			condom.render(getBatch());
		}
		for (Sperm sperm : getSperms()) {
			sperm.render(getBatch());
		}
		getBatch().end();

		getPenis().update();

		spawnRaindrop();

		updateCondoms();
		updateSperms();
	}

	private void updateSperms() {
		Iterator<Sperm> spermIter = getSperms().iterator();
		while (spermIter.hasNext()) {
			Sperm sperm = spermIter.next();
			sperm.getBounds().y += sperm.getSpeed() * Gdx.graphics.getDeltaTime();
			if (sperm.getBounds().y + sperm.getBounds().height > 800) {
				spermIter.remove();
				continue;
			}
		}
	}

	private void updateCondoms() {
		Iterator<Condom> iter = getCondoms().iterator();
		while (iter.hasNext()) {
			Condom condom = iter.next();
			condom.getBounds().y -= condom.getSpeed() * Gdx.graphics.getDeltaTime();
			if (condom.getBounds().y + condom.getBounds().height < 0) {
				iter.remove();
				continue;
			}
			Iterator<Sperm> spermIter = getSperms().iterator();
			while (spermIter.hasNext()) {
				Sperm sperm = spermIter.next();
				if (sperm.getBounds().overlaps(condom.getBounds())) {
					getSounds().getCatchSound().play();
					if (Gdx.input.isPeripheralAvailable(Peripheral.Vibrator)) {
						Gdx.input.vibrate(60);
					}
					spermIter.remove();
					iter.remove();
					continue;
				}
			}
			if (condom.getBounds().overlaps(getPenis().getBounds())) {
				getSounds().getCatchSound().play();
				if (Gdx.input.isPeripheralAvailable(Peripheral.Vibrator)) {
					Gdx.input.vibrate(60);
				}
				iter.remove();
			}

		}
	}

	private void glClear() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
		if (TimeUtils.nanoTime() - getLastDropTime() <= 1000000000) return;
		Condom condom = new Condom(this, MathUtils.random(0, 800 - 64), 480, 64, 64, false);
		condom.setSpeed((MathUtils.random(0, 20) >= 15) ? (condom.getSpeed() + condom.getSpeed()/2) : condom.getSpeed());
		int index = MathUtils.random(1,	getTextures().getCondomsAtlas().getRegions().size - 1);
		condom.getSprite().setRegion(getTextures().getCondomsAtlas().findRegion("condom", index));
		getCondoms().add(condom);

		setLastDropTime(TimeUtils.nanoTime());
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public Penis getPenis() {
		return penis;
	}

	public void setPenis(Penis penis) {
		this.penis = penis;
	}

	public Vector3 getTouchPos() {
		return touchPos;
	}

	public void setTouchPos(Vector3 touchPos) {
		this.touchPos = touchPos;
	}

	public Array<Condom> getCondoms() {
		return condoms;
	}

	public void setCondoms(Array<Condom> condoms) {
		this.condoms = condoms;
	}

	public Array<Sperm> getSperms() {
		return sperms;
	}

	public void setSperms(Array<Sperm> sperms) {
		this.sperms = sperms;
	}

	public long getLastDropTime() {
		return lastDropTime;
	}

	public void setLastDropTime(long lastDropTime) {
		this.lastDropTime = lastDropTime;
	}

	public Sounds getSounds() {
		return sounds;
	}

	public void setSounds(Sounds sounds) {
		this.sounds = sounds;
	}

	public Textures getTextures() {
		return textures;
	}

	public void setTextures(Textures textures) {
		this.textures = textures;
	}
}
