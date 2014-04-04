package com.game.zenpen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TexturedGameObject extends GameObject {
	private Texture texture;

	public TexturedGameObject(Game game, float x, float y, float width, float height, Texture texture) {
		super(game, x, y, width, height);
		setTexture(texture);
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

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

}
