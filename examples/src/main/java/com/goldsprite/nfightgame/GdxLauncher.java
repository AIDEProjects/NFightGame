package com.goldsprite.nfightgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.goldsprite.gdxcore.logs.Log;
import com.goldsprite.gdxcore.screens.ScreenManager;
import com.goldsprite.gdxcore.utils.FontUtils;
import com.goldsprite.infinityworld.assets.GlobalAssets;
import com.goldsprite.nfightgame.screens.StartScreen;
import com.goldsprite.nfightgame.screens.examples.ExampleSelectScreen;

public class GdxLauncher extends ApplicationAdapter {
	public static final float WORLD_WIDTH = 960;
	public static final float WORLD_HEIGHT = 540;
	public static boolean showFps = true;

	private SpriteBatch batch;
	private BitmapFont font;
	OrthographicCamera cam;

	@Override
	public void create() {
		Log.log("启动游戏.");
		//创建统一视口
		FitViewport viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
		viewport.apply(true);
		//实例化全局资源
		new GlobalAssets();
		//初始化屏幕
		ScreenManager.getInstance().
			setViewport(viewport).
			addScreen(new StartScreen()).
			addScreen(new ExampleSelectScreen()).
			setLaunchScreen(StartScreen.class);

		batch = new SpriteBatch();
		font = FontUtils.generate(35);
		font.getData().setScale(0.45f);

		cam = (OrthographicCamera) ScreenManager.getInstance().getViewport().getCamera();
	}

	@Override
	public void render() {
		ScreenManager.getInstance().render();
		if (showFps) drawFps();
	}

	private void drawFps() {
		//Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		float margin = 5;
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), margin, margin + font.getLineHeight());
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		ScreenManager.getInstance().resize(width, height);
	}

	@Override
	public void dispose() {
		ScreenManager.getInstance().dispose();
	}

}
