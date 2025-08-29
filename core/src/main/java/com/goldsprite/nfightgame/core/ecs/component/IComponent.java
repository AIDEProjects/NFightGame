package com.goldsprite.nfightgame.core.ecs.component;

import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.IRunnable;

public interface IComponent extends IRunnable {
	void update(float delta);

	GObject getGObject();

	void setGObject(GObject gObject);

	<T extends IComponent> T getComponent(Class<T> type);


	<T extends IComponent> T getComponent(Class<T> type, int index);
}
