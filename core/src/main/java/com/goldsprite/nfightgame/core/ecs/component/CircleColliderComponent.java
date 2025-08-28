package com.goldsprite.nfightgame.core.ecs.component;

import com.badlogic.gdx.graphics.*;
import com.goldsprite.nfightgame.core.ecs.system.renderer.Gizmos;

public class CircleColliderComponent extends ColliderComponent{
	private float radius;
	@Override
	public void drawGizmos() {
		Gizmos.setColor(!isCollision? Color.GREEN: Color.RED);
		Gizmos.setHollow(true);
		Gizmos.circle(getCenter().x, getCenter().y, getRadius());
	}

	public float getRadius() {
		return radius * transform.getScale().x;
	}

	public void setRadius(float r) {
		radius = r;
	}
}
