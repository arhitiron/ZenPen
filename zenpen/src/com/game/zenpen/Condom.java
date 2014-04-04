package com.game.zenpen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Condom extends SpritedGameObject {

	public Condom(Game game, float x, float y, float width, float height) {
		super(game, x, y, width, height);
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
