package com.goldsprite.nfightgame.core.ecs.component;

import com.goldsprite.nfightgame.core.ecs.GObject;

public interface IComponent {
	void act(float delta);

	void getGObject();

	void setGObject(GObject gObject);
}
