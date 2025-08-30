package com.goldsprite.nfightgame.core.ecs.renderer;

import com.badlogic.gdx.graphics.Camera;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.*;

public interface IRenderer {

	void addGObject(IComponent gObject);
	void removeGObject(IComponent gObject);
}
