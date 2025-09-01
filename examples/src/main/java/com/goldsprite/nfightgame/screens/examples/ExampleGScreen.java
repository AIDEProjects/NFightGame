package com.goldsprite.nfightgame.screens.examples;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.gdxcore.utils.FontUtils;
import com.goldsprite.utils.math.Vector2;

public abstract class ExampleGScreen extends GScreen {
	public abstract String getIntroduction();
	public abstract Vector2 getIntroductionPos();
	private SpriteBatch batch;
	private BitmapFont introFnt;

	public ExampleGScreen() {
		batch = new SpriteBatch();
		introFnt = FontUtils.generate(14);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		batch.setProjectionMatrix(getCamera().combined);
		batch.begin();
		introFnt.draw(batch, getIntroduction(), getIntroductionPos().x, getIntroductionPos().y);
		batch.end();
	}
}
