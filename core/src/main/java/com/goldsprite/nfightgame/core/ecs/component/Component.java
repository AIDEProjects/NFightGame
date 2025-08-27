package com.goldsprite.nfightgame.core.ecs.component;

import com.goldsprite.nfightgame.core.ecs.GObject;

public class Component implements IComponent{
	protected GObject gObject;
	protected TransformComponent transform;

	public Component() {
	}

	public void setGObject(GObject gObject) {
		this.gObject = gObject;
		transform = gObject.transform;
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void getGObject() {
	}

	@Override
	public <T extends IComponent> T getComponent(Class<T> type) {
		return gObject.getComponent(type);
	}

	public TransformComponent getTransform(){
		return transform;
	}
}
