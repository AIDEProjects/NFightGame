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
import com.goldsprite.nfightgame.Tools;
import com.goldsprite.nfightgame.core.components.*;
import com.goldsprite.nfightgame.core.components.fsms.DummyFsmComponent;
import com.goldsprite.nfightgame.core.components.fsms.HeroFsmComponent;
import com.goldsprite.nfightgame.core.ui.Rocker;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.GameSystem;
import com.goldsprite.nfightgame.core.ecs.component.*;
import com.goldsprite.nfightgame.core.fsms.enums.StateType;
import com.goldsprite.utils.math.Vector2Int;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class MainGameScreen extends GScreen {
	private GameSystem gm;

	private GObject hero, dummy, dummyHealthBar, monster2, wall, ground;

	private Skin uiSkin;
	private Stage uiStage;
	private Rocker rocker;

	private SpriteBatch batch;
	private Texture backTex;
	private FitViewport worldViewport, uiViewport;
	private OrthographicCamera worldCamera, uiCamera;
	int viewWidth = 1440, viewHeight = 810;
	private Label fpsLabel;
	private BitmapFont font;

	String heroPath = "sprites/roles/hero/hero_sheet.png";
	private final String monster1Path = "sprites/roles/monster1/monster1_sheet.png";
	private final String monster2Path = "sprites/roles/monster2/monster2_sheet.png";
	private final String[] healthBarPath = {
		"sprites/gui/healthBar/health_bar_back.png",
		"sprites/gui/healthBar/health_bar_bar.png",
		"sprites/gui/healthBar/health_bar_frame.png",
	};
	TextureRegion[] healthBarRegions = new TextureRegion[healthBarPath.length];
	private boolean showIntroduction = true;
	private final String introduction = "."
		+ "按键: WASD移动 JK攻击跳跃 Ctrl蹲下起立 蹲下时移动蹲行"
		+ "\n" + "测试: Y主角受伤 R主角重生"
		+ "\n" + "特殊: 可以蹭墙跳, 移动打断攻击, 跳跃攻击";

	@Override
	public Viewport getViewport() {
		return uiViewport;
	}

	@Override
	public void create() {
		worldViewport = new FitViewport(viewWidth, viewHeight, worldCamera = new OrthographicCamera());
		worldCamera.zoom = 0.65f;
		worldCamera.update();
		worldViewport.apply(true);
		uiViewport = new FitViewport(viewWidth, viewHeight, uiCamera = new OrthographicCamera());
		uiViewport.apply(true);

		batch = new SpriteBatch();

		loadTextures();

		createGM();

		backTex = Tools.createBackImage(new Color(0.5f, 0.5f, 0.5f, 1));

		createUI();

		createGObjects();
	}

	private void loadTextures() {
		//加载血条材质
		for (int i = 0; i < healthBarPath.length; i++) {
			Texture tex = new Texture(Gdx.files.internal(healthBarPath[i]));
			healthBarRegions[i] = new TextureRegion(tex);
		}
	}

	private void createGM() {
		gm = new GameSystem();
		gm.setViewport(worldViewport);

		gm.getGizmosRenderer().setEnabled(true);
	}

	private void createGObjects() {
		createDummy();

		createMonster2();

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
		ground.transform.setPosition(200, 150);

		RectColliderComponent groundCollider = ground.addComponent(new RectColliderComponent());
		groundCollider.setSize(1500, 40);
	}

	private void createMonster2() {
		monster2 = new GObject();
		monster2.transform.setPosition(400, 200);
		monster2.transform.setFace(-1, 1);

		RectColliderComponent heroBodyCollider = monster2.addComponent(new RectColliderComponent());
		heroBodyCollider.setOffsetPosition(0, 50);
		heroBodyCollider.setSize(35, 110);

		//		CircleColliderComponent m2AtkTrigger = monster2.addComponent(new CircleColliderComponent());
		//		m2AtkTrigger.setTrigger(true);
		//		m2AtkTrigger.setEnable(true);
		//		m2AtkTrigger.setOffsetPosition(79.5f, 90);
		//		m2AtkTrigger.setRadius(36);

		SpriteComponent texture = monster2.addComponent(new SpriteComponent());
		texture.getScale().set(1.5f);
		texture.setOriginOffset(119, 36);

		AnimatorComponent animator = monster2.addComponent(new AnimatorComponent(texture));
		animator.addAnim(StateType.Idle, new Animation<TextureRegion>(.25f, splitFrames(monster2Path, 0, 3), Animation.PlayMode.LOOP));
		animator.addAnim(StateType.Walk, new Animation<TextureRegion>(.25f, splitFrames(monster2Path, 1, 4), Animation.PlayMode.LOOP));
		animator.addAnim(StateType.Attack, new Animation<TextureRegion>(.15f, splitFrames(monster2Path, 2, 9), Animation.PlayMode.NORMAL));
		animator.addAnim(StateType.Hurt, new Animation<TextureRegion>(.15f, splitFrames(monster2Path, 3, 2), Animation.PlayMode.NORMAL));
		animator.addAnim(StateType.Death, new Animation<TextureRegion>(.2f, splitFrames(monster2Path, 4, 3), Animation.PlayMode.NORMAL));
	}

	private void createHero() {
		hero = new GObject();
		//变换属性
		hero.transform.setFace(1, 1);
		hero.transform.setPosition(100, 250);

		//身体碰撞器
		RectColliderComponent heroBodyCollider = hero.addComponent(new RectColliderComponent());
		heroBodyCollider.setOffsetPosition(0, 55);
		heroBodyCollider.setSize(34, 120);

		//脚底触发器
		CircleColliderComponent footTrigger = hero.addComponent(new CircleColliderComponent());
		footTrigger.setTrigger(true);
		footTrigger.setOffsetPosition(0, 0);
		footTrigger.setRadius(17);

		//攻击触发器
		CircleColliderComponent heroAtkTrigger = hero.addComponent(new CircleColliderComponent());
		heroAtkTrigger.setTrigger(true);
		heroAtkTrigger.setEnable(false);
		heroAtkTrigger.setOffsetPosition(79.5f, 90);
		heroAtkTrigger.setRadius(36);

		//物理特性
		RigidbodyComponent rigi = hero.addComponent(new RigidbodyComponent());
		rigi.setGravity(true);

		//材质组件
		SpriteComponent texture = hero.addComponent(new SpriteComponent());
		texture.getScale().set(1.5f);
		texture.setOriginOffset(119, 36);

		createHealthBar(hero.transform, 0, 140);

		//实体属性
		EntityComponent ent = hero.addComponent(new EntityComponent());
		ent.setMaxHealth(20, true);
		ent.setSpeed(300);
		ent.setBoostSpeedMultiplier(1.5f);

		//动画器组件
		AnimatorComponent animator = hero.addComponent(new AnimatorComponent(texture));
		animator.addAnim(StateType.Idle, new Animation<TextureRegion>(.25f, splitFrames(heroPath, 0, 3), Animation.PlayMode.LOOP));
		animator.addAnim(StateType.Run, new Animation<TextureRegion>(.15f, splitFrames(heroPath, 1, 4), Animation.PlayMode.LOOP));
		animator.addAnim(StateType.Attack, new Animation<TextureRegion>(.25f, splitFrames(heroPath, 2, 2), Animation.PlayMode.NORMAL));
		animator.addAnim(StateType.Crouching, new Animation<TextureRegion>(.2f, splitFrames(heroPath, 3, 3), Animation.PlayMode.NORMAL));
		animator.addAnim(StateType.Standing, new Animation<TextureRegion>(.2f, splitFrames(heroPath, 3, 3), Animation.PlayMode.REVERSED));
		animator.addAnim(StateType.CrouchWalk, new Animation<TextureRegion>(.25f, splitFrames(heroPath, 4, 4), Animation.PlayMode.LOOP));
		animator.addAnim(StateType.Sliding, new Animation<TextureRegion>(.1f, splitFrames(heroPath, 5, 3), Animation.PlayMode.NORMAL));
		animator.addAnim(StateType.Hurt, new Animation<TextureRegion>(.15f, splitFrames(heroPath, 6, 2), Animation.PlayMode.NORMAL));
		animator.addAnim(StateType.Death, new Animation<TextureRegion>(.2f, splitFrames(heroPath, 7, 3), Animation.PlayMode.NORMAL));
		animator.addAnim(StateType.Respawn, new Animation<TextureRegion>(.2f, splitFrames(heroPath, 7, 3), Animation.PlayMode.REVERSED));
		animator.addAnim(StateType.Jump, new Animation<TextureRegion>(.1f, splitFrames(heroPath, 8, 2), Animation.PlayMode.NORMAL));

		//状态机组件
		HeroFsmComponent fsm = hero.addComponent(new HeroFsmComponent());
		fsm.init();
		fsm.setFootCollider(footTrigger);
		fsm.setBodyCollider(heroBodyCollider);
		fsm.setAttackTrigger(heroAtkTrigger);

		//跟随相机组件
		FollowCamComponent camfollower = hero.addComponent(new FollowCamComponent());
		camfollower.setTarget(hero.transform);
	}

	private void createDummy() {
		dummy = new GObject();
		dummy.transform.setFace(-1, 1);
		dummy.transform.setPosition(220, 300);
		dummy.transform.setScale(1.2f);

		EntityComponent dummyEnt = dummy.addComponent(new EntityComponent());
		dummyEnt.setMaxHealth(20, true);

//		DummyControllerComponent dummyController = dummy.addComponent(new DummyControllerComponent());

		RigidbodyComponent drigi = dummy.addComponent(new RigidbodyComponent());

		SpriteComponent dummyTexture = dummy.addComponent(new SpriteComponent());
		dummyTexture.setSpriteFace(-1, 1);
		dummyTexture.getScale().set(0.6f);
		dummyTexture.setOriginOffset(140, 40);

		createHealthBar(dummy.transform, 0, 140);

		AnimatorComponent dummyAnimator = dummy.addComponent(new AnimatorComponent(dummyTexture));
		dummyAnimator.addAnim(StateType.Idle, new Animation<TextureRegion>(.25f, splitFrames(monster1Path, 0, 3), Animation.PlayMode.LOOP));
		dummyAnimator.addAnim(StateType.Hurt, new Animation<TextureRegion>(.2f, splitFrames(monster1Path, 1, 2), Animation.PlayMode.NORMAL));
		dummyAnimator.addAnim(StateType.Death, new Animation<TextureRegion>(.15f, splitFrames(monster1Path, 2, 5), Animation.PlayMode.NORMAL));

		RectColliderComponent dummyBodyCollider = dummy.addComponent(new RectColliderComponent());
		dummyBodyCollider.setSize(45, 100);
		dummyBodyCollider.setOffsetPosition(0, 45);

		DummyFsmComponent dummyFsm = dummy.addComponent(new DummyFsmComponent());
		dummyFsm.init();
		dummyFsm.setBodyCollider(dummyBodyCollider);
	}

	private void createHealthBar(TransformComponent bindEnt, float barOffsetX, float barOffsetY) {
		dummyHealthBar = new GObject();
		dummyHealthBar.transform.setPosition(300, 300);

		SpriteComponent[] textures = new SpriteComponent[healthBarRegions.length];
		for (int i = 0; i < healthBarRegions.length; i++) {
			TextureRegion region = new TextureRegion(healthBarRegions[i]);
			SpriteComponent s = dummyHealthBar.addComponent(new SpriteComponent());
			textures[i] = s;
			s.setRegion(region);
			s.getScale().set(2.6f);
			s.setOriginOffset(15, 3);
		}

		HealthBarComponent dummyHealthBarController = dummyHealthBar.addComponent(new HealthBarComponent());
		dummyHealthBarController.setHealthBarTextures(textures);
		dummyHealthBarController.bindEntity(bindEnt);
		dummyHealthBarController.setPositionOffset(barOffsetX, barOffsetY);
	}

	int m = 0;
	private void createUI() {
		uiSkin = GlobalAssets.getInstance().editorSkin;
		font = FontUtils.generate(100);
		font.getData().setScale(0.2f);

		uiStage = new Stage(uiViewport);
		getImp().addProcessor(uiStage);

//		rocker = new Rocker(0, uiSkin);
//		rocker.setPosition(30, 30);
//		rocker.setSize(225, 225);
//		uiStage.addActor(rocker);
//
//		TextButton atkBtn = new TextButton("平A", uiSkin);
//		atkBtn.addListener(new ClickListener() {
//			public void clicked(InputEvent e, float x, float y) {
//				hero.getComponent(RoleControllerComponent.class).attack();
//			}
//		});
//		atkBtn.setSize(100, 100);
//		atkBtn.setPosition(getViewSize().x - 75, 75, Align.bottomRight);
//		uiStage.addActor(atkBtn);
//
//		TextButton jumpBtn = new TextButton("跳", uiSkin);
//		jumpBtn.addListener(new ClickListener() {
//			public void clicked(InputEvent e, float x, float y) {
//				hero.getComponent(RoleControllerComponent.class).jump();
//			}
//		});
//		jumpBtn.setSize(100, 100);
//		jumpBtn.setPosition(getViewSize().x - 75 - (100 + 50), 75, Align.bottomRight);
//		uiStage.addActor(jumpBtn);
//
//		TextButton crouchBtn = new TextButton("蹲", uiSkin);
//		crouchBtn.addListener(new ClickListener() {
//			public void clicked(InputEvent e, float x, float y) {
//				RoleControllerComponent controller = hero.getComponent(RoleControllerComponent.class);
//				controller.crouching = !controller.crouching;
//			}
//		});
//		crouchBtn.setSize(100, 100);
//		crouchBtn.setPosition(getViewSize().x - 75 - (100 + 50) * 2, 75, Align.bottomRight);
//		uiStage.addActor(crouchBtn);
//
//		TextButton slidingBtn = new TextButton("滑铲", uiSkin);
//		slidingBtn.addListener(new ClickListener() {
//			public void clicked(InputEvent e, float x, float y) {
//				RoleControllerComponent controller = hero.getComponent(RoleControllerComponent.class);
//				controller.sliding = !controller.sliding;
//			}
//		});
//		slidingBtn.setSize(100, 100);
//		slidingBtn.setPosition(getViewSize().x - 75 - (100 + 50) * 2, 75, Align.bottomRight);
//		uiStage.addActor(slidingBtn);
//
//		TextButton cgAnimTargetBtn = new TextButton("切换动画目标", uiSkin);
//		cgAnimTargetBtn.setSize(180, 100);
//		cgAnimTargetBtn.setPosition(getViewSize().x - 75 - (180 + 50), getViewSize().y - 75, Align.topRight);
//		uiStage.addActor(cgAnimTargetBtn);
//		cgAnimTargetBtn.addListener(new ClickListener() {
//			public void clicked(InputEvent e, float x, float y) {
//				m = m==0?1:0;
//			}
//		});
//		TextButton cgAnimBtn = new TextButton("切换动画", uiSkin);
//		cgAnimBtn.setSize(180, 100);
//		cgAnimBtn.setPosition(getViewSize().x - 75, getViewSize().y - 75, Align.topRight);
//		uiStage.addActor(cgAnimBtn);
//		cgAnimBtn.addListener(new ClickListener() {
//			int index, index2;
//			AnimatorComponent animator, animator2;
//			String[] keys, keys2;
//			public void clicked(InputEvent e, float x, float y) {
//				if (m == 0) {
//					if (animator == null) {
//						animator = hero.getComponent(AnimatorComponent.class);
//						keys = animator.anims.keySet().toArray(new String[]{});
//					}
//					animator.setCurAnim(keys[++index % keys.length]);
//				} else if(m==1) {
//					if (animator2 == null) {
//						animator2 = monster2.getComponent(AnimatorComponent.class);
//						keys2 = animator2.anims.keySet().toArray(new String[]{});
//					}
//					animator2.setCurAnim(keys2[++index2 % keys2.length]);
//				}
//			}
//		});
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(.7f, .7f, .7f, 1);

		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();

		batch.draw(backTex, 0, 0, getViewSize().x, getViewSize().y);
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond() +"\n"+ (showIntroduction?introduction:""), 20, getViewSize().y - 20);

		batch.end();

		gm.gameLoop(delta);

		uiStage.act(delta);
		uiStage.draw();
	}

	public Array<TextureRegion> splitFrames(String path, int col, int count) {
		return splitFrames(path, col, count, 0);
	}
	public Array<TextureRegion> splitFrames(String path, int col, int count, int mode) {
		Texture hero_sheet = new Texture(Gdx.files.internal(path));
		Vector2Int cell = new Vector2Int(256, 256);
		Array<TextureRegion> frames = new Array<>();
		if(mode == 0){
			for (int i = 0; i < count; i++) {
				frames.add(new TextureRegion(hero_sheet, i * cell.x, col * cell.y, cell.x, cell.y));
			}
		}
		if(mode == 1){
			frames.add(new TextureRegion(hero_sheet, count * cell.x, col * cell.y, cell.x, cell.y));
		}
		return frames;
	}

}

