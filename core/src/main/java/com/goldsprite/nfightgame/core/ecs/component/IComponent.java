package com.goldsprite.nfightgame.core.ecs.component;

import com.goldsprite.nfightgame.core.ecs.GObject;

public interface IComponent {
	void update(float delta);

	void getGObject();

	<T extends IComponent> T getComponent(Class<T> type);

	void setGObject(GObject gObject);
}
