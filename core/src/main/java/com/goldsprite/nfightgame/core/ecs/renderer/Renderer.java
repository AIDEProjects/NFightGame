package com.goldsprite.nfightgame.core.ecs.renderer;

import com.badlogic.gdx.graphics.Camera;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.TextureComponent;

import java.util.ArrayList;
import java.util.List;

public class Renderer implements IRenderer{
	protected Camera camera;
	protected final List<GObject> gobjects = new ArrayList<GObject>();

	@Override
	public void setCamera(Camera camera) {
		this.camera = camera;
	}


	@Override
	public void addGObject(GObject gobject) {
		if(gobject.hasComponent(TextureComponent.class))
			gobjects.add(gobject);
	}

	@Override
	public void removeGObject(GObject gobject) {
		gobjects.remove(gobject);
	}

	@Override
	public void render(float delta) {
	}
}
