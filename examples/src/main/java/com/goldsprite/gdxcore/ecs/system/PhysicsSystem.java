package com.goldsprite.gdxcore.ecs.system;

import com.badlogic.gdx.math.MathUtils;
import com.goldsprite.gdxcore.ecs.GObject;
import com.goldsprite.gdxcore.ecs.component.CircleColliderComponent;
import com.goldsprite.gdxcore.ecs.component.ColliderComponent;
import com.goldsprite.gdxcore.ecs.component.RectColliderComponent;
import com.goldsprite.gdxcore.ecs.component.RigidbodyComponent;
import com.goldsprite.utils.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSystem extends System {
	public static float GRAVITY = 9.81f;
	private static final float gravity_scale = 6.25f;//2

	protected final List<GObject> gObjects = new ArrayList<GObject>();
	protected final List<ColliderComponent> colliders = new ArrayList<ColliderComponent>();
	Vector2 lastPosition = new Vector2();

	public void addGObject(ColliderComponent collider) {
		colliders.add(collider);
		if(!gObjects.contains(collider.getGObject()))gObjects.add(collider.getGObject());
	}

	public void removeGObject(ColliderComponent collider) {
		colliders.remove(collider);
		//检测该所属gobject已无物理组件则将其移除
		boolean remove = true;
		for(ColliderComponent c : colliders) {
			if(c.getGObject().equals(collider.getGObject())){
				remove = false;
				break;
			}
		}
		if(remove) gObjects.remove(collider.getGObject());
	}

	public void update(float delta) {
		if (!isEnabled())
			return;

		//使用固定步长的物理重力
		accumulator += delta;
		if(accumulator >= fixedTimeStep){
			updateGravity(fixedTimeStep);
			accumulator -= fixedTimeStep;
		}

		//碰撞检测
		for (ColliderComponent c1 : colliders) {
			RigidbodyComponent rigi1 = c1.getComponent(RigidbodyComponent.class);
			if (rigi1 == null)
				continue;

			boolean isColl = false;

			if (c1.isTrigger()) {
				isColl = checkOtherCollision(c1);
			} else {
				Vector2 pos1 = c1.getTransform().getPosition();
				Vector2 velocity = rigi1.getVelocity();
				lastPosition.set(pos1);

				//分轴碰撞检测
				pos1.x += velocity.x * delta;
				if (checkOtherCollision(c1)) {
					isColl = true;
					pos1.setX(lastPosition.x);
					velocity.x = 0;
				}
				pos1.y += velocity.y * delta;
				if (checkOtherCollision(c1)) {
					isColl = true;
					pos1.setY(lastPosition.y);
					velocity.y = 0;
				}
				pos1.set(lastPosition);
			}
			c1.setIsCollision(isColl);
		}

		//速度结算
		for(GObject obj : gObjects) {
			RigidbodyComponent rigi1 = obj.getComponent(RigidbodyComponent.class);
			if (rigi1 == null) continue;
			Vector2 vel = rigi1.getVelocity();
			obj.transform.getPosition().add(vel.x * delta, vel.y * delta);
		}

	}

	float accumulator = 0;
	float fixedTimeStep = 1/60f;
	private void updateGravity(float fixedDelta) {
		for(GObject obj : gObjects){
			RigidbodyComponent rigi1 = obj.getComponent(RigidbodyComponent.class);
			if (rigi1 == null) continue;
			Vector2 velocity = rigi1.getVelocity();
			// 计算重力
			velocity.y -= GRAVITY * gravity_scale * fixedDelta * 60;
		}
	}

	private boolean checkOtherCollision(ColliderComponent c1) {
		for (ColliderComponent c2 : colliders) {
			if (c2 == c1)
				continue;
			if (c2.getGObject() == c1.getGObject())
				continue;
			if (!c2.isEnable()) {
				if (c1.isCollisingTarget(c2)) {
					if (c2.isTrigger())
						c2.onTriggerExit(c1);
					c1.removeCollisingTarget(c2);
				}
				continue;
			}

			boolean collied = checkCollision(c1, c2);
			if (collied && !c1.isCollisingTarget(c2)) {
				c1.addCollisingTarget(c2);
				if (c2.isTrigger())
					c2.onTriggerEnter(c1);
			}
			if (!collied && c1.isCollisingTarget(c2)) {
				if (c2.isTrigger())
					c2.onTriggerExit(c1);
				c1.removeCollisingTarget(c2);
			}

			if (c2.isTrigger())
				continue;
			if (collied)
				return true;
		}
		return false;
	}

	private boolean checkCollision(ColliderComponent c1, ColliderComponent c2) {
		if (circleToCircleCollision(c1, c2))
			return true;
		if (circleToRectCollision(c1, c2))
			return true;
		if (rectToCircleCollision(c1, c2))
			return true;
		return rectToRectCollision(c1, c2);
	}

	private boolean circleToCircleCollision(ColliderComponent c1, ColliderComponent c2) {
		if (!(c1 instanceof CircleColliderComponent && c2 instanceof CircleColliderComponent))
			return false;
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
		if (!(c1 instanceof RectColliderComponent && c2 instanceof CircleColliderComponent))
			return false;
		return circleToRectCollision(c2, c1);
	}

	private boolean circleToRectCollision(ColliderComponent c1, ColliderComponent c2) {
		if (!(c1 instanceof CircleColliderComponent && c2 instanceof RectColliderComponent))
			return false;
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
		if (!(c1 instanceof RectColliderComponent && c2 instanceof RectColliderComponent))
			return false;
		float rx = c1.getCenter().x;
		float ry = c1.getCenter().y;
		float rw = ((RectColliderComponent) c1).getSize().x;
		float rh = ((RectColliderComponent) c1).getSize().y;
		float r2x = c2.getCenter().x;
		float r2y = c2.getCenter().y;
		float r2w = ((RectColliderComponent) c2).getSize().x;
		float r2h = ((RectColliderComponent) c2).getSize().y;

		//AABB
		float minX = rx - rw / 2, maxX = rx + rw / 2, minY = ry - rh / 2, maxY = ry + rh / 2;
		float min2X = r2x - r2w / 2, max2X = r2x + r2w / 2, min2Y = r2y - r2h / 2, max2Y = r2y + r2h / 2;
		return !(minX > max2X || min2X > maxX || minY > max2Y || min2Y > maxY);
	}
}

