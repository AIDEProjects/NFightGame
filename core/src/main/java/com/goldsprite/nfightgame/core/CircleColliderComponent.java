package com.goldsprite.nfightgame.core;

import com.goldsprite.utils.math.Vector2;

public class CircleColliderComponent extends Component{
	private Vector2 centerPosition = new Vector2();
	private Vector2 offsetPosition = new Vector2();
	private float radius;

	@Override
	public void act(float delta) {

	}

	public Vector2 getCenter() {
		return centerPosition.set(offsetPosition).scl(transform.getFace()).scl(transform.getScale()).add(transform.getPosition());
	}

	public float getRadius() {
		return radius * transform.getScale().x;
	}

	public void setRadius(float r) {
		radius = r;
	}

	public void setOffsetPosition(float offsetX, float offsetY) {
		offsetPosition.set(offsetX, offsetY);
	}
}
