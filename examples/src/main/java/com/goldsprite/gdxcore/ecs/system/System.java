package com.goldsprite.gdxcore.ecs.system;

import com.goldsprite.gdxcore.ecs.GameSystem;
import com.goldsprite.gdxcore.ecs.interfaces.ISystem;

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
