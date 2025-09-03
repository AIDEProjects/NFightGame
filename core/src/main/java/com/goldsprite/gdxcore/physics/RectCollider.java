package com.goldsprite.gdxcore.physics;

import com.goldsprite.utils.math.Rectangle;
import com.goldsprite.utils.math.Vector2;

public class RectCollider {
	public Vector2 velocity = new Vector2();
	public Rectangle bound = new Rectangle();
	public CollInfo collInfo = new CollInfo();

	public RectCollider() {
	}

	Vector2 tmpVec = new Vector2();
	static Vector2 velBack = new Vector2();
	static Rectangle projectedRect = new Rectangle();
	public void resolveCollision(RectCollider collB, float delta) {
		RectCollider collA = this;
		Rectangle rectA = collA.bound;
		Rectangle rectB = collB.bound;
		Vector2 velocity = tmpVec.set(collA.velocity).scl(delta);//单帧速度
		float k = velocity.y / velocity.x;//斜率

		CollInfo collInfo = collA.collInfo.reset();
		collInfo.newVelocity.set(collA.velocity);
		projectedRect.setHalfSize(rectA.halfSize.x, rectA.halfSize.y);

//		if(k == 0 || Float.isInfinite(k)) return;

		//y轴碰撞
		if (velocity.y != 0) {
			float depY = velocity.y > 0
				? rectB.bottomY() - rectA.topY()
				: rectB.topY() - rectA.bottomY();
			if(Math.signum(velocity.y) != Math.signum(depY)){
				float mappingX = depY / k;
				//获取回退量
				velBack.set(mappingX, depY);
				//创建y轴假想碰撞点矩形
				projectedRect.center.set(rectA.center.x + velBack.x, rectA.center.y + velBack.y);
				if (projectedRect.checkAABBCollision(rectB)) {
					collA.velocity.y = 0;
					collInfo.isCollision = true;
					collInfo.normalDirection.set(velocity.y > 0 ? Vector2.down : Vector2.up);
					collInfo.newVelocity.set(velocity).
						add(velBack.y).
						div(delta);
					rectA.center.y += velBack.y;
					return;
				}
			}
		}
		//x轴碰撞
		if (velocity.x != 0) {
			float depX = velocity.x > 0
				? rectB.leftX() - rectA.rightX()
				: rectB.rightX() - rectA.leftX();
			if(Math.signum(velocity.x) != Math.signum(depX)) {
				float mappingY = depX * k;
				//获取回退量
				velBack.set(depX, mappingY);
				//创建y轴假想碰撞点矩形
				projectedRect.center.set(rectA.center.x + velBack.x, rectA.center.y + velBack.y);
				if (projectedRect.checkAABBCollision(rectB)) {
					collA.velocity.x = 0;
					collInfo.isCollision = true;
					collInfo.normalDirection.set(velocity.x > 0 ? Vector2.left : Vector2.right);
					collInfo.newVelocity.set(velocity).
						add(velBack.y).
						div(delta);
					rectA.center.x += velBack.x;
					return;
				}
			}
		}
	}

}
