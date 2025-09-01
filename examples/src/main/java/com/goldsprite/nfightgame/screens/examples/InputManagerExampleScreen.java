package com.goldsprite.nfightgame.screens.examples;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.goldsprite.gdxcore.logs.Log;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.gdxcore.ui.GStage;
import com.goldsprite.infinityworld.assets.GlobalAssets;
import com.goldsprite.nfightgame.inputs.GameVirtualKey;
import com.goldsprite.nfightgame.inputs.InputManager;
import com.goldsprite.nfightgame.inputs.widgets.VirtualButton;
import com.goldsprite.nfightgame.inputs.widgets.VirtualTouchpad;
import com.goldsprite.utils.math.Vector2;

public class InputManagerExampleScreen extends ExampleGScreen {
	@Override
	public String getIntroduction() {
		return "输入管理器示例: "
				+"\n效果: "
				+"\n键盘WASD/↑↓←→ 摇杆四方向 皆可响应上下左右的按下, 抬起以及持续"
				+"\n键盘J/Atk按钮攻击, 有按下与持续; 键盘K/无字按钮跳跃, 有按下与抬起"
				;
	}
	private final Vector2 introPos = new Vector2();
	@Override
	public Vector2 getIntroductionPos() {
		return introPos.set(20, getViewSize().y - 20);
	}
	private GStage stage;
	private InputManager inputManager;
	private VirtualTouchpad tp;
	private VirtualButton attackBtn, jumpBtn;

	@Override
	public void create() {
		stage = new GStage(getViewport());
		getImp().addProcessor(stage);

		inputManager = new InputManager();
		inputManager.setVirtualKey(GameVirtualKey.values()[0]);
		getImp().addProcessor(inputManager);

		// ===== 演示用例 =====

		// W / UP: 向上移动
		inputManager.registerKey(GameVirtualKey.UP)
				.onDown(() -> System.out.println("上键按下"))
				.onUp(() -> System.out.println("上键抬起"))
				.onHold(() -> System.out.println("持续上移 delta="));

		// S / DOWN: 向下移动
		inputManager.registerKey(GameVirtualKey.DOWN)
				.onDown(() -> System.out.println("下键按下"))
				.onUp(() -> System.out.println("下键抬起"))
				.onHold(() -> System.out.println("持续下移 delta="));

		// A / LEFT: 向左移动
		inputManager.registerKey(GameVirtualKey.LEFT)
				.onDown(() -> System.out.println("左键按下"))
				.onUp(() -> System.out.println("左键抬起"))
				.onHold(() -> System.out.println("持续左移 delta="));

		// D / RIGHT: 向右移动
		inputManager.registerKey(GameVirtualKey.RIGHT)
				.onDown(() -> System.out.println("右键按下"))
				.onUp(() -> System.out.println("右键抬起"))
				.onHold(() -> System.out.println("持续右移 delta="));

		// J: 攻击
		inputManager.registerKey(GameVirtualKey.ATTACK)
				.onDown(() -> System.out.println("J 攻击!"))
				.onHold(() -> Log.log("持续攻击"))
				.onUp(() -> Log.log("攻击抬起"));

		// K: 跳跃
		inputManager.registerKey(GameVirtualKey.JUMP)
				.onDown(() -> System.out.println("K 跳跃!"))
				.onUp(() -> System.out.println("K 松开跳跃键"));


		Skin skin = GlobalAssets.getInstance().editorSkin;
		// Touchpad 摇杆
		tp = new VirtualTouchpad(100, 0.3f, skin);
		tp.setPosition(50, 50);
		tp.bindVirtualKey(GameVirtualKey.UP, GameVirtualKey.DOWN, GameVirtualKey.LEFT, GameVirtualKey.RIGHT);
		stage.addActor(tp);

		// 攻击按钮
		attackBtn = new VirtualButton("Atk", skin);
		attackBtn.setBounds(getViewSize().x - 250, 100, 200, 100);
		attackBtn.bindVirtualKey(GameVirtualKey.ATTACK);
		stage.addActor(attackBtn);

		// 跳跃按钮
		jumpBtn = new VirtualButton("", skin);
		jumpBtn.setBounds(getViewSize().x - 250, 250, 200, 100);
		jumpBtn.bindVirtualKey(GameVirtualKey.JUMP);
		stage.addActor(jumpBtn);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		inputManager.update(delta);

		stage.act(delta);
		stage.draw();
	}


	// ================= 输入管理器 =================


}

