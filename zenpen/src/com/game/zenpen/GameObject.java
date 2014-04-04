package com.game.zenpen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject {
	private Rectangle bounds;
	private Game game;
	private int speed = 200;

	public GameObject(Game game, float x, float y, float width, float height) {
		setGame(game);
		setBounds(new Rectangle(x, y, width, height));
	}

	public abstract void update();
	public abstract void render(SpriteBatch spriteBatch);
	public abstract void dispose();

	public Rectangle getBounds() {
		return bounds;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
}