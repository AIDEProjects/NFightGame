/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core.ecs.component;

import com.goldsprite.nfightgame.core.ecs.system.renderer.Gizmos;
import com.goldsprite.utils.math.*;
import com.badlogic.gdx.graphics.*;

public class RectColliderComponent extends ColliderComponent {
	private Vector2 size = new Vector2();
	private Vector2 sclSize = new Vector2();

	public Vector2 getSize(){
		return this.sclSize.set(size).scl(transform.getScale());
	}

	public void setSize(float sizeX, float sizeY) {
		this.size.set(sizeX, sizeY);
	}

	@Override
	public void drawGizmos() {
		Gizmos.setColor(!isCollision? Color.GREEN: Color.RED);
		Gizmos.setHollow(true);
		Gizmos.rect(getCenter().x, getCenter().y, size.x, size.y);
	}

}

