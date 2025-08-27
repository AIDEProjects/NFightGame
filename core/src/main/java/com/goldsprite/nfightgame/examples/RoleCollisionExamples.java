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
import com.goldsprite.nfightgame.core.ecs.renderer.*;
import com.goldsprite.utils.math.*;

public class RoleCollisionExamples extends GScreen {
	private Gizmos debugRenderer;
	private GObject role, obj;
	private float speed = 800, r = 50;
	private Vector2 vel = new Vector2(), recSize = new Vector2(200, 200);

	@Override
	public void create() {
		getImp().addProcessor(new InputAdapter(){
			public boolean touchDragged(int x, int y, int p){
				float vx = 1f*x/Gdx.graphics.getWidth()-0.5f;
				float vy = (1-1f*y/Gdx.graphics.getHeight())-0.5f;
				vel.set(vx, vy).scl(speed);
				return false;
			}
			public boolean touchUp(int x, int y, int p, int b){
				vel.scl(0);
				return false;
			}
		});

		debugRenderer = Gizmos.getInstance();
		debugRenderer.setCamera(getCamera());

		
		role = new GObject();
		role.transform.setPosition(200, 300);
		
		CircleColliderComponent collider = role.addComponent(new CircleColliderComponent());
		collider.setRadius(r);
		
		
		obj = new GObject();
		obj.transform.setPosition(500, 300);
		RectColliderComponent collider2 = obj.addComponent(new RectColliderComponent());
		collider2.setSize(recSize.x, recSize.y);
	}

	
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.7f, 0.7f, 0.7f, 1f);
		
		role.transform.getPosition().add(vel.x*delta, vel.y*delta);
		obj.act(delta);
		role.act(delta);
		//调试线绘制器
		debugRenderer.render(delta);
	}

}

