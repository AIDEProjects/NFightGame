package com.goldsprite.nfightgame.core;

public interface IComponent {
	void act(float delta);

	void getGObject();

	void setGObject(GObject gObject);
}
