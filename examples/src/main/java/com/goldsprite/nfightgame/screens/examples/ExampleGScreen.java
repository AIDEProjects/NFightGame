package com.goldsprite.nfightgame.screens.examples;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.gdxcore.utils.FontUtils;
import com.goldsprite.utils.math.Vector2;

public abstract class ExampleGScreen extends GScreen {
	public abstract String getIntroduction();
	private Vector2 introPos = new Vector2();
	public Vector2 getIntroductionPos() {
		return introPos.set(20, getViewSize().y - 20);
	}
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
