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
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;

public class RoleRendererExamples extends GScreen {
	private Color clearColor = Color.valueOf("#333333FF");
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private TexRole role;
	private AnimRole role2;
	private AnimatorRole role3;

	@Override
	public void create() {
		getImp().addProcessor(new InputAdapter(){
			int index;
			public boolean touchDown(int x, int y, int p, int b){
				role2.face.x = role2.face.x>0?-1:1;
				String[] animNames = {"idle", "run", "attack"};
				role3.setCurAnim(animNames[++index%animNames.length]);
				return false;
			}
		});
		
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		role = new TexRole();
		role.initTex();
		role.pos.set(100, 250);
		role.roleScl = 1.5f;

		role2 = new AnimRole();
		role2.pos.set(240, 250);
		role2.roleScl = 1.8f;
		role2.face.x = -1;
		TextureRegion[] frames = splitFrames("hero/hero_sheet.png", 2, 2);
		role2.setFrames(.2f, frames, true);
		
		role3 = new AnimatorRole();
		role3.pos.set(450, 250);
		role3.roleScl = 1.2f;
		role2.face.x = 1;
		TextureRegion[] frames1 = splitFrames("hero/hero_sheet.png", 0, 3);
		TextureRegion[] frames2 = splitFrames("hero/hero_sheet.png", 1, 4);
		TextureRegion[] frames3 = splitFrames("hero/hero_sheet.png", 2, 2);
		Animation anim1 = new Animation(.25f, frames1);
		anim1.setPlayMode(Animation.PlayMode.LOOP);
		Animation anim2 = new Animation(.2f, frames2);
		anim2.setPlayMode(Animation.PlayMode.LOOP);
		Animation anim3 = new Animation(.2f, frames3);
		anim3.setPlayMode(Animation.PlayMode.NORMAL);
		role3.addAnim("idle", anim1);
		role3.addAnim("run", anim2);
		role3.addAnim("attack", anim3);
		
	}
	
	public TextureRegion[] splitFrames(String path, int col, int count){
		Texture hero_sheet = new Texture(Gdx.files.internal(path));
		Vector2Int cell = new Vector2Int(256, 256);
		TextureRegion[] frames = new TextureRegion[count];
		for (int i = 0; i < count; i++) {
			frames[i] = new TextureRegion(hero_sheet, i*cell.x, col*cell.y, cell.x, cell.y);
		}
		return frames;
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
		role2.step(delta);
		
		role3.act(delta);
		role3.draw(batch);
		role3.step(delta);

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

		//绘制攻击区域
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.YELLOW);
		shapeRenderer.circle(role2.getAtkPos().x, role2.getAtkPos().y, role2.atkRange*role2.roleScl);
		shapeRenderer.end();
	}

}

