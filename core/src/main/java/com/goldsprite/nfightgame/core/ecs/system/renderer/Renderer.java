package com.goldsprite.nfightgame.core.ecs.system.renderer;

import com.badlogic.gdx.graphics.Camera;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.system.System;

import java.util.ArrayList;
import java.util.List;

public class Renderer extends System implements IRenderer{
	protected final List<GObject> gobjects = new ArrayList<GObject>();

	@Override
	public void addGObject(GObject gobject) {
		gobjects.add(gobject);
	}

	@Override
	public void removeGObject(GObject gobject) {
		gobjects.remove(gobject);
	}
}
