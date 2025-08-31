package com.goldsprite.gdxcore.ecs;

import com.goldsprite.gdxcore.ecs.enums.ManageMode;
import com.goldsprite.gdxcore.ecs.interfaces.IComponent;
import com.goldsprite.gdxcore.ecs.component.TransformComponent;

import java.util.*;

public class GObject {
	private boolean isDestroyed;
	public final TransformComponent transform;
	private final Map<Class<? extends IComponent>, List<IComponent>> components = new LinkedHashMap<>();

	public GObject() {
		addComponent(transform = new TransformComponent());

		GameSystem.manageGObject(this, ManageMode.ADD);
	}

	public <T extends IComponent> T addComponent(Class<T> clazz) {
		try {
			T comp = clazz.getConstructor().newInstance();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	public <T extends IComponent> T addComponent(T component) {
		List<IComponent> list = components.get(component.getClass());
		if (list == null) list = new ArrayList<>();

		list.add(component);
		components.put(component.getClass(), list);
		component.setGObject(this);

		GameSystem.manageGameComponent(component, ManageMode.ADD);
		return component;
	}

	public void removeComponent(IComponent component) {
		//从系统中移除
		GameSystem.manageGameComponent(component, ManageMode.REMOVE);
		//从游戏物体自身中移除
		List<IComponent> list = components.get(component.getClass());
		if(list != null) {
			list.remove(component);
		}
	}

	public boolean hasComponent(Class<? extends IComponent> type) {
		return components.containsKey(type);
	}

	public <T extends IComponent> T getComponent(Class<T> type) {
		return getComponent(type, 0);
	}

	public <T extends IComponent> T getComponent(Class<T> type, int index) {
		List<IComponent> list = components.get(type);
		//找到同类型直接返回
		if (list != null) return (T) list.get(index);
		else {
			for (Class<? extends IComponent> k : components.keySet()) {
				List<IComponent> v = components.get(k);
				//否则查找子类返回
				if (type.isAssignableFrom(k)) {
					return (T) v.get(index);
				}
			}
		}
		//找不到返回空
		return null;
	}

	public void update(float delta) {
		for (List<IComponent> list : components.values()) {
			for (IComponent component : list) {
				component.update(delta);
			}
		}
	}

	public void destroy() {
		isDestroyed = true;
		GameSystem.getInstance().addDestroyGObject(this);
	}
	public void destroyImmediate() {
		for (List<IComponent> list : components.values()) {
			for(int i = list.size() - 1; i >= 0; i--) {
				IComponent component = list.get(i);
				component.destroyImmediate();//立即销毁组件并从自身移除
			}
		}
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}
}
