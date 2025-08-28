package com.goldsprite.nfightgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.infinityworld.assets.GlobalAssets;
import com.goldsprite.nfightgame.core.Rocker;
import com.goldsprite.nfightgame.core.RoleControllerComponent;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.*;
import com.goldsprite.utils.math.Vector2Int;
import com.goldsprite.nfightgame.core.ecs.system.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.scenes.scene2d.*;

public class MainGameScreen extends GScreen {
	private GameSystem gm;

	private GObject hero, dummy, wall, ground;

	private Skin uiSkin;
	private Stage uiStage;
	private Rocker rocker;

	private SpriteBatch batch;
	private Texture backTex;

	@Override
	public void create() {
		gm = new GameSystem();
		gm.setCamera(getCamera());

		createBackImage();
		
		createUI();

		createGObjects();
	}

	private void createGObjects() {
		createDummy();
		
		createHero();
		

		wall = new GObject();
		wall.transform.setPosition(500, 300);

		RectColliderComponent wallCollider = wall.addComponent(new RectColliderComponent());
		wallCollider.setSize(100, 200);


		ground = new GObject();
		ground.transform.setPosition(400, 150);

		RectColliderComponent groundCollider = ground.addComponent(new RectColliderComponent());
		groundCollider.setSize(600, 40);
	}

	private void createHero() {
		hero = new GObject();

		hero.transform.setFace(1, 1);
		hero.transform.setPosition(100, 250);

		CircleColliderComponent heroFootCollider = hero.addComponent(new CircleColliderComponent());
		heroFootCollider.setOffsetPosition(0, 22.5f);
		heroFootCollider.setRadius(22.5f);

		CircleColliderComponent heroAtkTrigger = hero.addComponent(new CircleColliderComponent());
		heroAtkTrigger.setTrigger(true);
		heroAtkTrigger.setOffsetPosition(79.5f, 90);
		heroAtkTrigger.setRadius(36);

		RoleControllerComponent roleController = hero.addComponent(new RoleControllerComponent());
		roleController.setRocker(rocker);
		roleController.setTarget(hero.transform);
		roleController.setSpeed(500);
		roleController.bindAttackTrigger(heroAtkTrigger);

		RigidbodyComponent rigi = hero.addComponent(new RigidbodyComponent());
		rigi.setGravity(true);

		TextureComponent texture = hero.addComponent(new TextureComponent());
		texture.getScale().set(1.5f);
		texture.setOriginOffset(119, 36);

		AnimatorComponent animator = hero.addComponent(new AnimatorComponent(texture));
		TextureRegion[] frames1 = splitFrames("hero/hero_sheet.png", 0, 3);
		TextureRegion[] frames2 = splitFrames("hero/hero_sheet.png", 1, 4);
		TextureRegion[] frames3 = splitFrames("hero/hero_sheet.png", 2, 2);
		Animation<TextureRegion> anim1 = new Animation<>(.25f, frames1);
		anim1.setPlayMode(Animation.PlayMode.LOOP);
		Animation<TextureRegion> anim2 = new Animation<>(.15f, frames2);
		anim2.setPlayMode(Animation.PlayMode.LOOP);
		Animation<TextureRegion> anim3 = new Animation<>(.2f, frames3);
		anim3.setPlayMode(Animation.PlayMode.NORMAL);
		animator.addAnim("idle", anim1);
		animator.addAnim("run", anim2);
		animator.addAnim("attack", anim3);
	}

	private void createDummy() {
		dummy = new GObject();
		dummy.transform.setPosition(220, 300);
		dummy.transform.setScale(1.2f);

		RigidbodyComponent drigi = dummy.addComponent(new RigidbodyComponent());

		TextureComponent dummyTexture = dummy.addComponent(new TextureComponent());
		dummyTexture.getScale().set(0.6f);
		dummyTexture.setOriginOffset(130, 120);

		AnimatorComponent dummyAnimator = dummy.addComponent(new AnimatorComponent(dummyTexture));
		TextureRegion[] dframes1 = splitFrames("monster1/monster1_sheet.png", 0, 3);
		TextureRegion[] dframes2 = splitFrames("monster1/monster1_sheet.png", 1, 2);
		Animation<TextureRegion> danim1 = new Animation<>(.25f, dframes1);
		danim1.setPlayMode(Animation.PlayMode.LOOP);
		Animation<TextureRegion> danim2 = new Animation<>(.2f, dframes2);
		danim2.setPlayMode(Animation.PlayMode.NORMAL);
		dummyAnimator.addAnim("idle", danim1);
		dummyAnimator.addAnim("hurt", danim2);

		RectColliderComponent dummyCollider = dummy.addComponent(new RectColliderComponent());
		dummyCollider.setSize(40, 100);
	}

	private void createUI() {
		uiSkin = GlobalAssets.getInstance().editorSkin;
		uiStage = new Stage();
		uiStage.setViewport(getViewport());
		getImp().addProcessor(uiStage);
		
		rocker = new Rocker(0, uiSkin);
		rocker.setPosition(20, 20);
		rocker.setSize(150, 150);
		uiStage.addActor(rocker);
		
		TextButton atkBtn = new TextButton("平A", uiSkin);
		atkBtn.addListener(new ClickListener(){
			public void clicked(InputEvent e, float x, float y){
				hero.getComponent(AnimatorComponent.class).setCurAnim("attack");
			}
		});
		atkBtn.setSize(80, 80);
		atkBtn.setPosition(getViewSize().x-50, 50, Align.bottomRight);
		uiStage.addActor(atkBtn);
	}

	private void createBackImage() {
		batch = new SpriteBatch();
		Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pm.setColor(0.5f, 0.5f, 0.5f, 1);
		pm.fill();
		backTex = new Texture(pm);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(.7f, .7f, .7f, 1);
		
		batch.begin();
		batch.setProjectionMatrix(getCamera().combined);
		batch.draw(backTex, 0, 0, getViewSize().x, getViewSize().y);
		batch.end();
		
		gameLogic(delta);

		uiStage.act(delta);
		uiStage.draw();
	}

	private void gameLogic(float delta) {
		gm.gameLoop(delta);
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

}
