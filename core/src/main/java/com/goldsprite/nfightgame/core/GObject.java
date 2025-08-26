package com.goldsprite.nfightgame.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GObject {
	private final HashMap<Class<? extends IComponent>, IComponent> components = new HashMap<>();
	public void addComponent(IComponent component){
		if(!components.containsKey(component.getClass())){
			components.put(component.getClass(), component);
		}
	}

	public void removeComponent(IComponent component){
		components.remove(component.getClass());
	}

	public boolean hasComponent(Class<? extends IComponent> type) {
		return components.containsKey(type);
	}

	public <T extends IComponent> T getComponent(Class<T> type) {
		return (T)(Object)components.get(type);
	}
}
