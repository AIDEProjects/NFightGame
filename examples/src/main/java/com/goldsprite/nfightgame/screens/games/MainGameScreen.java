package com.goldsprite.nfightgame.screens.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.goldsprite.gdxcore.ecs.component.*;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.gdxcore.utils.ColorTextureUtils;
import com.goldsprite.gdxcore.utils.FontUtils;
import com.goldsprite.infinityworld.assets.GlobalAssets;
import com.goldsprite.nfightgame.components.EntityComponent;
import com.goldsprite.nfightgame.components.EntityInputManagerComponent;
import com.goldsprite.nfightgame.components.FollowCamComponent;
import com.goldsprite.nfightgame.components.HealthBarComponent;
import com.goldsprite.nfightgame.fsm.fsms.DummyFsmComponent;
import com.goldsprite.nfightgame.fsm.fsms.HeroFsmComponent;
import com.goldsprite.nfightgame.fsm.fsms.LizardmanFsmComponent;
import com.goldsprite.gdxcore.ecs.GObject;
import com.goldsprite.gdxcore.ecs.GameSystem;
import com.goldsprite.nfightgame.fsm.enums.StateType;
import com.goldsprite.nfightgame.inputs.GameInputSystem;
import com.goldsprite.nfightgame.inputs.widgets.VirtualButton;
import com.goldsprite.nfightgame.inputs.widgets.VirtualJoystick;
import com.goldsprite.utils.math.Vector2Int;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import java.util.function.Consumer;

public class MainGameScreen extends GScreen {
	private GameSystem gm;

	private GObject hero, dummy, dummyHealthBar, lizardman, wall, ground;

	private Skin uiSkin;
	private Stage uiStage;
	private VirtualJoystick joystick;

	private SpriteBatch batch;
	private Color screenColor = new Color(.0f, .0f, .0f, 1);
	private FollowCamComponent camfollower;

	@Override
	public Color getBackColor(){return backColor;}
	private Color backColor = new Color(.0f, .0f, .0f, 1);
	@Override
	public Color getScreenColor(){return screenColor;}
	private FitViewport worldViewport, uiViewport;
	@Override
	public Viewport getViewport() {
		return uiViewport;
	}
	private OrthographicCamera worldCamera, uiCamera;
	int viewWidth = 1440, viewHeight = 810;
	private BitmapFont font;

	String heroPath = "sprites/roles/hero/hero_sheet.png";
	private final String dummyPath = "sprites/roles/dummy/dummy_sheet.png";
	private final String lizardManPath = "sprites/roles/lizardMan/lizardMan_sheet.png";
	private final String[] healthBarPath = {
		"sprites/gui/healthBar/health_bar_back.png",
		"sprites/gui/healthBar/health_bar_bar.png",
		"sprites/gui/healthBar/health_bar_frame.png",
	};
	TextureRegion[] healthBarRegions = new TextureRegion[healthBarPath.length];
	String back1Path = "sprites/backs/back_moon.png";
	String back2Path = "sprites/backs/back_moon2.jpg";

	private final boolean showIntroduction = true;
	private final String introduction = "."
		+ "按键: WASD移动 Shift疾跑 J攻击K/Space跳跃 Ctrl蹲下起立 蹲下时移动蹲行"
		+ "\n" + "测试: Y主角受伤 R主角重生 Q切换角色"
		+ "\n" + "特殊: 可以蹭墙跳, 移动打断攻击, 跳跃攻击";

	TextureRegion blackRegion;
	@Override
	public void create() {
		blackRegion = ColorTextureUtils.createColorTextureRegion(Color.BLACK);

		worldViewport = new FitViewport(viewWidth, viewHeight, worldCamera = new OrthographicCamera());
		worldCamera.zoom = 0.65f;//0.65f
		worldCamera.update();
		worldViewport.apply(true);
		uiViewport = new FitViewport(viewWidth, viewHeight, uiCamera = new OrthographicCamera());
		uiViewport.apply(true);

		batch = new SpriteBatch();

		loadTextures();

		createGM();

		createGObjects();

		createUI();
	}

	private void loadTextures() {
		//加载血条材质
		for (int i = 0; i < healthBarPath.length; i++) {
			Texture tex = new Texture(Gdx.files.internal(healthBarPath[i]));
			healthBarRegions[i] = new TextureRegion(tex);
		}
	}

	private void createGM() {
		//初始化游戏系统
		gm = new GameSystem();
		gm.setViewport(worldViewport);

		//初始化游戏输入器
		GameInputSystem inputs = GameInputSystem.getInstance();
		getImp().addProcessor(inputs);

		//启用/禁用调式绘制器
		gm.getGizmosRenderer().setEnabled(true);
	}

	private void createGObjects() {
		createBack();

		createDummy();

		createLizardMan();

		createHero();

		createTerrias();
	}

	private void createBack() {
		GObject back = new GObject();
		back.transform.setPosition(500, 150);

		SpriteComponent sprite = back.addComponent(new SpriteComponent());
		sprite.setRegion(new TextureRegion(new Texture(Gdx.files.internal(back2Path))));
		sprite.setOriginOffset(512, 50);
		sprite.getScale().set(1.4f);

		SpriteComponent spriteLeft = back.addComponent(new SpriteComponent());
		spriteLeft.setRegion(new TextureRegion(new Texture(Gdx.files.internal(back2Path))));
		spriteLeft.setOriginOffset(512+1024, 50);
		spriteLeft.getScale().set(1.4f);

		SpriteComponent spriteRight = back.addComponent(new SpriteComponent());
		spriteRight.setRegion(new TextureRegion(new Texture(Gdx.files.internal(back2Path))));
		spriteRight.setOriginOffset(512-1024, 50);
		spriteRight.getScale().set(1.4f);


		int k;

	}

	private void createTerrias() {
		ground = new GObject("Ground");
		ground.transform.setPosition(200, 150);

		RectColliderComponent groundCollider = ground.addComponent(new RectColliderComponent());
		groundCollider.setSize(4000, 40);


		float groundTop = ground.transform.getPosition().y + groundCollider.getSize().y/2;//170


		wall = new GObject("Wall");
		wall.transform.setPosition(500, 345);

		float sizeX = 100, sizeY = 200;

		RectColliderComponent wallCollider = wall.addComponent(new RectColliderComponent());
		wallCollider.setSize(sizeX, sizeY);

		SpriteComponent wallSprite = wall.addComponent(new SpriteComponent());
		wallSprite.setRegion(ColorTextureUtils.createColorTextureRegion(Color.WHITE));
		wallSprite.setScale(sizeX, sizeY);
		wallSprite.setOriginOffset(sizeX/2, sizeY/2);
/*

		GObject wall2 = new GObject();
		wall2.transform.setPosition(800, 350);

		sizeX = 200;sizeY = 100;
		RectColliderComponent wall2Collider = wall2.addComponent(new RectColliderComponent());
		wall2Collider.setSize(sizeX, sizeY);

		SpriteComponent wall2Sprite = wall2.addComponent(new SpriteComponent());
		wall2Sprite.setRegion(ColorTextureUtils.createColorTextureRegion(Color.BLACK));
		wall2Sprite.setScale(sizeX, sizeY);
		wall2Sprite.setOriginOffset(sizeX/2, sizeY/2);
*/

		/*GObject wall3 = new GObject();
		wall3.transform.setPosition(-50, groundTop + 270 + 45);

		sizeX = 50;sizeY = 500;
		RectColliderComponent wall3Collider = wall3.addComponent(new RectColliderComponent());
		wall3Collider.setSize(sizeX, sizeY);

		SpriteComponent wall3Sprite = wall3.addComponent(new SpriteComponent());
		wall3Sprite.setRegion(new TextureRegion(blackRegion));
		wall3Sprite.setScale(sizeX, sizeY);
		wall3Sprite.setOriginOffset(sizeX/2, sizeY/2);


		GObject wall4 = new GObject();
		wall4.transform.setPosition(-175-50, groundTop + 270 + 40 + 20);

		sizeX = 50;sizeY = 500;
		RectColliderComponent wall4Collider = wall4.addComponent(new RectColliderComponent());
		wall4Collider.setSize(sizeX, sizeY);int k;

		SpriteComponent wall4Sprite = wall4.addComponent(new SpriteComponent());
		wall4Sprite.setRegion(new TextureRegion(blackRegion));
		wall4Sprite.setScale(sizeX, sizeY);
		wall4Sprite.setOriginOffset(sizeX/2, sizeY/2);

*/

		GObject worldTxt1 = new GObject();
		worldTxt1.transform.setPosition(-160, groundTop + 40);

		WorldTxtComponent worldTxtComp = worldTxt1.addComponent(new WorldTxtComponent());
		worldTxtComp.initFnt(20);
		worldTxtComp.setText("你能上去吗?");


		GObject worldTxt2 = new GObject();
		worldTxt2.transform.setPosition(-180, groundTop + 40 + 700);

		WorldTxtComponent worldTxtComp2 = worldTxt2.addComponent(new WorldTxtComponent());
		worldTxtComp2.initFnt(20);
		worldTxtComp2.setText("上来也没啥用, 嘿嘿");
	}

	private void createLizardMan() {
		lizardman = new GObject("LizardMan");
		lizardman.transform.setScale(1.1f);
		lizardman.transform.setPosition(400, 200);
		lizardman.transform.setFace(-1, 1);

		RigidbodyComponent rigi = lizardman.addComponent(new RigidbodyComponent());

		//身体碰撞器
		RectColliderComponent bodyCollider = lizardman.addComponent(new RectColliderComponent());
		bodyCollider.setOffsetPosition(0, 50);
		bodyCollider.setSize(35, 110);

		//脚底触发器
		CircleColliderComponent footTrigger = lizardman.addComponent(new CircleColliderComponent());
		footTrigger.setTrigger(true);
		footTrigger.setOffsetPosition(0, 0);
		footTrigger.setRadius(17);

		//攻击触发器
		CircleColliderComponent atkTrigger = lizardman.addComponent(new CircleColliderComponent());
		atkTrigger.setTrigger(true);
		atkTrigger.setEnable(false);
		atkTrigger.setOffsetPosition(65f, 65);
		atkTrigger.setRadius(28);

		createHealthBar(lizardman.transform, 0, 120);

		SpriteComponent texture = lizardman.addComponent(new SpriteComponent());
		texture.getScale().set(1.5f);
		texture.setOriginOffset(119, 40);

		//实体属性
		EntityComponent ent = lizardman.addComponent(new EntityComponent());
		ent.setMaxHealth(20, true);
		ent.setSpeed(140);
		ent.setBoostSpeedMultiplier(1.5f);

		AnimatorComponent animator = lizardman.addComponent(new AnimatorComponent(texture));
		animator.addAnim(StateType.Idle, new Animation<TextureRegion>(.25f, splitFrames(lizardManPath, 0, 3), Animation.PlayMode.LOOP));
		animator.addAnim(StateType.Move, new Animation<TextureRegion>(.25f, splitFrames(lizardManPath, 1, 4), Animation.PlayMode.LOOP));
		animator.addAnim(StateType.Attack, new Animation<TextureRegion>(.15f, splitFrames(lizardManPath, 2, 9), Animation.PlayMode.NORMAL));
		animator.addAnim(StateType.Hurt, new Animation<TextureRegion>(.15f, splitFrames(lizardManPath, 3, 2), Animation.PlayMode.NORMAL));
		animator.addAnim(StateType.Death, new Animation<TextureRegion>(.2f, splitFrames(lizardManPath, 4, 3), Animation.PlayMode.NORMAL));
		animator.addAnim(StateType.Respawn, new Animation<TextureRegion>(.2f, splitFrames(lizardManPath, 4, 3), Animation.PlayMode.REVERSED));
		animator.addAnim(StateType.Jump, new Animation<TextureRegion>(.1f, splitFrames(lizardManPath, 5, 2), Animation.PlayMode.NORMAL));

		LizardmanFsmComponent fsm = lizardman.addComponent(new LizardmanFsmComponent());
		fsm.setFootTrigger(footTrigger);
		fsm.setBodyCollider(bodyCollider);
		fsm.setAttackTrigger(atkTrigger);
		fsm.init();
		fsm.setEnableInput(true);

		EntityInputManagerComponent inputs = lizardman.addComponent(new EntityInputManagerComponent());
		inputs.bindFsm(fsm);
	}

	private void createHero() {
		hero = new GObject("Hero");
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
		animator.addAnim(StateType.Move, new Animation<TextureRegion>(.15f, splitFrames(heroPath, 1, 4), Animation.PlayMode.LOOP));
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
		fsm.setFootTrigger(footTrigger);
		fsm.setBodyCollider(heroBodyCollider);
		fsm.setAttackTrigger(heroAtkTrigger);
		fsm.init();
		fsm.setEnableInput(true);

//		//实体输入机
		EntityInputManagerComponent inputs = hero.addComponent(new EntityInputManagerComponent());
		inputs.bindFsm(fsm);

		//跟随相机组件
		camfollower = hero.addComponent(new FollowCamComponent());
		camfollower.setTarget(hero.transform);
	}

	private void createDummy() {
		dummy = new GObject("Dummy");
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
		dummyAnimator.addAnim(StateType.Idle, new Animation<TextureRegion>(.25f, splitFrames(dummyPath, 0, 3), Animation.PlayMode.LOOP));
		dummyAnimator.addAnim(StateType.Hurt, new Animation<TextureRegion>(.2f, splitFrames(dummyPath, 1, 2), Animation.PlayMode.NORMAL));
		dummyAnimator.addAnim(StateType.Death, new Animation<TextureRegion>(.15f, splitFrames(dummyPath, 2, 5), Animation.PlayMode.NORMAL));

		RectColliderComponent dummyBodyCollider = dummy.addComponent(new RectColliderComponent());
		dummyBodyCollider.setSize(45, 100);
		dummyBodyCollider.setOffsetPosition(0, 45);

		//Dummy状态机
		DummyFsmComponent dummyFsm = dummy.addComponent(new DummyFsmComponent());
		dummyFsm.init();
		dummyFsm.setBodyCollider(dummyBodyCollider);

		//实体输入机
		EntityInputManagerComponent inputs = dummy.addComponent(new EntityInputManagerComponent());
		inputs.bindFsm(dummyFsm);
	}

	private void createHealthBar(TransformComponent bindEnt, float barOffsetX, float barOffsetY) {
		dummyHealthBar = new GObject(bindEnt.getGObject().getName()+"_HealthBar");
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
	int changeRoleIndex = 0, lastChangeRoleIndex=0;
	EntityInputManagerComponent[] entInputs;
	private void createUI() {
		uiSkin = GlobalAssets.getInstance().editorSkin;
		font = FontUtils.generate(100);
		font.getData().setScale(0.2f);

		uiStage = new Stage(uiViewport);
		getImp().addProcessor(uiStage);

		joystick = new VirtualJoystick(225, 0.1f, uiSkin);
		joystick.setPosition(30, 30);
		uiStage.addActor(joystick);
		GameInputSystem.getInstance().setVirtualJoystick(joystick);

		VirtualButton atkBtn = new VirtualButton("平A", uiSkin);
		atkBtn.setSize(100, 100);
		atkBtn.setPosition(getViewSize().x - 75, 75, Align.bottomRight);
		uiStage.addActor(atkBtn);
		GameInputSystem.getInstance().setAttackVirButton(atkBtn);

		VirtualButton jumpBtn = new VirtualButton("跳", uiSkin);
		jumpBtn.setSize(100, 100);
		jumpBtn.setPosition(getViewSize().x - 75 - (100 + 50), 75, Align.bottomRight);
		uiStage.addActor(jumpBtn);
		GameInputSystem.getInstance().setJumpVirButton(jumpBtn);

		VirtualButton crouchBtn = new VirtualButton("蹲", uiSkin);
		crouchBtn.setSize(100, 100);
		crouchBtn.setPosition(getViewSize().x - 75 - (100 + 50) * 2, 75, Align.bottomRight);
		uiStage.addActor(crouchBtn);
		GameInputSystem.getInstance().setCrouchVirButton(crouchBtn);

		VirtualButton speedBoostBtn = new VirtualButton("疾跑", uiSkin);
		speedBoostBtn.setSize(100, 100);
		speedBoostBtn.setPosition(75, 30+225+200, Align.bottomLeft);
		uiStage.addActor(speedBoostBtn);
		GameInputSystem.getInstance().setSpeedBoostVirButton(speedBoostBtn);

		VirtualButton hurtBtn = new VirtualButton("受伤", uiSkin);
		hurtBtn.setSize(100, 100);
		hurtBtn.setPosition(getViewSize().x - 75 - (100 + 50) * 3, 75, Align.bottomRight);
		uiStage.addActor(hurtBtn);
		GameInputSystem.getInstance().setHurtVirButton(hurtBtn);

		VirtualButton respawnBtn = new VirtualButton("复活", uiSkin);
		respawnBtn.setSize(100, 100);
		respawnBtn.setPosition(getViewSize().x - 75 - (100 + 50) * 4, 75, Align.bottomRight);
		uiStage.addActor(respawnBtn);
		GameInputSystem.getInstance().setRespawnVirButton(respawnBtn);

		VirtualButton changeRoleBtn = new VirtualButton("切换角色", uiSkin);
		changeRoleBtn.setSize(100, 100);
		changeRoleBtn.setPosition(getViewSize().x - 75 - (100 + 50) * 5, 75, Align.bottomRight);
		uiStage.addActor(changeRoleBtn);
		GameInputSystem.getInstance().setChangeRoleVirButton(changeRoleBtn);

		//切换角色按钮
		entInputs = new EntityInputManagerComponent[]{
			hero.getComponent(EntityInputManagerComponent.class),
			dummy.getComponent(EntityInputManagerComponent.class),
			lizardman.getComponent(EntityInputManagerComponent.class)
		};
		for(EntityInputManagerComponent i : entInputs) {
			i.active = false;
		}
		entInputs[0].active = true;
		//轮换启用单个角色的输入器
		Consumer<Boolean> onChangeRole = down -> {
			for(EntityInputManagerComponent i : entInputs) {
				i.active = false;
			}
			changeRoleIndex = ++changeRoleIndex%entInputs.length;
			entInputs[changeRoleIndex].active = true;
			entInputs[lastChangeRoleIndex].getGObject().removeComponent(camfollower);
			entInputs[changeRoleIndex].addComponent(camfollower);
			entInputs[changeRoleIndex].getComponent(FollowCamComponent.class).setTarget(entInputs[changeRoleIndex].getTransform());
			lastChangeRoleIndex = changeRoleIndex;
		};
		GameInputSystem.getInstance().registerActionListener(GameInputSystem.getInstance().getInputAction("ChangeRole"), onChangeRole, Boolean.class);
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
//						animator2 = lizardMan.getComponent(AnimatorComponent.class);
//						keys2 = animator2.anims.keySet().toArray(new String[]{});
//					}
//					animator2.setCurAnim(keys2[++index2 % keys2.length]);
//				}
//			}
//		});
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();

		font.draw(batch, "介绍: " + "\n"+ (showIntroduction?introduction:""), 20, getViewSize().y - 20);

		batch.end();

		GameInputSystem.getInstance().update(delta);

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

