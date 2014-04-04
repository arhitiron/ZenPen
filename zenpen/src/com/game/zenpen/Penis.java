package com.game.zenpen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Penis extends TexturedGameObject {
	private long lastShootTime;
	private float speedX = 800 / 1.6f;
	private Sound shootSound;

	public Penis(Game game, float x, float y, float width, float height) {
		super(game, x, y, width, height, new Texture(Gdx.files.internal("penis.png")));
		setShootSound(Gdx.audio.newSound(Gdx.files.internal("shoot.mp3")));
	}

	@Override
	public void dispose() {
		getTexture().dispose();
		getShootSound().dispose();
	}

	public void shoot() {
		if (TimeUtils.nanoTime() - getLastShootTime() <= 1000000000) return;
		final Sperm sperm = new Sperm(getGame(), getBounds().x + getBounds().width/2 - 32/2, getBounds().y + getBounds().height - 32/2, 32, 32);
//		sperm.box.width = 32;
//		sperm.box.height = 32;
//		sperm.box.x = getPosition().x + getBounds().width/2 - 32/2;
//		sperm.box.y = getPosition().y + getBounds().height - 32/2;
		sperm.getSprite().setRegion(getGame().getTextures().getSpermsAtlas().findRegion("sperm", sperm.getCurrentSpriteIndex()));
		Timer.schedule(new Task(){
            @Override
            public void run() {
            	sperm.setCurrentSpriteIndex(sperm.getCurrentSpriteIndex() + 1);
                if(sperm.getCurrentSpriteIndex() > 4)
                	sperm.setCurrentSpriteIndex(1);

                sperm.getSprite().setRegion(getGame().getTextures().getSpermsAtlas().findRegion("sperm", sperm.getCurrentSpriteIndex()));
            }
        }
        , 0, 1/25.0f);

		getGame().getSperms().add(sperm);
		getShootSound().play();
		setLastShootTime(TimeUtils.nanoTime());
	}

	@Override
	public void update() {
		switch (Gdx.app.getType()) {
		case Android:
			boolean accelerometerAvailable = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
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
				getBounds().x += ( adjustedY * getSpeedX() * Gdx.graphics.getDeltaTime() );

			} else {
				if (Gdx.input.isTouched()) {
					getGame().setTouchPos(new Vector3());
					getGame().getTouchPos().set(Gdx.input.getX(), Gdx.input.getY(), 0);
					getGame().getCamera().unproject(getGame().getTouchPos());
					getBounds().x = getGame().getTouchPos().x - getBounds().width / 2;
				}
			}
			if (Gdx.input.justTouched()) {
				shoot();
			}
			break;
		default:
		case Desktop:
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				getBounds().x -= getSpeed() * Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				getBounds().x += getSpeed() * Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Keys.SPACE))
				shoot();
			break;
		}

		if (getBounds().x < 0) getBounds().x = 0;
		if (getBounds().x > 800 - getBounds().width) getBounds().x = 800 - getBounds().width;
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(getTexture(), getBounds().x, getBounds().y);
	}

	public Sound getShootSound() {
		return shootSound;
	}

	public void setShootSound(Sound shootSound) {
		this.shootSound = shootSound;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public long getLastShootTime() {
		return lastShootTime;
	}

	public void setLastShootTime(long lastShootTime) {
		this.lastShootTime = lastShootTime;
	}
}
