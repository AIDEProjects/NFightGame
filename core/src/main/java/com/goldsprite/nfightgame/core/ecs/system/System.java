package com.goldsprite.nfightgame.core.ecs.system;

import com.goldsprite.nfightgame.core.ecs.GameSystem;

public class System implements ISystem {
	protected GameSystem gm;

	private boolean isEnabled = true;

	public System(){
		gm = GameSystem.getInstance();
	}

	@Override
	public void update(float delta) {
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}
}
