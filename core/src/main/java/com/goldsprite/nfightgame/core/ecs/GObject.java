package com.goldsprite.nfightgame.core.ecs;

import com.goldsprite.nfightgame.core.ecs.component.IComponent;
import com.goldsprite.nfightgame.core.ecs.component.TransformComponent;

import java.util.HashMap;

public class GObject {
	private final HashMap<Class<? extends IComponent>, IComponent> components = new HashMap<>();
	public final TransformComponent transform;

	public GObject(){
		addComponent(transform = new TransformComponent());
	}

	public <T extends IComponent> T addComponent(T component){
		if(!components.containsKey(component.getClass())){
			components.put(component.getClass(), component);
			component.setGObject(this);
		}
		return (T)component;
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

	public void act(float delta){
		for(IComponent component : components.values()){
			component.act(delta);
		}
	}
}
