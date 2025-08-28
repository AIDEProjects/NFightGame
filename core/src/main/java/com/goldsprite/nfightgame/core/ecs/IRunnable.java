package com.goldsprite.nfightgame.core.ecs;

public interface IRunnable {
	void update(float delta);
	void fixedUpdate(float fixedDelta);
}
