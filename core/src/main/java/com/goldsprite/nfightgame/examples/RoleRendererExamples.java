package com.goldsprite.nfightgame.examples;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.nfightgame.core.TexRole;
import com.goldsprite.utils.math.Vector2Int;
import com.goldsprite.nfightgame.core.*;

public class RoleRendererExamples extends GScreen {
	private Color clearColor = Color.valueOf("#333333FF");
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private TexRole role;
	private AnimRole role2;

	@Override
	public void create() {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		role = new TexRole();
		role.initTex();
		role.pos.set(200, 250);
		role.roleScl = 1.8f;

		role2 = new AnimRole();
		role2.pos.set(400, 250);
		role2.roleScl = 1.5f;
		role2.face.x = -1;
		String path = "hero/hero_sheet.png";
		Texture hero_sheet = new Texture(Gdx.files.internal(path));
		int count = 2;
		Vector2Int cell = new Vector2Int(256, 256);
		TextureRegion[] frames = new TextureRegion[count];
		for (int i = 0; i < count; i++) {
			frames[i] = new TextureRegion(hero_sheet, i*cell.x, 2*cell.y, cell.x, cell.y);
		}
		role2.setFrames(.25f, frames, true);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.7f, 0.7f, 0.7f, 1f);

		batch.setProjectionMatrix(getCamera().combined);
		batch.begin();

		role.act(delta);
		role.draw(batch);

		role2.act(delta);
		role2.draw(batch);

		batch.end();

		shapeRenderer.setProjectionMatrix(getCamera().combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		//红色地板
		shapeRenderer.rect(50, 50, 500, 200);
		shapeRenderer.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.GREEN);
		//绿色脚底标识
		shapeRenderer.circle(role.pos.x, role.pos.y, 5 * role.roleScl);
		shapeRenderer.end();
	}

}

