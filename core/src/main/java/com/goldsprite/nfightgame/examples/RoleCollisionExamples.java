/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.examples;
import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.*;
import com.goldsprite.gdxcore.screens.*;
import com.goldsprite.nfightgame.core.ecs.*;
import com.goldsprite.nfightgame.core.ecs.component.*;
import com.goldsprite.nfightgame.core.ecs.system.PhysicsSystem;
import com.goldsprite.nfightgame.core.ecs.renderer.Gizmos;
import com.goldsprite.utils.math.*;

public class RoleCollisionExamples extends GScreen {
	private PhysicsSystem physics;
	private Gizmos gizmos;
	private GObject role, obj, obj2;
	private float speed = 1400, r = 50, r2 = 60;
	private Vector2 vel = new Vector2(), recSize = new Vector2(200, 200);

	@Override
	public void create() {
		createInputEvent();

		physics = new PhysicsSystem();

		gizmos = Gizmos.getInstance();

		role = new GObject();
		role.transform.setPosition(200, 300);

		RigidbodyComponent rigi = role.addComponent(new RigidbodyComponent());

		CircleColliderComponent collider = role.addComponent(new CircleColliderComponent());
		collider.setRadius(r);

		physics.addGObject(collider);


		obj = new GObject();
		obj.transform.setPosition(500, 300);
		RectColliderComponent collider2 = obj.addComponent(new RectColliderComponent());
		collider2.setSize(recSize.x, recSize.y);

		physics.addGObject(collider2);


		obj2 = new GObject();
		obj2.transform.setPosition(700, 300);
		CircleColliderComponent collider3 = obj2.addComponent(new CircleColliderComponent());
		collider3.setRadius(r2);

		physics.addGObject(collider3);
	}

	public void createInputEvent(){
		getImp().addProcessor(new InputAdapter(){
				public boolean touchDragged(int x, int y, int p){
					float vx = 1f*x/Gdx.graphics.getWidth()-0.5f;
					float vy = (1-1f*y/Gdx.graphics.getHeight())-0.5f;
					role.getComponent(RigidbodyComponent.class).getVelocity().set(vx, vy).scl(speed);
					return false;
				}
				public boolean touchUp(int x, int y, int p, int b){
					role.getComponent(RigidbodyComponent.class).getVelocity().scl(0);
					return false;
				}
			});
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.7f, 0.7f, 0.7f, 1f);

		//应用速度
		Vector2 velocity = role.getComponent(RigidbodyComponent.class).getVelocity();
		role.transform.getPosition().add(velocity.x*delta, velocity.y*delta);

		//计算物理
		physics.update(delta);

		//应用组件更新
		obj.update(delta);
		obj2.update(delta);
		role.update(delta);

		//调试线绘制器
//		gizmos.render(delta);
	}

}

