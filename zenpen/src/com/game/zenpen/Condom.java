package com.game.zenpen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Condom extends SpritedGameObject {

	private boolean unpacked;
	private boolean isPack;

	public Condom(Game game, float x, float y, float width, float height, boolean isPack) {
		super(game, x, y, width, height);
		setUnpacked(false);
		setPack(isPack);
	}

	@Override
	public void update() {

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
	}

	public boolean isPack() {
		return isPack;
	}

	public void setPack(boolean isPack) {
		this.isPack = isPack;
	}
}
