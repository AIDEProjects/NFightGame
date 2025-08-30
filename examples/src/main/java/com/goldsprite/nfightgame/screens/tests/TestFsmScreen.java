/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.screens.tests;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.goldsprite.gdxcore.screens.*;
import com.goldsprite.nfightgame.Tools;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.GameSystem;

public class TestFsmScreen extends GScreen{
	private SpriteBatch batch = new SpriteBatch();
	private Color backColor = new Color(0.3f, 0.3f, 0.3f, 1);
	private Texture backTex;
	private GObject hero;
	private GameSystem gm;

	@Override
	public void create(){
		backTex = Tools.createBackImage(backColor);

		createGM();

		createHero();
	}

	private void createGM() {
		gm = new GameSystem();
		gm.setViewport(getViewport());

		gm.getGizmosRenderer().setEnabled(false);
	}

	private void createHero() {
		hero = new GObject();

		hero.transform.setPosition(100, 250);
	}

	@Override
	public void render(float delta){
		ScreenUtils.clear(Color.GRAY);

		drawBack();
	}

	private void drawBack() {
		batch.setProjectionMatrix(getCamera().combined);
		batch.begin();
		batch.draw(backTex, 0, 0, getViewSize().x, getViewSize().y);
		batch.end();
	}

}
