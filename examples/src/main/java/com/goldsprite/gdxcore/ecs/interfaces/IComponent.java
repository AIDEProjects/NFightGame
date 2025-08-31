package com.goldsprite.gdxcore.ecs.interfaces;

import com.goldsprite.gdxcore.ecs.GObject;

public interface IComponent extends IRunnable {
	void update(float delta);

	GObject getGObject();

	void setGObject(GObject gObject);

	<T extends IComponent> T getComponent(Class<T> type);


	<T extends IComponent> T getComponent(Class<T> type, int index);

	void destroy();
	void destroyImmediate();
}
