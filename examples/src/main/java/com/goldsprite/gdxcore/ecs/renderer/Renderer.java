package com.goldsprite.gdxcore.ecs.renderer;

import com.goldsprite.gdxcore.ecs.interfaces.IComponent;
import com.goldsprite.gdxcore.ecs.interfaces.IRenderer;
import com.goldsprite.gdxcore.ecs.system.System;

import java.util.ArrayList;
import java.util.List;

public class Renderer extends System implements IRenderer {
	protected final List<IComponent> gobjects = new ArrayList<>();

	@Override
	public void addGObject(IComponent gobject) {
		gobjects.add(gobject);
	}

	@Override
	public void removeGObject(IComponent gobject) {
		gobjects.remove(gobject);
	}
}
