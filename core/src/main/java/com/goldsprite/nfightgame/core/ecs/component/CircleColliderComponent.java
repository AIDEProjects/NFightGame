package com.goldsprite.nfightgame.core.ecs.component;

import com.goldsprite.nfightgame.core.ecs.renderer.*;
import com.badlogic.gdx.graphics.*;

public class CircleColliderComponent extends ColliderComponent{
	private float radius;
	@Override
	public void drawGizmos() {
		Gizmos.setColor(Color.YELLOW);
		Gizmos.setHollow(false);
		Gizmos.circle(getCenter().x, getCenter().y, getRadius());
	}

	public float getRadius() {
		return radius * transform.getScale().x;
	}

	public void setRadius(float r) {
		radius = r;
	}
}
