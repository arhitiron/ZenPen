package com.game.zenpen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpritedGameObject extends GameObject {
	private Sprite sprite;
	private int currentSpriteIndex;

	public SpritedGameObject(Game game, float x, float y, float width, float height) {
		super(game, x, y, width, height);
		setSprite(new Sprite());
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch spriteBatch) {

	}

	@Override
	public void dispose() {

	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public int getCurrentSpriteIndex() {
		return currentSpriteIndex;
	}

	public void setCurrentSpriteIndex(int currentSpriteIndex) {
		this.currentSpriteIndex = currentSpriteIndex;
	}

}
