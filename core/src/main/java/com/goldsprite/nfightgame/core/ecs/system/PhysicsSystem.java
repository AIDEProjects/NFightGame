package com.goldsprite.nfightgame.core.ecs.system;

import com.badlogic.gdx.Gdx;
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

			Vector2 pos1 = c1.getTransform().getPosition();
			//记录之前位置
			lastPosition.set(pos1);
			//计算重力
			rigi1.getVelocity().setY(rigi1.getVelocity().getY() - GRAVITY * 0.2f);
			//应用速度
			Vector2 stepVel = tempVec.set(rigi1.getVelocity()).scl(delta);
			pos1.add(stepVel);
			//重置碰撞状态
			c1.setIsCollision(false);
			//检测碰撞
			for (ColliderComponent c2 : colliders) {
				if (c2 == c1)
					continue;
				if (circleToCircleCollision(c1, c2)) {
					c1.setIsCollision(true);
					break;
				}
				if (circleToRectCollision(c1, c2)) {
					c1.setIsCollision(true);
					break;
				}
				if (rectToCircleCollision(c1, c2)) {
					c1.setIsCollision(true);
					break;
				}
			}
			//取消应用速度
			if (c1.isCollision()) {
				pos1.set(lastPosition);
				rigi1.getVelocity().scl(0);
			}
		}
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

