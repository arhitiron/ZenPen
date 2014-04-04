package com.game.zenpen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Condom extends SpritedGameObject {

	private boolean unpacked;
	private boolean isPack;
	private boolean isAlreadyUnpacked;
	private float speedX = 800 / 1.6f;
	private boolean fly;
	private boolean isAlreadyFlied;

	public Condom(Game game, float x, float y, float width, float height, boolean isPack) {
		super(game, x, y, width, height);
		setUnpacked(false);
		setAlreadyUnpacked(false);
		setAlreadyFlied(false);
		setFly(false);
		setPack(isPack);
	}

	@Override
	public void update() {
		if (isPack() && isUnpacked()) {
			switch (Gdx.app.getType()) {
			case Android:
				boolean accelerometerAvailable = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
				if (accelerometerAvailable && !isFly()) {
					float adjustedX = ( Gdx.input.getAccelerometerX() - 2f );
					if( adjustedX < - 2f ) adjustedX = - 2f; else if( adjustedX > 2f ) adjustedX = 2f;

					// y: -2 (left), 0 (still), 2 (right)
					float adjustedY = Gdx.input.getAccelerometerY();
					if( adjustedY < - 2f ) adjustedY = - 2f; else if( adjustedY > 2f ) adjustedY = 2f;

					// since 2 is 100% of movement speed, let's calculate the final speed percentage
					adjustedX /= 2;
					adjustedY /= 2;

					// notice the inverted axis because the game is displayed in landscape mode
					getBounds().x -= ( adjustedY * getSpeedX() * Gdx.graphics.getDeltaTime() );
					simpleUpdatePosition();
				} else {
					simpleUpdatePosition();
				}
				break;
			default:
			case Desktop:
				simpleUpdatePosition();
				break;
			}
		} else {
			simpleUpdatePosition();
		}
	}

	private void simpleUpdatePosition() {
		if (isFly()) {
			getBounds().y += getSpeed() * Gdx.graphics.getDeltaTime();
		} else {
			getBounds().y -= getSpeed() * Gdx.graphics.getDeltaTime();
		}
	}

	@Override
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.draw(getSprite(), getBounds().x, getBounds().y);
	}

	@Override
	public void dispose() {

	}

	public boolean isUnpacked() {
		return unpacked;
	}

	public void setUnpacked(boolean unpacked) {
		this.unpacked = unpacked;
		if (isUnpacked()) {
			if (isAlreadyUnpacked()) return;
			setSpeed(50);
			setCurrentSpriteIndex(1);
			getSprite().setRegion(getGame().getTextures().getCondomsUnpackedAtlas().findRegion("condom_unpacked", getCurrentSpriteIndex()));
			Timer.schedule(new Task(){
	            @Override
	            public void run() {
	            	if (isFly()) {
	            		cancel();
	            	} else {
		            	setCurrentSpriteIndex(getCurrentSpriteIndex() + 1);
		                if(getCurrentSpriteIndex() > 8)
		                	setCurrentSpriteIndex(1);

		                getSprite().setRegion(getGame().getTextures().getCondomsUnpackedAtlas().findRegion("condom_unpacked", getCurrentSpriteIndex()));
	            	}
	            }
	        }
	        , 0, 1/35.0f);
			setAlreadyUnpacked(true);
		}
	}

	public boolean isPack() {
		return isPack;
	}

	public void setPack(boolean isPack) {
		this.isPack = isPack;
	}

	public boolean isAlreadyUnpacked() {
		return isAlreadyUnpacked;
	}

	private void setAlreadyUnpacked(boolean isAlreadyUnpacked) {
		this.isAlreadyUnpacked = isAlreadyUnpacked;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public boolean isFly() {
		return fly;
	}

	public void setFly(boolean fly) {
		this.fly = fly;
		if (isUnpacked() && isFly()) {
			if (isAlreadyFlied()) return;
			setSpeed(350);
			setCurrentSpriteIndex(1);
			getSprite().setRegion(getGame().getTextures().getCondomsUnpackedFullAtlas().findRegion("condom_unpacked_full", getCurrentSpriteIndex()));
			Timer.schedule(new Task(){
	            @Override
	            public void run() {
	            	setCurrentSpriteIndex(getCurrentSpriteIndex() + 1);
	                if(getCurrentSpriteIndex() > 3)
	                	setCurrentSpriteIndex(1);

	                getSprite().setRegion(getGame().getTextures().getCondomsUnpackedFullAtlas().findRegion("condom_unpacked_full", getCurrentSpriteIndex()));
	            }
	        }
	        , 0, 1/35.0f);
			setAlreadyFlied(true);
		}
	}

	public boolean isAlreadyFlied() {
		return isAlreadyFlied;
	}

	public void setAlreadyFlied(boolean isAlreadyFlied) {
		this.isAlreadyFlied = isAlreadyFlied;
	}
}
