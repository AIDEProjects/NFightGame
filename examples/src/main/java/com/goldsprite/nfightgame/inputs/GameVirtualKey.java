package com.goldsprite.nfightgame.inputs;

import com.badlogic.gdx.Input;

import java.util.HashMap;
import java.util.Map;

public enum GameVirtualKey implements IVirtualKey {
	UP, DOWN, LEFT, RIGHT, ATTACK, JUMP;
	private static final Map<Integer, GameVirtualKey> keyboardMap = new HashMap<Integer, GameVirtualKey>(){{
		//WASD
		put(Input.Keys.W, GameVirtualKey.UP);
		put(Input.Keys.S, GameVirtualKey.DOWN);
		put(Input.Keys.A, GameVirtualKey.LEFT);
		put(Input.Keys.D, GameVirtualKey.RIGHT);
		//↑↓←→
		put(Input.Keys.UP, GameVirtualKey.UP);
		put(Input.Keys.DOWN, GameVirtualKey.DOWN);
		put(Input.Keys.LEFT, GameVirtualKey.LEFT);
		put(Input.Keys.RIGHT, GameVirtualKey.RIGHT);

		put(Input.Keys.J, GameVirtualKey.ATTACK);
		put(Input.Keys.K, GameVirtualKey.JUMP);
	}};

	@Override
	public GameVirtualKey mapping(int keycode) {
		return keyboardMap.get(keycode);
	}
}
