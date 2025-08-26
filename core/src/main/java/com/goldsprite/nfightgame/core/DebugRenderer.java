package com.goldsprite.nfightgame.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.goldsprite.utils.math.Vector2;

public class DebugRenderer extends Renderer{
	private ShapeRenderer shapeRenderer;

	public DebugRenderer(){
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render(float delta) {
		shapeRenderer.setProjectionMatrix(camera.combined);
		for(GObject gobject : gobjects){
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(Color.YELLOW);
			CircleColliderComponent collComp = gobject.getComponent(CircleColliderComponent.class);
			shapeRenderer.circle(collComp.getCenter().x, collComp.getCenter().y, collComp.getRadius());
			shapeRenderer.end();
		}
	}
}
