package com.goldsprite.nfightgame.screens.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.goldsprite.gdxcore.ecs.component.*;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.gdxcore.ui.GSplitPane;
import com.goldsprite.gdxcore.ui.GStage;
import com.goldsprite.gdxcore.utils.ColorTextureUtils;
import com.goldsprite.gdxcore.utils.FontUtils;
import com.goldsprite.gdxcore.utils.Pivot;
import com.goldsprite.nfightgame.assets.GlobalAssets;
import com.goldsprite.nfightgame.components.EntityComponent;
import com.goldsprite.nfightgame.components.EntityInputManagerComponent;
import com.goldsprite.nfightgame.components.FollowCamComponent;
import com.goldsprite.nfightgame.components.HealthBarComponent;
import com.goldsprite.nfightgame.fsm.fsms.DummyFsmComponent;
import com.goldsprite.nfightgame.fsm.fsms.EntityFsmComponent;
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

	private Skin skin;
	private GStage uiStage;
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
	int worldViewWidth = 1728, worldViewHeight = 810;
	int uiViewWidth = 1152, uiViewHeight = 540;
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
	private final String introduction = "."
		+ "按键: WASD移动 Shift疾跑 J攻击K/Space跳跃 Ctrl蹲下起立 蹲下时移动蹲行"
		+ "\n" + "测试: Y主角受伤 R主角重生 Q切换角色"
		+ "\n" + "特殊: 可以蹭墙跳, 移动打断攻击, 跳跃攻击";

	TextureRegion blackRegion;
	@Override
	public void create() {
		blackRegion = ColorTextureUtils.createColorTextureRegion(Color.BLACK);

		worldViewport = new FitViewport(worldViewWidth, worldViewHeight, worldCamera = new OrthographicCamera());
		worldCamera.zoom = 0.65f;//0.65f
		worldCamera.update();
		worldViewport.apply(true);
		uiViewport = new FitViewport(uiViewWidth, uiViewHeight, uiCamera = new OrthographicCamera());
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
		gm.getGizmosRenderer().setEnabled(enableGizmos);
	}

	private void createGObjects() {
		createBack();

		createDummy();

		createLizardMan();

		createHero();

		createTerrias();
	}

	private void createBack() {
		TextureRegion backRegion = new TextureRegion(new Texture(Gdx.files.internal(back2Path)));

		GObject back = new GObject();
		back.transform.setPosition(500, 150);

		SpriteComponent sprite = back.addComponent(new SpriteComponent());
		sprite.setRegion(backRegion);
		sprite.setOriginOffset(0, -310);
		sprite.getScale().set(1.4f);

		SpriteComponent spriteLeft = back.addComponent(new SpriteComponent());
		spriteLeft.setRegion(backRegion);
		spriteLeft.setOriginOffset(0+1024, -310);
		spriteLeft.getScale().set(1.4f);

		SpriteComponent spriteRight = back.addComponent(new SpriteComponent());
		spriteRight.setRegion(backRegion);
		spriteRight.setOriginOffset(0-1024, -310);
		spriteRight.getScale().set(1.4f);

//		SpriteComponent spriteTop = back.addComponent(new SpriteComponent());
//		spriteTop.setRegion(backRegion);
//		spriteTop.setOriginOffset(0, 576+310);
//		spriteTop.setSpriteFace(1, -1);
//		spriteTop.getScale().set(1.4f);
	}

	private void createTerrias() {
		float sizeX = 0, sizeY = 0;

		ground = new GObject("Ground");
		ground.transform.setPosition(200, 150);

		sizeX = 4000;sizeY = 40;
		SpriteComponent groundSprite = ground.addComponent(new SpriteComponent());
		groundSprite.setRegion(blackRegion);
		groundSprite.setScale(sizeX, sizeY);

		RectColliderComponent groundCollider = ground.addComponent(new RectColliderComponent());
		groundCollider.setSize(sizeX, sizeY);


		float groundTop = ground.transform.getPosition().y + groundCollider.getSize().y/2;//170


		wall = new GObject("Wall");
		wall.transform.setPosition(500, 345);

		sizeX = 100;sizeY = 200;
		SpriteComponent wallSprite = wall.addComponent(new SpriteComponent());
		wallSprite.setRegion(blackRegion);
		wallSprite.setScale(sizeX, sizeY);

		RectColliderComponent wallCollider = wall.addComponent(new RectColliderComponent());
		wallCollider.setSize(sizeX, sizeY);


		GObject wall2 = new GObject();
		wall2.transform.setPosition(800, 350);

		sizeX = 200;sizeY = 100;

		SpriteComponent wall2Sprite = wall2.addComponent(new SpriteComponent());
		wall2Sprite.setRegion(blackRegion);
		wall2Sprite.setScale(sizeX, sizeY);

		RectColliderComponent wall2Collider = wall2.addComponent(new RectColliderComponent());
		wall2Collider.setSize(sizeX, sizeY);


		GObject wall3 = new GObject();
		wall3.transform.setPosition(-50, groundTop + 270 + 45);

		sizeX = 50;sizeY = 500;

		SpriteComponent wall3Sprite = wall3.addComponent(new SpriteComponent());
		wall3Sprite.setRegion(new TextureRegion(blackRegion));
		wall3Sprite.setScale(sizeX, sizeY);

		RectColliderComponent wall3Collider = wall3.addComponent(new RectColliderComponent());
		wall3Collider.setSize(sizeX, sizeY);


		GObject wall4 = new GObject();
		wall4.transform.setPosition(-175-50, groundTop + 270 + 40 + 20);

		sizeX = 50;sizeY = 500;

		SpriteComponent wall4Sprite = wall4.addComponent(new SpriteComponent());
		wall4Sprite.setRegion(new TextureRegion(blackRegion));
		wall4Sprite.setScale(sizeX, sizeY);

		RectColliderComponent wall4Collider = wall4.addComponent(new RectColliderComponent());
		wall4Collider.setSize(sizeX, sizeY);


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
		texture.setOriginOffset(-9, -88);

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
		texture.setOriginOffset(-9, -92);

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
		animator.addAnim(StateType.Jump, new Animation<TextureRegion>(.2f, splitFrames(heroPath, 8, 2), Animation.PlayMode.LOOP));
		animator.addAnim(StateType.Fall, new Animation<TextureRegion>(.2f, splitFrames(heroPath, 9, 2), Animation.PlayMode.LOOP));

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
		dummyTexture.setOriginOffset(12, -88);

		createHealthBar(dummy.transform, 0, 140);

		AnimatorComponent dummyAnimator = dummy.addComponent(new AnimatorComponent(dummyTexture));
		dummyAnimator.addAnim(StateType.Idle, new Animation<TextureRegion>(.25f, splitFrames(dummyPath, 0, 3), Animation.PlayMode.LOOP));
		dummyAnimator.addAnim(StateType.Hurt, new Animation<TextureRegion>(.2f, splitFrames(dummyPath, 1, 2), Animation.PlayMode.NORMAL));
		dummyAnimator.addAnim(StateType.Death, new Animation<TextureRegion>(.15f, splitFrames(dummyPath, 2, 5), Animation.PlayMode.NORMAL));
		dummyAnimator.addAnim(StateType.Respawn, new Animation<TextureRegion>(.15f, splitFrames(dummyPath, 2, 5), Animation.PlayMode.REVERSED));

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
			s.setPivot(Pivot.LeftDown);
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
	MTable guiTable;
	// 比例系数（当前分辨率 ÷ 设计分辨率）
	float scale = 960f / 1440f;  // = 0.666...

	// 工具方法：缩放并创建虚拟按钮
	private VirtualButton createButton(String text, float size, float x, float y, int align, Skin skin, Table guiTable) {
		VirtualButton btn = new VirtualButton(text, skin);
		btn.setSize(size, size);
		btn.setPosition(x, y, align);
		guiTable.addActor(btn);
		return btn;
	}

	private void createUI() {
		skin = GlobalAssets.getInstance().editorSkin;
		font = FontUtils.generate(100);
		font.getData().setScale(0.2f);

		uiStage = new GStage(uiViewport);
		getImp().addProcessor(uiStage);

		guiTable = new MTable();
		guiTable.setFillParent(true);

		float scale = 960f / 1440f;

		// === 摇杆 ===
		VirtualJoystick joystick = new VirtualJoystick(225 * scale, 0.1f, skin);
		joystick.setPosition(30 * scale, 30 * scale);
		guiTable.addActor(joystick);
		GameInputSystem.getInstance().setVirtualJoystick(joystick);

		// === 通用按钮参数 ===
		float btnSize = 100 * scale;
		float margin = 50 * scale;
		float baseY = 75 * scale;

		// 攻击按钮
		VirtualButton atkBtn = createButton("平A", btnSize,
			getViewSize().x - 75 * scale, baseY, Align.bottomRight, skin, guiTable);
		GameInputSystem.getInstance().setAttackVirButton(atkBtn);

		// 跳
		VirtualButton jumpBtn = createButton("跳", btnSize,
			getViewSize().x - 75 * scale - (btnSize + margin), baseY, Align.bottomRight, skin, guiTable);
		GameInputSystem.getInstance().setJumpVirButton(jumpBtn);

		// 蹲
		VirtualButton crouchBtn = createButton("蹲", btnSize,
			getViewSize().x - 75 * scale - (btnSize + margin) * 2, baseY, Align.bottomRight, skin, guiTable);
		GameInputSystem.getInstance().setCrouchVirButton(crouchBtn);

		// 疾跑（左上）
		VirtualButton speedBoostBtn = createButton("疾跑", btnSize,
			75 * scale, 30 * scale + 225 * scale + 200 * scale, Align.bottomLeft, skin, guiTable);
		GameInputSystem.getInstance().setSpeedBoostVirButton(speedBoostBtn);

		// 受伤
		VirtualButton hurtBtn = createButton("受伤", btnSize,
			getViewSize().x - 75 * scale - (btnSize + margin) * 3, baseY, Align.bottomRight, skin, guiTable);
		GameInputSystem.getInstance().setHurtVirButton(hurtBtn);

		// 复活
		VirtualButton respawnBtn = createButton("复活", btnSize,
			getViewSize().x - 75 * scale - (btnSize + margin) * 4, baseY, Align.bottomRight, skin, guiTable);
		GameInputSystem.getInstance().setRespawnVirButton(respawnBtn);

		// 切换角色
		VirtualButton changeRoleBtn = createButton("切换角色", btnSize,
			getViewSize().x - 75 * scale - (btnSize + margin) * 5, baseY, Align.bottomRight, skin, guiTable);
		GameInputSystem.getInstance().setChangeRoleVirButton(changeRoleBtn);

		//切换角色按钮
		//try catch是防止角色没有被添加
		try {
			entInputs = new EntityInputManagerComponent[]{
				hero.getComponent(EntityInputManagerComponent.class),
				dummy.getComponent(EntityInputManagerComponent.class),
				lizardman.getComponent(EntityInputManagerComponent.class)
			};
			for (EntityInputManagerComponent i : entInputs) {
				i.active = false;
			}
			entInputs[0].active = true;
			//轮换启用单个角色的输入器
			Consumer<Boolean> onChangeRole = down -> {
				nextRole();
			};
			GameInputSystem.getInstance().registerActionListener(GameInputSystem.getInstance().getInputAction("ChangeRole"), onChangeRole, Boolean.class);
		} catch (Exception e) {
		}
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

		createInspector();
	}

	private void nextRole() {
		for (EntityInputManagerComponent i : entInputs) {
			i.active = false;
		}
		changeRoleIndex = ++changeRoleIndex % entInputs.length;
		entInputs[changeRoleIndex].active = true;
		entInputs[lastChangeRoleIndex].getGObject().removeComponent(camfollower);
		entInputs[changeRoleIndex].addComponent(camfollower);
		entInputs[changeRoleIndex].getComponent(FollowCamComponent.class).setTarget(entInputs[changeRoleIndex].getTransform());
		lastChangeRoleIndex = changeRoleIndex;
	}

	Table inspectorTable;
	GSplitPane inspectorPanelGSplit;
	boolean enableGizmos = false;
	private void createInspector() {
		String[] tabs = {"检查器", "编辑配置"};
		Table page1 = new Table(skin);
		{
			page1.add(new Label("第一页", skin)).pad(20);
		}
		Table page2 = new Table(skin);
		{
			page2.add(new Label("第二页", skin)).pad(20).row();
			CheckBox toggleGizmosEnable = new CheckBox("调试线("+(options.enableGizmos?"开":"关")+")", skin, "switch");
			toggleGizmosEnable.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent changeEvent, Actor actor) {
					options.enableGizmos = !options.enableGizmos;
					gm.getGizmosRenderer().setEnabled(options.enableGizmos);
					toggleGizmosEnable.setText("调试线("+(options.enableGizmos?"开":"关")+")");
				}
			});
			page2.add(toggleGizmosEnable).row();
			CheckBox toggleIntros = new CheckBox("介绍("+(options.showIntros?"开":"关")+")", skin, "switch");
			toggleIntros.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent changeEvent, Actor actor) {
					options.showIntros = !options.showIntros;
					toggleIntros.setText("介绍("+(options.showIntros?"开":"关")+")");
				}
			});
			page2.add(toggleIntros).row();
		}
		Table[] pages = {page1, page2};
		inspectorTable = createPagesTab(skin, tabs, pages);

		ScrollPane inspectorScrollPane = new ScrollPane(inspectorTable, skin);
		inspectorScrollPane.setScrollingDisabled(false, false); // 允许横向和纵向滚动
		inspectorScrollPane.setCancelTouchFocus(false);
		inspectorScrollPane.setSmoothScrolling(false); // 禁用平滑滚动动画
		inspectorPanelGSplit = new GSplitPane(inspectorScrollPane, guiTable, false, skin);
		inspectorPanelGSplit.setToggleData(0, 0.25f);
		inspectorPanelGSplit.setAbsoluteSplitAmount(0);
		inspectorPanelGSplit.setFillParent(true);
		inspectorPanelGSplit.addListener(new ClickListener() {
			private float startX, startY;
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				startX = x;
				startY = y;
				return true; // 拦截事件
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				float dx = x - startX;
				float dy = y - startY;
				float distanceSquared = dx * dx + dy * dy;
				float tolerance = 1f;
				if (distanceSquared < tolerance * tolerance) { // 阈值 10 像素以内算点击
					Actor hit = inspectorPanelGSplit.hit(x, y, true);
					if (hit == inspectorPanelGSplit) {
						inspectorPanelGSplit.toggle();
					}
				}
			}
		});
		uiStage.addActor(inspectorPanelGSplit);
	}

	private Table createPagesTab(Skin skin, String[] tabNames, Table[] pageContents) {
		if (tabNames.length != pageContents.length) {
			throw new IllegalArgumentException("tabNames 和 pageContents 数量必须一致");
		}

		// 顶层总表，背景 panel1
		Table root = new Table(skin);
		root.setBackground("panel1");

		// 菜单栏，背景 list
		Table menuBar = new Table(skin);
		menuBar.setBackground("list");

		// 内容，背景 list
		Table content = new Table(skin);
		content.setBackground("list");

		// 页面堆叠
		Stack pages = new Stack();
		for (Table page : pageContents) {
			page.setVisible(false);
			pages.add(page);
		}
		pageContents[0].setVisible(true);

		content.add(pages).expand().fill();

		// 创建按钮
		for (int i = 0; i < tabNames.length; i++) {
			final int index = i;
			TextButton tabButton = new TextButton(tabNames[i], skin);

			tabButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					for (int j = 0; j < pageContents.length; j++) {
						pageContents[j].setVisible(j == index);
					}
				}
			});

			// 按钮均分宽度，最小高度 150
			menuBar.add(tabButton).expandX().fillX().minHeight(50).pad(5);
		}

		// 布局
		root.top().pad(10);
		root.add(menuBar).growX().row();
		root.add(content).expand().fill().pad(10);

		return root;
	}


	private <T extends Table> void marginLRB(T layout, float margin) {
		layout.padLeft(margin);
		layout.padRight(margin);
		layout.padBottom(margin);
	}

	private void updateInspector() {

	}

	@Override
	public void render(float delta) {
		super.render(delta);

		GameInputSystem.getInstance().update(delta);

		gm.gameLoop(delta);

		if(options.showIntros) {
			batch.setProjectionMatrix(uiCamera.combined);
			batch.begin();
			font.draw(batch, "介绍: " + "\n" + introduction, 40, getViewSize().y - 20);
			batch.end();
		}

		updateInspector();

		uiStage.getBatch().setProjectionMatrix(uiCamera.combined);
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


	Options options = new Options();
	public class Options{
		public boolean showIntros = true;

		public boolean enableGizmos = false;
	}


	/**
	 * 作用是拦截点击事件： 让其可以精准点到分割条
	 */
	public class MTable extends Table {
		public MTable() {
			setTouchable(Touchable.enabled);
		}

		@Override
		public Actor hit(float x, float y, boolean touchable) {
			// 先让父类处理子控件
			Actor hit = super.hit(x, y, touchable);
			if (hit != null && hit != this) {
				return hit; // 点击到子控件，返回子控件
			}

			// 点击在 MTable 自己的空白区域
			if (isVisible() && x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
				return this;
			}

			return null; // 其他地方
		}
	}

}

