package com.goldsprite.nfightgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.gdxcore.utils.FontUtils;
import com.goldsprite.infinityworld.assets.GlobalAssets;
import com.goldsprite.nfightgame.core.DummyControllerComponent;
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
	private FitViewport worldViewport, uiViewport;
	private Camera worldCamera, uiCamera;
	int viewWidth = 1440, viewHeight = 810;
	private Label fpsLabel;
	private BitmapFont font;

	@Override
	public Viewport getViewport() {
		return uiViewport;
	}

	@Override
	public void create() {
		worldViewport = new FitViewport(viewWidth, viewHeight, worldCamera = new OrthographicCamera());
		worldViewport.apply(true);
		uiViewport = new FitViewport(viewWidth, viewHeight, uiCamera = new OrthographicCamera());
		uiViewport.apply(true);

		gm = new GameSystem();
		gm.setCamera(worldCamera);

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

		GObject wall2 = new GObject();
		wall2.transform.setPosition(800, 350);

		RectColliderComponent wall2Collider = wall2.addComponent(new RectColliderComponent());
		wall2Collider.setSize(200, 100);


		ground = new GObject();
		ground.transform.setPosition(600, 150);

		RectColliderComponent groundCollider = ground.addComponent(new RectColliderComponent());
		groundCollider.setSize(1000, 40);
	}

	private void createHero() {
		hero = new GObject();

		hero.transform.setFace(1, 1);
		hero.transform.setPosition(100, 250);

//		CircleColliderComponent heroFootCollider = hero.addComponent(new CircleColliderComponent());
//		heroFootCollider.setOffsetPosition(0, 22.5f);
//		heroFootCollider.setRadius(22.5f);
		RectColliderComponent heroBodyCollider = hero.addComponent(new RectColliderComponent());
		heroBodyCollider.setOffsetPosition(5, 50);
		heroBodyCollider.setSize(35, 110);

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

		SpriteComponent texture = hero.addComponent(new SpriteComponent());
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
		dummy.transform.setFace(-1, 1);
		dummy.transform.setPosition(220, 300);
		dummy.transform.setScale(1.2f);

		DummyControllerComponent dummyController = dummy.addComponent(new DummyControllerComponent());

		RigidbodyComponent drigi = dummy.addComponent(new RigidbodyComponent());

		SpriteComponent dummyTexture = dummy.addComponent(new SpriteComponent());
		dummyTexture.setSpriteFace(-1, 1);
		dummyTexture.getScale().set(0.6f);
		dummyTexture.setOriginOffset(140, 120);

		AnimatorComponent dummyAnimator = dummy.addComponent(new AnimatorComponent(dummyTexture));
		TextureRegion[] dframes1 = splitFrames("monster1/monster1_sheet.png", 0, 3);
		TextureRegion[] dframes2 = splitFrames("monster1/monster1_sheet.png", 1, 2);
		Animation<TextureRegion> danim1 = new Animation<>(.25f, dframes1);
		danim1.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		Animation<TextureRegion> danim2 = new Animation<>(.2f, dframes2);
		danim2.setPlayMode(Animation.PlayMode.NORMAL);
		dummyAnimator.addAnim("idle", danim1);
		dummyAnimator.addAnim("hurt", danim2);

		RectColliderComponent dummyCollider = dummy.addComponent(new RectColliderComponent());
		dummyCollider.setSize(45, 100);
	}

	private void createUI() {
		uiSkin = GlobalAssets.getInstance().editorSkin;
		font = FontUtils.generate(35);
		font.getData().setScale(1f);

		uiStage = new Stage(uiViewport);
		getImp().addProcessor(uiStage);

		rocker = new Rocker(0, uiSkin);
		rocker.setPosition(30, 30);
		rocker.setSize(225, 225);
		uiStage.addActor(rocker);

		TextButton atkBtn = new TextButton("平A", uiSkin);
		atkBtn.addListener(new ClickListener(){
			public void clicked(InputEvent e, float x, float y){
				hero.getComponent(AnimatorComponent.class).setCurAnim("attack");
			}
		});
		atkBtn.setSize(100, 100);
		atkBtn.setPosition(getViewSize().x-75, 75, Align.bottomRight);
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


		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();

		batch.draw(backTex, 0, 0, getViewSize().x, getViewSize().y);
		font.draw(batch, "FPS: "+Gdx.graphics.getFramesPerSecond(), 20, getViewSize().y - 20);

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
