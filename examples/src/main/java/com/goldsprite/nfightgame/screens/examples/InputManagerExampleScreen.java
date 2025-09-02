package com.goldsprite.nfightgame.screens.examples;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.goldsprite.gdxcore.ui.GStage;
import com.goldsprite.infinityworld.assets.GlobalAssets;
import com.goldsprite.nfightgame.inputs.*;
import com.goldsprite.nfightgame.inputs.widgets.VirtualButton;
import com.goldsprite.nfightgame.inputs.widgets.VirtualJoystick;
import com.goldsprite.utils.math.Vector2;

import java.util.function.Consumer;

public class InputManagerExampleScreen extends ExampleGScreen {
	private final Vector2 introPos = new Vector2();
	private GStage stage;
	private InputSystem inputSystem;
	private VirtualJoystick joystick;
	private VirtualButton attackBtn, jumpBtn;
	private final Consumer<Vector2> onMove = dir -> {
		System.out.println("onMove " + dir);
	};
	private final Consumer<Boolean> onAttack = down -> {
		System.out.println("onAttack_down " + down);
	};
	private final Consumer<Boolean> onJump = down -> {
		System.out.println("onJump_down " + down);
	};

	@Override
	public String getIntroduction() {
		return "输入管理器示例: "
			+ "\n效果: "
			+ "\n键盘WASD/↑↓←→ 摇杆四方向 皆可响应上下左右的按下, 抬起以及持续变更"
			+ "\n键盘J/Atk按钮攻击；键盘K/无字按钮跳跃"
			;
	}

	@Override
	public Vector2 getIntroductionPos() {
		return introPos.set(20, getViewSize().y - 20);
	}

	@Override
	public void create() {
		stage = new GStage(getViewport());
		inputSystem = InputSystem.getInstance();

		getImp().addProcessor(stage);
		getImp().addProcessor(inputSystem);

		//创建按键行为表实例
		InputActions inputActions = new InputActions();
		inputSystem.setInputActions(inputActions);

		//创建按键行为并添加到表
		InputAction moveAction = new InputAction();
		moveAction.setValueType(Vector2.class);
		Vector2KeyBinding WASD = new Vector2KeyBinding();
		WASD.Up = new KeyBinding(Input.Keys.W);
		WASD.Down = new KeyBinding(Input.Keys.S);
		WASD.Left = new KeyBinding(Input.Keys.A);
		WASD.Right = new KeyBinding(Input.Keys.D);
		moveAction.addInputBinding(WASD);
		Vector2KeyBinding FourArrowKey = new Vector2KeyBinding();
		FourArrowKey.Up = new KeyBinding(Input.Keys.UP);
		FourArrowKey.Down = new KeyBinding(Input.Keys.DOWN);
		FourArrowKey.Left = new KeyBinding(Input.Keys.LEFT);
		FourArrowKey.Right = new KeyBinding(Input.Keys.RIGHT);
		moveAction.addInputBinding(FourArrowKey);
		VirtualJoystickBinding joystickBinding = new VirtualJoystickBinding();
		moveAction.addInputBinding(joystickBinding);
		inputActions.addAction("Move", moveAction);

		InputAction attackAction = new InputAction();
		attackAction.setValueType(Boolean.class);
		KeyBinding keyboardJ = new KeyBinding(Input.Keys.J);
		attackAction.addInputBinding(keyboardJ);
		VirtualButtonBinding virtualButton = new VirtualButtonBinding();
		attackAction.addInputBinding(virtualButton);
		inputActions.addAction("Attack", attackAction);

		InputAction jumpAction = new InputAction();
		jumpAction.setValueType(Boolean.class);
		KeyBinding keyboardK = new KeyBinding(Input.Keys.K);
		jumpAction.addInputBinding(keyboardK);
		KeyBinding keyboardSpace = new KeyBinding(Input.Keys.SPACE);
		jumpAction.addInputBinding(keyboardSpace);
		VirtualButtonBinding virtualButton2 = new VirtualButtonBinding();
		jumpAction.addInputBinding(virtualButton2);
		inputActions.addAction("Jump", jumpAction);


		//注册按键行为到输入系统
		inputSystem.registerActionListener(inputActions.getAction("Move"), onMove, Vector2.class);
		inputSystem.registerActionListener(inputActions.getAction("Attack"), onAttack, Boolean.class);
		inputSystem.registerActionListener(inputActions.getAction("Jump"), onJump, Boolean.class);

		Skin skin = GlobalAssets.getInstance().editorSkin;
		// Touchpad 摇杆
		joystick = new VirtualJoystick(100, 0.3f, skin);
		joystick.setPosition(50, 50);
		stage.addActor(joystick);
		joystickBinding.setJoystick(joystick);

		// 攻击按钮
		attackBtn = new VirtualButton("Atk", skin);
		attackBtn.setBounds(getViewSize().x - 250, 100, 200, 100);
		stage.addActor(attackBtn);
		virtualButton.setVirtualButton(attackBtn);

		// 跳跃按钮
		jumpBtn = new VirtualButton("", skin);
		jumpBtn.setBounds(getViewSize().x - 250, 250, 200, 100);
		stage.addActor(jumpBtn);
		virtualButton2.setVirtualButton(jumpBtn);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		inputSystem.update(delta);

		stage.act(delta);
		stage.draw();
	}


	// ================= 输入管理器 =================


}

