package com.goldsprite.nfightgame.screens.examples;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.IntMap;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.gdxcore.ui.GStage;

import java.util.ArrayList;
import java.util.List;

public class InputManagerExampleScreen extends GScreen {
	private GStage stage;
	private InputManager inputManager;

	@Override
	public void create() {
		stage = new GStage(getViewport());
		getImp().addProcessor(stage);

		inputManager = new InputManager();
		getImp().addProcessor(inputManager);

		// ===== 演示用例 =====

		// W / UP: 向上移动
		inputManager.registerKeys(Input.Keys.W, Input.Keys.UP)
			.onDown(() -> System.out.println("上键按下"))
			.onUp(() -> System.out.println("上键抬起"))
			.onHold(delta -> System.out.println("持续上移 delta=" + delta));

		// S / DOWN: 向下移动
		inputManager.registerKeys(Input.Keys.S, Input.Keys.DOWN)
			.onDown(() -> System.out.println("下键按下"))
			.onUp(() -> System.out.println("下键抬起"))
			.onHold(delta -> System.out.println("持续下移 delta=" + delta));

		// A / LEFT: 向左移动
		inputManager.registerKeys(Input.Keys.A, Input.Keys.LEFT)
			.onDown(() -> System.out.println("左键按下"))
			.onUp(() -> System.out.println("左键抬起"))
			.onHold(delta -> System.out.println("持续左移 delta=" + delta));

		// D / RIGHT: 向右移动
		inputManager.registerKeys(Input.Keys.D, Input.Keys.RIGHT)
			.onDown(() -> System.out.println("右键按下"))
			.onUp(() -> System.out.println("右键抬起"))
			.onHold(delta -> System.out.println("持续右移 delta=" + delta));

		// J: 攻击
		inputManager.registerKey(Input.Keys.J)
			.onDown(() -> System.out.println("J 攻击!"));

		// K: 跳跃
		inputManager.registerKey(Input.Keys.K)
			.onDown(() -> System.out.println("K 跳跃!"))
			.onUp(() -> System.out.println("K 松开跳跃键"));
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		inputManager.update(delta); // 每帧更新触发 onHold
		stage.act(delta);
		stage.draw();
	}

	// ================= 输入管理器 =================
	public static class InputManager extends InputAdapter {
		// 三个函数式接口（可用 lambda）
		public interface OnKeyDown { void handle(); }
		public interface OnKeyUp { void handle(); }
		public interface OnKeyHold { void handle(float delta); }

		// 一个 KeyListener 持有三种回调
		public static class KeyListener {
			OnKeyDown down;
			OnKeyUp up;
			OnKeyHold hold;

			public KeyListener onDown(OnKeyDown d) { this.down = d; return this; }
			public KeyListener onUp(OnKeyUp u) { this.up = u; return this; }
			public KeyListener onHold(OnKeyHold h) { this.hold = h; return this; }
		}

		private IntMap<List<KeyListener>> keyListeners = new IntMap<>();
		private IntMap<Boolean> pressedKeys = new IntMap<>();

		/** 注册单个键 */
		public KeyListener registerKey(int keycode) {
			KeyListener listener = new KeyListener();
			addListener(keycode, listener);
			return listener;
		}

		/** 注册多个键，共享同一个监听器 */
		public KeyListener registerKeys(int... keycodes) {
			KeyListener listener = new KeyListener();
			for (int keycode : keycodes) {
				addListener(keycode, listener);
			}
			return listener;
		}

		private void addListener(int keycode, KeyListener listener) {
			List<KeyListener> list = keyListeners.get(keycode);
			if (list == null) {
				list = new ArrayList<>();
				keyListeners.put(keycode, list);
			}
			list.add(listener);
		}

		@Override
		public boolean keyDown(int keycode) {
			pressedKeys.put(keycode, true);
			List<KeyListener> list = keyListeners.get(keycode);
			if (list != null) {
				for (KeyListener l : list) if (l.down != null) l.down.handle();
			}
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			pressedKeys.remove(keycode);
			List<KeyListener> list = keyListeners.get(keycode);
			if (list != null) {
				for (KeyListener l : list) if (l.up != null) l.up.handle();
			}
			return false;
		}

		/** 每帧更新，触发 onKeyHold */
		public void update(float delta) {
			for (IntMap.Entry<List<KeyListener>> e : keyListeners.entries()) {
				if (pressedKeys.containsKey(e.key)) {
					for (KeyListener l : e.value) {
						if (l.hold != null) l.hold.handle(delta);
					}
				}
			}
		}
	}
}
