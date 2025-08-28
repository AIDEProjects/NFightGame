package com.goldsprite.nfightgame.core.ecs.system;

import com.badlogic.gdx.math.MathUtils;
import com.goldsprite.nfightgame.core.ecs.component.CircleColliderComponent;
import com.goldsprite.nfightgame.core.ecs.component.ColliderComponent;
import com.goldsprite.nfightgame.core.ecs.component.RectColliderComponent;
import com.goldsprite.nfightgame.core.ecs.component.RigidbodyComponent;
import com.goldsprite.utils.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSystem {
	public static float GRAVITY = 9.81f;

	protected final List<ColliderComponent> colliders = new ArrayList<ColliderComponent>();
	Vector2 lastPosition = new Vector2();
	Vector2 tempVec = new Vector2();

	public void addGObject(ColliderComponent collider) {
		colliders.add(collider);
	}

	public void removeGObject(ColliderComponent collider) {
		colliders.remove(collider);
	}

	public void update(float delta) {
		for (ColliderComponent c1 : colliders) {
			RigidbodyComponent rigi1 = c1.getComponent(RigidbodyComponent.class);
			if (rigi1 == null) continue;

			boolean isColl = false;

			if(c1.isTrigger()){
				isColl = checkCollision(c1);
			}else{
				Vector2 pos1 = c1.getTransform().getPosition();
				Vector2 velocity = rigi1.getVelocity();

				// 计算重力
				velocity.y -= GRAVITY * 3f;

				//分轴碰撞检测
				float oldX = pos1.x;
				pos1.x += velocity.x * delta;
				if (checkCollision(c1)) {
					isColl = true;
					pos1.x = oldX;
					velocity.x = 0;
				}
				float oldY = pos1.y;
				pos1.y += velocity.y * delta;
				if (checkCollision(c1)) {
					isColl = true;
					pos1.y = oldY; 
					velocity.y = 0;
				}
			}
			c1.setIsCollision(isColl);
		}
	}

	private boolean checkCollision(ColliderComponent c1) {
		for (ColliderComponent c2 : colliders) {
			if (c2 == c1) continue;
			if (c2.getGObject() == c1.getGObject()) continue;

			if (circleToCircleCollision(c1, c2)) return true;
			if (circleToRectCollision(c1, c2)) return true;
			if (rectToCircleCollision(c1, c2)) return true;
		}
		return false;
	}

	private boolean circleToCircleCollision(ColliderComponent c1, ColliderComponent c2) {
		if (!(c1 instanceof CircleColliderComponent && c2 instanceof CircleColliderComponent)) return false;
		float cx = c1.getCenter().x;
		float cy = c1.getCenter().y;
		float r = ((CircleColliderComponent) c1).getRadius();
		float c2x = c2.getCenter().x;
		float c2y = c2.getCenter().y;
		float r2 = ((CircleColliderComponent) c2).getRadius();

		float disX = cx - c2x;
		float disY = cy - c2y;
		float distance = (float) Math.sqrt(disX * disX + disY * disY);

		return distance < r + r2;
	}

	private boolean rectToCircleCollision(ColliderComponent c1, ColliderComponent c2) {
		if (!(c1 instanceof RectColliderComponent && c2 instanceof CircleColliderComponent)) return false;
		return circleToRectCollision(c2, c1);
	}

	private boolean circleToRectCollision(ColliderComponent c1, ColliderComponent c2) {
		if (!(c1 instanceof CircleColliderComponent && c2 instanceof RectColliderComponent)) return false;
		float cx = c1.getCenter().x;
		float cy = c1.getCenter().y;
		float r = ((CircleColliderComponent) c1).getRadius();
		float rx = c2.getCenter().x;
		float ry = c2.getCenter().y;
		float rw = ((RectColliderComponent) c2).getSize().x;
		float rh = ((RectColliderComponent) c2).getSize().y;

		float closestX = MathUtils.clamp(cx, rx - rw / 2, rx + rw / 2);
		float closestY = MathUtils.clamp(cy, ry - rh / 2, ry + rh / 2);
		float distance = (float) Math.sqrt(Math.pow(cx - closestX, 2) + Math.pow(cy - closestY, 2));

		return distance < r;
	}

	private boolean rectToRectCollision(ColliderComponent c1, ColliderComponent c2) {
		if (!(c1 instanceof RectColliderComponent && c2 instanceof RectColliderComponent)) return false;

		return false;
	}
}

