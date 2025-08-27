package com.goldsprite.nfightgame.core.ecs.system;

import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.TextureComponent;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSystem {
	protected final List<GObject> gobjects = new ArrayList<GObject>();

	public void addGObject(GObject gobject) {
		if(gobject.hasComponent(TextureComponent.class))
			gobjects.add(gobject);
	}

	public void removeGObject(GObject gobject) {
		gobjects.remove(gobject);
	}

	public void update(float delta) {
		for(GObject gobject : gobjects) {

		}
	}
}
