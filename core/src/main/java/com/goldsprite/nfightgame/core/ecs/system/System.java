package com.goldsprite.nfightgame.core.ecs.system;

public class System implements ISystem {
	protected GameSystem gm;

	public System(){
		gm = GameSystem.getInstance();
	}

	@Override
	public void update(float delta) {
	}
}
