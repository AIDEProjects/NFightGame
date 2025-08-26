package com.goldsprite.nfightgame.examples;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.utils.math.Vector2;
import com.goldsprite.utils.math.Vector2Int;

public class RoleRendererExamples extends GScreen {
	Vector2Int footOffset = new Vector2Int(119, 36);
	private Color clearColor = Color.valueOf("#333333FF");
	private Texture roleTex;
	private TextureRegion region_idle1;
	private Vector2Int texSplit;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private float roleScl = 2f;//角色缩放
	private Vector2 pos = new Vector2(200, 250);
	private boolean faceX = true, faceY = false;

	@Override
	public void create() {
		roleTex = new Texture(Gdx.files.internal("hero/hero_sheet.png"));
		texSplit = new Vector2Int(256, 256);
		region_idle1 = new TextureRegion(roleTex, 0, 0, texSplit.x, texSplit.y);

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.7f, 0.7f, 0.7f, 1f);

		batch.setProjectionMatrix(getCamera().combined);
		batch.begin();
		drawOnFoot(batch, region_idle1, pos.x, pos.y);
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
		shapeRenderer.circle(pos.x, pos.y, 5 * roleScl);
		shapeRenderer.end();

		boolean flipX, flipY;
		if(faceX){
			flipX = region_idle1.isFlipX()?true:false;
		}else
			flipX = region_idle1.isFlipX()?false:true;
		if(faceY){
			flipY = region_idle1.isFlipY()?false:true;
		}else
			flipY = region_idle1.isFlipY()?true:false;
		region_idle1.flip(flipX, flipY);
	}

	Vector2 flipFootOffset = new Vector2(), roleBound = new Vector2();
	public void drawOnFoot(SpriteBatch batch, TextureRegion region, float x, float y) {
		roleBound.set(region.getRegionWidth(), region.getRegionHeight());
		flipFootOffset.set(region_idle1.isFlipX()?roleBound.x-footOffset.x:footOffset.x, region_idle1.isFlipY()?roleBound.y-footOffset.y:footOffset.y).scl(roleScl);
		float pivotX = x - flipFootOffset.x;
		float pivotY = y - flipFootOffset.y;
		float roleWidth = roleBound.x * roleScl;
		float roleHeight = roleBound.y * roleScl;
		batch.draw(region, pivotX, pivotY, roleWidth, roleHeight);
	}
}
