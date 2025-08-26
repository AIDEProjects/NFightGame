package com.goldsprite.nfightgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.goldsprite.gdxcore.screens.ScreenManager;
import com.goldsprite.nfightgame.examples.RoleRendererExamples;

public class GdxLauncher extends ApplicationAdapter {
	private ScreenManager screenManager;

	@Override
	public void create() {
		screenManager = new ScreenManager();
		screenManager.setViewport(new FitViewport(960, 540, new OrthographicCamera()));
		screenManager.setCurScreen(RoleRendererExamples.class, true);
	}

	@Override
	public void render() {
		screenManager.render();
	}

	@Override
	public void resize(int width, int height) {
		screenManager.resize(width, height);
	}
}

