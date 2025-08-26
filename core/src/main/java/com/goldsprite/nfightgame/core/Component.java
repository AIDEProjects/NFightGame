package com.goldsprite.nfightgame.core;

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
	public void act(float delta) {
	}

	@Override
	public void getGObject() {
	}
}
