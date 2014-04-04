package com.game.zenpen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Sperm extends SpritedGameObject {

	public Sperm(Game game, float x, float y, float width, float height) {
		super(game, x, y, width, height);
		setCurrentSpriteIndex(1);
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
}
