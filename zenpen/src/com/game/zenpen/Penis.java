package com.game.zenpen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Penis {
	Rectangle box = new Rectangle();
	Texture texture = new Texture(Gdx.files.internal("penis.png"));
	int speed = 200;
	Game game;
	long lastShootTime;
	public float speedX = 800 / 1.6f;
	Sound shootSound;

	public Penis(Game game) {
		this.game = game;
		shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.mp3"));
	}

	public void dispose() {
		texture.dispose();
		shootSound.dispose();
	}

	public void shoot() {
		if (TimeUtils.nanoTime() - lastShootTime <= 1000000000) return;
		final Sperm sperm = new Sperm();
		sperm.box.width = 32;
		sperm.box.height = 32;
		sperm.box.x = box.x + box.width/2 - sperm.box.width/2;
		sperm.box.y = box.y + box.height - sperm.box.height/2;
		sperm.sprite.setRegion(game.spermTextureAtlas.findRegion("sperm", sperm.currentSpriteIndex));
		Timer.schedule(new Task(){
            @Override
            public void run() {
            	sperm.currentSpriteIndex++;
                if(sperm.currentSpriteIndex > 4)
                	sperm.currentSpriteIndex = 1;

                sperm.sprite.setRegion(game.spermTextureAtlas.findRegion("sperm", sperm.currentSpriteIndex));
            }
        }
        , 0, 1/25.0f);

		game.sperms.add(sperm);
		shootSound.play();
		lastShootTime = TimeUtils.nanoTime();
	}
}
