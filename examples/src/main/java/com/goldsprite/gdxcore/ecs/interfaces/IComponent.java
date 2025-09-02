package com.goldsprite.gdxcore.ecs.interfaces;

import com.goldsprite.gdxcore.ecs.GObject;

public interface IComponent extends IRunnable {
	void update(float delta);

	GObject getGObject();

	void setGObject(GObject gObject);

	<T extends IComponent> T getComponent(Class<T> type);
	<T extends IComponent> T getComponent(Class<T> type, int index);
	<T extends IComponent> T addComponent(Class<T> clazz);
	<T extends IComponent> T addComponent(T component);


	void destroy();
	void destroyImmediate();
}
