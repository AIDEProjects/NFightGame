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

public class RoleCollisionExamples extends GScreen {
	private DebugRenderer debugRenderer;
	private GObject role;

	@Override
	public void create() {
		getImp().addProcessor(new InputAdapter(){
			public boolean touchDown(int x, int y, int p, int b){
				float viewWidth = Gdx.graphics.getWidth();
				return false;
			}
		});

		debugRenderer = new DebugRenderer();
		debugRenderer.setCamera(getCamera());

		role = new GObject();
		role.transform.setPosition(200, 200);
		
		CircleColliderComponent collComp = role.addComponent(new CircleColliderComponent());
		collComp.setRadius(20);
		debugRenderer.addGObject(role);
	}

	
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.7f, 0.7f, 0.7f, 1f);

		//调试线绘制器
		debugRenderer.render(delta);
	}

}

