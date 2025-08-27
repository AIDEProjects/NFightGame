package com.goldsprite.nfightgame.core.ecs.renderer;

import com.badlogic.gdx.graphics.Camera;
import com.goldsprite.nfightgame.core.ecs.GObject;

public interface IRenderer {
	void setCamera(Camera camera);

	void addGObject(GObject gObject);
	void removeGObject(GObject gObject);

	void render(float delta);
}
