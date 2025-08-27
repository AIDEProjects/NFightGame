/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.examples;
import com.goldsprite.gdxcore.screens.*;
import com.badlogic.gdx.*;
import com.goldsprite.nfightgame.core.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.MathUtils;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.CircleColliderComponent;
import com.goldsprite.nfightgame.core.ecs.system.PhysicsSystem;
import com.goldsprite.utils.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.goldsprite.infinityworld.assets.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class RoleCollisionExamples extends GScreen {
	private GObject role1;
	private GObject obj1;
	private PhysicsSystem physics;

	@Override
	public void create() {
		physics = new PhysicsSystem();


		role1 = new GObject();
		role1.transform.setPosition(200, 200);

		CircleColliderComponent circleColl = role1.addComponent(new CircleColliderComponent());
		circleColl.setRadius(50);

		physics.addGObject(role1);

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.7f, 0.7f, 0.7f, 1f);

	}

//	private boolean rectToRectCollision() {
//		//AABB
//		float minX = rx - rw / 2, maxX = rx + rw / 2, minY = ry - rh / 2, maxY = ry + rh / 2;
//		float min2X = r2x - r2w / 2, max2X = r2x + r2w / 2, min2Y = r2y - r2h / 2, max2Y = r2y + r2h / 2;
//		return !(minX > max2X || min2X > maxX || minY > max2Y || min2Y > maxY);
//	}
//
//	private boolean circleToCircleCollision() {
//		float disX = cx - c2x;
//		float disY = cy - c2y;
//		float distance = (float) Math.sqrt(disX * disX + disY * disY);
//
//		return distance < r + r2;
//	}
//
//	private boolean rectToCircleCollision() {
//		return circleToRectCollision();
//	}
//
//	private boolean circleToRectCollision() {
//		float closestX = MathUtils.clamp(cx, rx - rw / 2, rx + rw / 2);
//		float closestY = MathUtils.clamp(cy, ry - rh / 2, ry + rh / 2);
//
//		float distance = (float) Math.sqrt(Math.pow(cx - closestX, 2) + Math.pow(cy - closestY, 2));
//
//		return distance < r;
//	}

}

