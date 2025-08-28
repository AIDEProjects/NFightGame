package com.goldsprite.nfightgame.examples;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.goldsprite.gdxcore.screens.GScreen;
import com.badlogic.gdx.*;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.AnimatorComponent;
import com.goldsprite.nfightgame.core.ecs.component.CircleColliderComponent;
import com.goldsprite.nfightgame.core.ecs.component.SpriteComponent;
import com.goldsprite.nfightgame.core.ecs.system.renderer.Gizmos;
import com.goldsprite.nfightgame.core.ecs.system.renderer.SpriteRenderer;
import com.goldsprite.utils.math.Vector2Int;

public class RoleRendererExamples extends GScreen {
	private ShapeRenderer shapeRenderer;
	private SpriteRenderer spriteRenderer;
	private Gizmos debugRenderer;
	private Texture roleTex;
	private GObject role;
	private GObject role2;

	@Override
	public void create() {
		getImp().addProcessor(new InputAdapter(){
			int index;
			public boolean touchDown(int x, int y, int p, int b){
				float viewWidth = Gdx.graphics.getWidth();
				role2.transform.setFace(x>viewWidth/2?1:-1, 1);
				String[] animNames = {"idle", "run", "attack"};
				role2.getComponent(AnimatorComponent.class).setCurAnim(animNames[++index%animNames.length]);
				return false;
			}
		});

		shapeRenderer = new ShapeRenderer();

		spriteRenderer = new SpriteRenderer();

		debugRenderer = Gizmos.getInstance();

		roleTex = new Texture(Gdx.files.internal("hero/hero_sheet.png"));
		TextureRegion idleRegion = new TextureRegion(roleTex, 0, 0, 256, 256);


		role = new GObject();
		role.transform.setFace(-1, 1);
		role.transform.setPosition(100, 250);
		role.transform.setScale(1.5f);
		SpriteComponent texComp = role.addComponent(new SpriteComponent());
		texComp.setRegion(idleRegion);
		texComp.setOriginOffset(119, 36);
		spriteRenderer.addGObject(role);


		role2 = new GObject();
		role2.transform.setFace(1, 1);
		role2.transform.setPosition(300, 250);
		role2.transform.setScale(1.6f);

		SpriteComponent texComp2 = role2.addComponent(new SpriteComponent());
		texComp2.setOriginOffset(119, 36);

		AnimatorComponent animComp = role2.addComponent(new AnimatorComponent(texComp2));
		TextureRegion[] frames1 = splitFrames("hero/hero_sheet.png", 0, 3);
		TextureRegion[] frames2 = splitFrames("hero/hero_sheet.png", 1, 4);
		TextureRegion[] frames3 = splitFrames("hero/hero_sheet.png", 2, 2);
		Animation<TextureRegion> anim1 = new Animation<>(.25f, frames1);
		anim1.setPlayMode(Animation.PlayMode.LOOP);
		Animation<TextureRegion> anim2 = new Animation<>(.2f, frames2);
		anim2.setPlayMode(Animation.PlayMode.LOOP);
		Animation<TextureRegion> anim3 = new Animation<>(.2f, frames3);
		anim3.setPlayMode(Animation.PlayMode.NORMAL);
		animComp.addAnim("idle", anim1);
		animComp.addAnim("run", anim2);
		animComp.addAnim("attack", anim3);

		CircleColliderComponent collComp = role2.addComponent(new CircleColliderComponent());
		collComp.setOffsetPosition(53, 60);
		collComp.setRadius(24);

		spriteRenderer.addGObject(role2);
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

		role.fixedUpdate(delta);
		role2.fixedUpdate(delta);
//		textureRenderer.render(delta);

		shapeRenderer.setProjectionMatrix(getCamera().combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//红色地板
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(50, 50, 500, 200);
		shapeRenderer.end();

		//调试线绘制器
//		debugRenderer.render(delta);
	}

}

