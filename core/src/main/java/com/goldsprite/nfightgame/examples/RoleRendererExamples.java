package com.goldsprite.nfightgame.examples;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.nfightgame.core.*;
import com.badlogic.gdx.*;

public class RoleRendererExamples extends GScreen {
	private ShapeRenderer shapeRenderer;
	private Texture roleTex;
	private GObject role;
	private TextureRenderer textureRenderer;

	@Override
	public void create() {
		getImp().addProcessor(new InputAdapter(){
			int index;
			public boolean touchDown(int x, int y, int p, int b){
				return false;
			}
		});

		shapeRenderer = new ShapeRenderer();

		roleTex = new Texture(Gdx.files.internal("hero/hero_sheet.png"));
		TextureRegion idleRegion = new TextureRegion(roleTex, 0, 0, 256, 256);
		role = new GObject();
		TextureComponent texComp = new TextureComponent();
		texComp.setRegion(idleRegion);
		texComp.setFace(-1, 1);
		texComp.setPosition(200, 250);
		texComp.setScale(1.5f);
		texComp.setOriginOffset(119, 36);
		role.addComponent(texComp);
		textureRenderer = new TextureRenderer();
		textureRenderer.setCamera(getCamera());
		textureRenderer.addGObject(role);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.7f, 0.7f, 0.7f, 1f);

		textureRenderer.draw(delta);

		shapeRenderer.setProjectionMatrix(getCamera().combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		//红色地板
		shapeRenderer.rect(50, 50, 500, 200);
		shapeRenderer.end();
	}

}

