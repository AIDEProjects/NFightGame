package com.goldsprite.nfightgame.core.ecs.system.renderer;

import com.badlogic.gdx.graphics.Camera;
import com.goldsprite.nfightgame.core.ecs.GObject;

public interface IRenderer {

	void addGObject(GObject gObject);
	void removeGObject(GObject gObject);
}
