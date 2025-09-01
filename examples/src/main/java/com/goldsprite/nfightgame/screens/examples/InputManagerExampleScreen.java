package com.goldsprite.nfightgame.screens.examples;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.goldsprite.gdxcore.logs.Log;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.gdxcore.ui.GStage;
import com.goldsprite.infinityworld.assets.GlobalAssets;

import java.util.*;

public class InputManagerExampleScreen extends GScreen {
	private GStage stage;
	private InputManager inputManager;
	private Touchpad tp;
	private TextButton attackBtn, jumpBtn;

	@Override
	public void create() {
		stage = new GStage(getViewport());
		getImp().addProcessor(stage);

		inputManager = new InputManager<VirtualKey>();
		inputManager.setVirtualKey(VirtualKey.values()[0]);
		getImp().addProcessor(inputManager);

		// ===== 演示用例 =====

		// W / UP: 向上移动
		inputManager.registerKey(VirtualKey.UP)
			.onDown(() -> System.out.println("上键按下"))
			.onUp(() -> System.out.println("上键抬起"))
			.onHold(() -> System.out.println("持续上移 delta="));

		// S / DOWN: 向下移动
		inputManager.registerKey(VirtualKey.DOWN)
			.onDown(() -> System.out.println("下键按下"))
			.onUp(() -> System.out.println("下键抬起"))
			.onHold(() -> System.out.println("持续下移 delta="));

		// A / LEFT: 向左移动
		inputManager.registerKey(VirtualKey.LEFT)
			.onDown(() -> System.out.println("左键按下"))
			.onUp(() -> System.out.println("左键抬起"))
			.onHold(() -> System.out.println("持续左移 delta="));

		// D / RIGHT: 向右移动
		inputManager.registerKey(VirtualKey.RIGHT)
			.onDown(() -> System.out.println("右键按下"))
			.onUp(() -> System.out.println("右键抬起"))
			.onHold(() -> System.out.println("持续右移 delta="));

		// J: 攻击
		inputManager.registerKey(VirtualKey.ATTACK)
			.onDown(() -> System.out.println("J 攻击!"))
			.onHold(() -> Log.log("持续攻击"))
			.onUp(() -> Log.log("攻击抬起"));

		// K: 跳跃
		inputManager.registerKey(VirtualKey.JUMP)
			.onDown(() -> System.out.println("K 跳跃!"))
			.onUp(() -> System.out.println("K 松开跳跃键"));


		Skin skin = GlobalAssets.getInstance().editorSkin;
		// Touchpad 摇杆
		float tpSize = 100;
		tp = new Touchpad(0.3f * tpSize/2, skin);
		tp.setBounds(50, 50, tpSize, tpSize);
		stage.addActor(tp);

		// 攻击按钮
		attackBtn = new TextButton("Attack", skin);
		attackBtn.setBounds(getViewSize().x - 250, 100, 200, 100);
		attackBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				inputManager.actionDown(VirtualKey.ATTACK);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				inputManager.actionUp(VirtualKey.ATTACK);
			}
		});
		stage.addActor(attackBtn);

		// 跳跃按钮
		jumpBtn = new TextButton("Jump", skin);
		jumpBtn.setBounds(getViewSize().x - 250, 250, 200, 100);
		jumpBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				inputManager.actionDown(VirtualKey.JUMP);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				inputManager.actionUp(VirtualKey.JUMP);
			}
		});
		stage.addActor(jumpBtn);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		stage.act(delta);

		virtualInputUpdate();
		inputManager.update(delta); // 每帧更新触发 onHold

		stage.draw();
	}

	private boolean isTouchpadPressed;
	private VirtualKey prevDir;
	private void virtualInputUpdate() {
		VirtualKey dir = getVirtualDir();

		//down
		if(!isTouchpadPressed && dir != null){
			isTouchpadPressed = true;
			inputManager.actionDown(dir);
			prevDir = dir;
		}
		//hold
		if(isTouchpadPressed && dir != null){
			if(dir != prevDir) {
				inputManager.actionUp(prevDir);//换向
				inputManager.actionDown(dir);
			}else {
				inputManager.actionHold(dir);
			}
			prevDir = dir;
		}
		//up
		if(isTouchpadPressed && dir == null){
			isTouchpadPressed = false;
			inputManager.actionUp(prevDir);
		}

		if(attackBtn.isPressed()) inputManager.actionHold(VirtualKey.ATTACK);
		if(jumpBtn.isPressed()) inputManager.actionHold(VirtualKey.JUMP);
	}
	public VirtualKey getVirtualDir(){
		VirtualKey dir = null;
		float x = tp.getKnobPercentX();
		float y = tp.getKnobPercentY();

		if (x == 0 && y == 0) return dir;

		float angle = (float) Math.toDegrees(Math.atan2(y, x));
		if (angle < 0) angle += 360;

		if (angle >= 315 || angle < 45) dir = VirtualKey.RIGHT;
		if (angle >= 45 && angle < 135) dir = VirtualKey.UP;
		if (angle >= 135 && angle < 225) dir = VirtualKey.LEFT;
		if(angle >= 225 && angle < 315) dir = VirtualKey.DOWN;

		return dir;
	}

	// ================= 输入管理器 =================
	public static class InputManager<T extends Enum> extends InputAdapter {

		public void actionDown(T virtualKey) {
			if(!virtualKeyListeners.containsKey(virtualKey)) return;
			virtualKeyListeners.get(virtualKey).forEach((listener) -> {if(listener.down != null) listener.down.handle();});
			updateVirtualKey(virtualKey, true);
		}

		public void actionHold(T virtualKey) {
			if(!virtualKeyListeners.containsKey(virtualKey)) return;
			virtualKeyListeners.get(virtualKey).forEach((listener) -> {if(listener.hold != null) listener.hold.handle();});
		}

		public void actionUp(T virtualKey) {
			if(!virtualKeyListeners.containsKey(virtualKey)) return;
			virtualKeyListeners.get(virtualKey).forEach((listener) -> {if(listener.up != null) listener.up.handle();});
			updateVirtualKey(virtualKey, false);
		}

		public interface IVirtualKey<T extends Enum>{ T mapping(int keycode); }

		public interface OnKeyDown { void handle(); }
		public interface OnKeyUp { void handle(); }
		public interface OnKeyHold { void handle(); }

		public static class KeyListener {
			OnKeyDown down;
			OnKeyUp up;
			OnKeyHold hold;

			public KeyListener onDown(OnKeyDown d) { this.down = d; return this; }
			public KeyListener onUp(OnKeyUp u) { this.up = u; return this; }
			public KeyListener onHold(OnKeyHold h) { this.hold = h; return this; }
		}

		private IVirtualKey<T> virtualKeys;
		private Map<T, List<KeyListener>> virtualKeyListeners = new HashMap<>();
		private Map<T, Integer> pressedVirtualKeys = new HashMap<>();

		public T getVirtualKey(int keycode) {
			return virtualKeys.mapping(keycode);
		}
		public void setVirtualKey(IVirtualKey<T> virtualKeys) {
			this.virtualKeys = virtualKeys;
		}
		public KeyListener registerKey(T virtualKey) {
			KeyListener listener = new KeyListener();
			addListener(virtualKey, listener);
			return listener;
		}
		private void updateVirtualKey(T virtualKey, boolean pressed) {
			pressedVirtualKeys.compute(virtualKey, (k, prev) -> (prev == null ? 0 : prev) + (pressed ? 1 : -1));
		}

		private void addListener(T virtualKey, KeyListener listener) {
			List<KeyListener> list = virtualKeyListeners.get(virtualKey);
			if (list == null) {
				list = new ArrayList<>();
				virtualKeyListeners.put(virtualKey, list);
			}
			list.add(listener);
		}

		@Override
		public boolean keyDown(int keycode) {
			T virtualKey = getVirtualKey(keycode);
			updateVirtualKey(virtualKey, true);
			List<KeyListener> list = virtualKeyListeners.get(virtualKey);
			if (list != null) {
				for (KeyListener l : list) if (l.down != null) l.down.handle();
			}
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			T virtualKey = getVirtualKey(keycode);
			updateVirtualKey(virtualKey, false);
			List<KeyListener> list = virtualKeyListeners.get(virtualKey);
			if (list != null) {
				for (KeyListener l : list) if (l.up != null) l.up.handle();
			}
			return false;
		}

		/** 每帧更新，触发 onKeyHold */
		public void update(float delta) {
			for (Map.Entry<T, List<KeyListener>> e : virtualKeyListeners.entrySet()) {
				if (pressedVirtualKeys.containsKey(e.getKey()) && pressedVirtualKeys.get(e.getKey()) > 0) {
					for (KeyListener l : e.getValue()) {
						if (l.hold != null) l.hold.handle();
					}
				}
			}
		}
	}

	public static class TouchControls {

	}

	public enum VirtualKey implements InputManager.IVirtualKey<VirtualKey> {
		UP, DOWN, LEFT, RIGHT, ATTACK, JUMP;
		private static final Map<Integer, VirtualKey> keyboardMap = new HashMap<Integer, VirtualKey>(){{
			put(Input.Keys.W, VirtualKey.UP);
			put(Input.Keys.S, VirtualKey.DOWN);
			put(Input.Keys.A, VirtualKey.LEFT);
			put(Input.Keys.D, VirtualKey.RIGHT);
			put(Input.Keys.UP, VirtualKey.UP);
			put(Input.Keys.DOWN, VirtualKey.DOWN);
			put(Input.Keys.LEFT, VirtualKey.LEFT);
			put(Input.Keys.RIGHT, VirtualKey.RIGHT);
			put(Input.Keys.J, VirtualKey.ATTACK);
			put(Input.Keys.K, VirtualKey.JUMP);
		}};

		@Override
		public VirtualKey mapping(int keycode) {
			return keyboardMap.get(keycode);
		}
	}


}

