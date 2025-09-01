package com.goldsprite.nfightgame.inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputManager extends InputAdapter {
	private static InputManager instance;
	public static InputManager getInstance() {
		return instance;
	}
	private IVirtualKey virtualKeys;
	private Map<Enum, List<KeyListener>> virtualKeyListeners = new HashMap<>();
	private Map<Enum, Integer> pressedVirtualKeys = new HashMap<>();

	public InputManager() {
		instance = this;
	}

	public Enum getVirtualKey(int keycode) {
		return virtualKeys.mapping(keycode);
	}
	public void setVirtualKey(IVirtualKey virtualKeys) {
		this.virtualKeys = virtualKeys;
	}
	public KeyListener registerKey(Enum virtualKey) {
		KeyListener listener = new KeyListener();
		addListener(virtualKey, listener);
		return listener;
	}
	private void updateVirtualKey(Enum virtualKey, boolean pressed) {
		pressedVirtualKeys.compute(virtualKey, (k, prev) -> (prev == null ? 0 : prev) + (pressed ? 1 : -1));
	}

	private void addListener(Enum virtualKey, KeyListener listener) {
		List<KeyListener> list = virtualKeyListeners.get(virtualKey);
		if (list == null) {
			list = new ArrayList<>();
			virtualKeyListeners.put(virtualKey, list);
		}
		list.add(listener);
	}

	@Override
	public boolean keyDown(int keycode) {
		Enum virtualKey = getVirtualKey(keycode);
		updateVirtualKey(virtualKey, true);
		List<KeyListener> list = virtualKeyListeners.get(virtualKey);
		if (list != null) {
			for (KeyListener l : list) if (l.down != null) l.down.invoke();
		}
		return false;
	}

	public void update(float delta) {
		for (Map.Entry<Enum, List<KeyListener>> e : virtualKeyListeners.entrySet()) {
			if (pressedVirtualKeys.containsKey(e.getKey()) && pressedVirtualKeys.get(e.getKey()) > 0) {
				for (KeyListener l : e.getValue()) {
					if (l.hold != null) l.hold.invoke();
				}
			}
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		Enum virtualKey = getVirtualKey(keycode);
		updateVirtualKey(virtualKey, false);
		List<KeyListener> list = virtualKeyListeners.get(virtualKey);
		if (list != null) {
			for (KeyListener l : list) if (l.up != null) l.up.invoke();
		}
		return false;
	}

	public void actionDown(Enum virtualKey) {
		if(!virtualKeyListeners.containsKey(virtualKey)) return;
		virtualKeyListeners.get(virtualKey).forEach((listener) -> {if(listener.down != null) listener.down.invoke();});
		updateVirtualKey(virtualKey, true);
	}

	public void actionHold(Enum virtualKey) {
		if(!virtualKeyListeners.containsKey(virtualKey)) return;
		virtualKeyListeners.get(virtualKey).forEach((listener) -> {if(listener.hold != null) listener.hold.invoke();});
	}

	public void actionUp(Enum virtualKey) {
		if(!virtualKeyListeners.containsKey(virtualKey)) return;
		virtualKeyListeners.get(virtualKey).forEach((listener) -> {if(listener.up != null) listener.up.invoke();});
		updateVirtualKey(virtualKey, false);
	}
}
