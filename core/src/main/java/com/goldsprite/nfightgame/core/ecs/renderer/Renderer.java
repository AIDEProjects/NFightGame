package com.goldsprite.nfightgame.core.ecs.renderer;

import com.goldsprite.nfightgame.core.ecs.system.System;

import java.util.ArrayList;
import java.util.List;
import com.goldsprite.nfightgame.core.ecs.component.*;

public class Renderer extends System implements IRenderer{
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
