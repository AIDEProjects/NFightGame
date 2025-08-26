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
import com.goldsprite.nfightgame.core.TexRoleRenderer;
import com.goldsprite.utils.math.Vector2;
import com.goldsprite.utils.math.Vector2Int;

public class RoleRendererExamples extends GScreen {
	private Color clearColor = Color.valueOf("#333333FF");
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private TexRole role;
	private TexRoleRenderer roleRenderer;

	@Override
	public void create() {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		role = new TexRole();
		roleRenderer = new TexRoleRenderer(batch);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.7f, 0.7f, 0.7f, 1f);

		batch.setProjectionMatrix(getCamera().combined);
		batch.begin();
		roleRenderer.drawOnFoot(role);
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
		shapeRenderer.circle(role.pivotPos.x, role.pivotPos.y, 5 * role.roleScl);
		shapeRenderer.end();
	}

}
