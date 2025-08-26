package com.goldsprite.nfightgame.core;

import com.badlogic.gdx.graphics.Camera;

public interface IRenderer {
	void setCamera(Camera camera);

	void addGObject(GObject gObject);
	void removeGObject(GObject gObject);

	void render(float delta);
}
