package com.goldsprite.nfightgame.core.ecs.system;

import java.util.ArrayList;
import java.util.List;
import com.goldsprite.nfightgame.core.ecs.component.*;
import com.badlogic.gdx.math.*;

public class PhysicsSystem {
	protected final List<ColliderComponent> colliders = new ArrayList<ColliderComponent>();

	public void addGObject(ColliderComponent collider) {
		colliders.add(collider);
	}

	public void removeGObject(ColliderComponent collider) {
		colliders.remove(collider);
	}

	public void update(float delta) {
		for (ColliderComponent c1 : colliders) {
			c1.setIsCollision(false);
			for (ColliderComponent c2 : colliders) {
				if (c2 == c1)
					continue;
				if (circleToCircleCollision(c1, c2)){
					c1.setIsCollision(true);
					break;
				}
				if (circleToRectCollision(c1, c2)){
					c1.setIsCollision(true);
					break;
				}
				if (rectToCircleCollision(c1, c2)){
					c1.setIsCollision(true);
					break;
				 }
			}
			if(c1.isCollision()){
				c1.setToLastPos();
			}else{
				c1.lastPos.set(c1.getTransform().getPosition());
			}
		}
	}

	private boolean circleToCircleCollision(ColliderComponent c1, ColliderComponent c2) {
		if(!(c1 instanceof CircleColliderComponent && c2 instanceof CircleColliderComponent)) return false;
		float cx = c1.getCenter().x;
		float cy = c1.getCenter().y;
		float r = ((CircleColliderComponent)c1).getRadius();
		float c2x = c2.getCenter().x;
		float c2y = c2.getCenter().y;
		float r2 = ((CircleColliderComponent)c2).getRadius();
		
		float disX = cx - c2x;
		float disY = cy - c2y;
		float distance = (float) Math.sqrt(disX * disX + disY * disY);
		
		return distance < r + r2;
	}

	private boolean rectToCircleCollision(ColliderComponent c1, ColliderComponent c2) {
		if(!(c1 instanceof RectColliderComponent && c2 instanceof CircleColliderComponent)) return false;
		return circleToRectCollision(c2, c1);
	}
	private boolean circleToRectCollision(ColliderComponent c1, ColliderComponent c2) {
		if(!(c1 instanceof CircleColliderComponent && c2 instanceof RectColliderComponent)) return false;
		float cx = c1.getCenter().x;
		float cy = c1.getCenter().y;
		float r = ((CircleColliderComponent)c1).getRadius();
		float rx = c2.getCenter().x;
		float ry = c2.getCenter().y;
		float rw = ((RectColliderComponent)c2).getSize().x;
		float rh = ((RectColliderComponent)c2).getSize().y;
		
		float closestX = MathUtils.clamp(cx, rx - rw / 2, rx + rw / 2);
		float closestY = MathUtils.clamp(cy, ry - rh / 2, ry + rh / 2);
		float distance = (float) Math.sqrt(Math.pow(cx - closestX, 2) + Math.pow(cy - closestY, 2));

		return distance < r;
	}

	private boolean rectToRectCollision(ColliderComponent c1, ColliderComponent c2) {
		if(!(c1 instanceof RectColliderComponent && c2 instanceof RectColliderComponent)) return false;

		return false;
	}
}

