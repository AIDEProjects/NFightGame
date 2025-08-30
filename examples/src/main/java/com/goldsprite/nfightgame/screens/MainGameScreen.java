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
import com.goldsprite.nfightgame.core.*;

public class MainGameScreen extends GScreen {
	private GameSystem gm;

	private GObject hero, dummy, dummyHealthBar, wall, ground;

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

		createGM();

		createBackImage();

		createUI();

		createGObjects();
	}

	private void createGM() {
		gm = new GameSystem();
		gm.setViewport(worldViewport);

//		gm.getGizmosRenderer().setEnabled(false);
	}

	private void createGObjects() {
		createDummy();

		createHero();

		wall = new GObject();
		wall.transform.setPosition(500, 345);

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

		RectColliderComponent heroBodyCollider = hero.addComponent(new RectColliderComponent());
		heroBodyCollider.setOffsetPosition(0, 50);
		heroBodyCollider.setSize(35, 110);

		CircleColliderComponent heroAtkTrigger = hero.addComponent(new CircleColliderComponent());
		heroAtkTrigger.setTrigger(true);
		heroAtkTrigger.setEnable(false);
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

		String path = "sprites/roles/hero/hero_sheet.png";
		AnimatorComponent animator = hero.addComponent(new AnimatorComponent(texture));
		animator.addAnim("idle", new Animation<TextureRegion>(.25f, splitFrames(path, 0, 3), Animation.PlayMode.LOOP));
		animator.addAnim("run", new Animation<TextureRegion>(.15f, splitFrames(path, 1, 4), Animation.PlayMode.LOOP));
		animator.addAnim("attack",
				new Animation<TextureRegion>(.25f, splitFrames(path, 2, 2), Animation.PlayMode.NORMAL));
		animator.addAnim("crouch",
				new Animation<TextureRegion>(.2f, splitFrames(path, 3, 3), Animation.PlayMode.NORMAL));
		animator.addAnim("crouchMove",
				new Animation<TextureRegion>(.25f, splitFrames(path, 4, 4), Animation.PlayMode.LOOP));
		animator.addAnim("sliding",
				new Animation<TextureRegion>(.1f, splitFrames(path, 5, 3), Animation.PlayMode.NORMAL));
	}

	private void createDummy() {
		dummy = new GObject();
		dummy.transform.setFace(-1, 1);
		dummy.transform.setPosition(220, 300);
		dummy.transform.setScale(1.2f);

		EntityComponent dummyEnt = dummy.addComponent(new EntityComponent());
		dummyEnt.setMaxHealth(20);
		dummyEnt.setHealth(20);

		DummyControllerComponent dummyController = dummy.addComponent(new DummyControllerComponent());

		RigidbodyComponent drigi = dummy.addComponent(new RigidbodyComponent());

		SpriteComponent dummyTexture = dummy.addComponent(new SpriteComponent());
		dummyTexture.setSpriteFace(-1, 1);
		dummyTexture.getScale().set(0.6f);
		dummyTexture.setOriginOffset(140, 40);

		createDummyHealthBar();

		String path = "sprites/roles/monster1/monster1_sheet.png";
		AnimatorComponent dummyAnimator = dummy.addComponent(new AnimatorComponent(dummyTexture));
		dummyAnimator.addAnim("idle",
				new Animation<TextureRegion>(.25f, splitFrames(path, 0, 3), Animation.PlayMode.LOOP));
		dummyAnimator.addAnim("hurt",
				new Animation<TextureRegion>(.2f, splitFrames(path, 1, 2), Animation.PlayMode.NORMAL));

		RectColliderComponent dummyCollider = dummy.addComponent(new RectColliderComponent());
		dummyCollider.setSize(45, 100);
		dummyCollider.setOffsetPosition(0, 45);
	}

	private void createDummyHealthBar() {
		dummyHealthBar = new GObject();
		dummyHealthBar.transform.setPosition(300, 300);

		String[] pathes = {
			"sprites/gui/healthBar/health_bar_back.png",
			"sprites/gui/healthBar/health_bar_bar.png",
			"sprites/gui/healthBar/health_bar_frame.png",
		};
		SpriteComponent[] textures = new SpriteComponent[pathes.length];
		for(int i=0;i<pathes.length;i++){
			Texture tex = new Texture(Gdx.files.internal(pathes[i]));
			TextureRegion region = new TextureRegion(tex);
//			region.setRegionX(5);
			SpriteComponent s = dummyHealthBar.addComponent(new SpriteComponent());
			textures[i] = s;
			s.setRegion(region);
			s.getScale().set(2.6f);
			s.setOriginOffset(15, 3);
		}

		HealthBarComponent dummyHealthBarController = dummyHealthBar.addComponent(new HealthBarComponent());
		dummyHealthBarController.setHealthBarTextures(textures[1]);
		dummyHealthBarController.bindEntity(dummy.transform);
		dummyHealthBarController.setPositionOffset(0, 140);
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
		atkBtn.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				hero.getComponent(RoleControllerComponent.class).attack();
			}
		});
		atkBtn.setSize(100, 100);
		atkBtn.setPosition(getViewSize().x - 75, 75, Align.bottomRight);
		uiStage.addActor(atkBtn);

		TextButton jumpBtn = new TextButton("跳", uiSkin);
		jumpBtn.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				hero.getComponent(RoleControllerComponent.class).jump();
			}
		});
		jumpBtn.setSize(100, 100);
		jumpBtn.setPosition(getViewSize().x - 75 - 100 - 50, 75, Align.bottomRight);
		uiStage.addActor(jumpBtn);

//		TextButton cgAnimBtn = new TextButton("切换动画", uiSkin);
//		cgAnimBtn.setSize(180, 100);
//		cgAnimBtn.setPosition(getViewSize().x - 75, getViewSize().y - 75, Align.topRight);
//		uiStage.addActor(cgAnimBtn);
//		cgAnimBtn.addListener(new ClickListener() {
//			int index;
//			AnimatorComponent animator;
//			String[] animNames;
//			public void clicked(InputEvent e, float x, float y) {
//				if (animator == null) {
//					animator = hero.getComponent(AnimatorComponent.class);
//					animNames = animator.anims.keySet().toArray(new String[]{});
//				}
//				animator.setCurAnim(animNames[index++ % animNames.length]);
//			}
//		});
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
		font.draw(batch, "帧率FPS: " + Gdx.graphics.getFramesPerSecond(), 20, getViewSize().y - 20);

		batch.end();

		gm.gameLoop(delta);

		uiStage.act(delta);
		uiStage.draw();
	}

	public Array<TextureRegion> splitFrames(String path, int col, int count) {
		Texture hero_sheet = new Texture(Gdx.files.internal(path));
		Vector2Int cell = new Vector2Int(256, 256);
		Array<TextureRegion> frames = new Array<>();
		for (int i = 0; i < count; i++) {
			frames.add(new TextureRegion(hero_sheet, i * cell.x, col * cell.y, cell.x, cell.y));
		}
		return frames;
	}

}

