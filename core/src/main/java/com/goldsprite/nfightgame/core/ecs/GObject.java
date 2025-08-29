package com.goldsprite.nfightgame.core.ecs;

import com.goldsprite.nfightgame.core.ecs.component.IComponent;
import com.goldsprite.nfightgame.core.ecs.component.TransformComponent;
import com.goldsprite.nfightgame.core.ecs.system.GameSystem;
import com.goldsprite.nfightgame.core.ecs.system.ManageMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GObject {
	public final TransformComponent transform;
	private final HashMap<Class<? extends IComponent>, List<IComponent>> components = new HashMap<>();

	public GObject() {
		addComponent(transform = new TransformComponent());

		GameSystem.manageGObject(this, ManageMode.ADD);
	}

	public <T extends IComponent> T addComponent(T component) {
		List<IComponent> list = components.get(component.getClass());
		if (list == null) list = new ArrayList<>();

		list.add(component);
		components.put(component.getClass(), list);
		component.setGObject(this);

		GameSystem.manageGameComponent(component, ManageMode.ADD);
		return (T) component;
	}

	public void removeComponent(IComponent component) {
		components.remove(component.getClass());

		GameSystem.manageGameComponent(component, ManageMode.REMOVE);
	}

	public boolean hasComponent(Class<? extends IComponent> type) {
		return components.containsKey(type);
	}

	public <T extends IComponent> T getComponent(Class<T> type) {
		return getComponent(type, 0);
	}

	public <T extends IComponent> T getComponent(Class<T> type, int index) {
		List<IComponent> list = components.get(type);
		if (list == null) return null;
		return (T) (Object) list.get(index);
	}

	public void update(float delta) {
		for (List<IComponent> list : components.values()) {
			for (IComponent component : list) {
				component.update(delta);
			}
		}
	}

}
