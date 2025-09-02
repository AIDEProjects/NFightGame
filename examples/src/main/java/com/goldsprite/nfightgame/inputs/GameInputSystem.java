package com.goldsprite.nfightgame.inputs;

public class GameInputSystem extends InputSystem {
	private static GameInputSystem instance;
	public static GameInputSystem getInstance() {
		if(instance == null) {
			instance = new GameInputSystem();
		}
		return instance;
	}

	private GameInputSystem() {

	}
}
