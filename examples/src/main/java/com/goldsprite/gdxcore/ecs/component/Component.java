package com.goldsprite.gdxcore.ecs.component;

import com.goldsprite.gdxcore.ecs.GObject;
import com.goldsprite.gdxcore.ecs.GameSystem;
import com.goldsprite.gdxcore.ecs.interfaces.IComponent;

public class Component implements IComponent {
	protected GObject gObject;
	protected TransformComponent transform;
	protected boolean isEnabled = true;
	protected boolean showGizmos = true;

	public Component() {
	}

	public void setGObject(GObject gObject) {
		this.gObject = gObject;
		transform = gObject.transform;
	}

	@Override
	public void update(float delta) {
		if (isEnabled && showGizmos)
			drawGizmos();
	}

	@Override
	public GObject getGObject() {
		return gObject;
	}

	@Override
	public <T extends IComponent> T getComponent(Class<T> type) {
		return gObject.getComponent(type);
	}

	@Override
	public <T extends IComponent> T getComponent(Class<T> type, int index) {
		return gObject.getComponent(type, index);
	}

	public <T extends IComponent> T addComponent(Class<T> clazz) {
		return getGObject().addComponent(clazz);
	}
	public <T extends IComponent> T addComponent(T component) {
		return getGObject().addComponent(component);
	}

	public TransformComponent getTransform(){
		return transform;
	}

	protected void drawGizmos() {
	}

	public void setEnable(boolean isEnable) {
		this.isEnabled = isEnable;
	}

	public boolean isEnable() {
		return isEnabled;
	}

	@Override
	public void destroy() {
		GameSystem.getInstance().addDestroyComponent(this);
	}
	@Override
	public void destroyImmediate() {
		gObject.removeComponent(this);
	}

	@Override
	public String toString() {
		return gObject.getName();
	}
}
