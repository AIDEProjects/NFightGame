package com.goldsprite.nfightgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GdxLauncher extends ApplicationAdapter {
	private SpriteBatch batch;
	private Color backgroundColor = Color.valueOf("#000000FF"), clearColor = Color.valueOf("#333333FF");


	@Override
	public void create() {
		batch = new SpriteBatch();
	}

	@Override
	public void render() {
		ScreenUtils.clear(clearColor);
	}
}

