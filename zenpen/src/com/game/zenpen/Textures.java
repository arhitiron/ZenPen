package com.game.zenpen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Textures {
	private Game game;
	private TextureAtlas condomsAtlas;
	private TextureAtlas spermsAtlas;

	public Textures(Game game) {
		setGame(game);
		setCondomsAtlas(new TextureAtlas(Gdx.files.internal("data/condoms/condoms.atlas")));
		setSpermsAtlas(new TextureAtlas(Gdx.files.internal("data/sperm/sperm.atlas")));
		getGame().setBatch(new SpriteBatch());
	}

	public void dispose() {
		getCondomsAtlas().dispose();
		getSpermsAtlas().dispose();
		getGame().getBatch().dispose();
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public TextureAtlas getCondomsAtlas() {
		return condomsAtlas;
	}

	public void setCondomsAtlas(TextureAtlas condomsAtlas) {
		this.condomsAtlas = condomsAtlas;
	}

	public TextureAtlas getSpermsAtlas() {
		return spermsAtlas;
	}

	public void setSpermsAtlas(TextureAtlas spermsAtlas) {
		this.spermsAtlas = spermsAtlas;
	}
}
