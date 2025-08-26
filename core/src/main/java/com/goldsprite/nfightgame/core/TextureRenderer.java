package com.goldsprite.nfightgame.core;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goldsprite.utils.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class TextureRenderer {
	private final List<GObject> gobjects = new ArrayList<GObject>();
	private final SpriteBatch batch;
	private Camera camera;

	public TextureRenderer(){
		batch = new SpriteBatch();
	}

	public void addGObject(GObject gobject) {
		if(gobject.hasComponent(TextureComponent.class))
			gobjects.add(gobject);
	}

	public void removeGObject(GObject gobject) {
		gobjects.remove(gobject);
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public void draw(float delta){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(GObject gobject : gobjects){
			TextureComponent texComp = gobject.getComponent(TextureComponent.class);
			TextureRegion region = texComp.getRegion();
			texComp.updateRegionFlip();
			Vector2 leftDownPos = texComp.getLeftDownPos();
			Vector2 renderSize = texComp.getRenderSize();
			batch.draw(region, leftDownPos.x, leftDownPos.y, renderSize.x, renderSize.y);
		}
		batch.end();
	}
}
