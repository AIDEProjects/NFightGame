/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.gdxcore.ecs.component;

import com.goldsprite.gdxcore.ecs.renderer.Gizmos;
import com.goldsprite.utils.math.*;
import com.badlogic.gdx.graphics.*;

public class RectColliderComponent extends ColliderComponent {
	public Rectangle rect = new Rectangle();
	private final Vector2 size = new Vector2();
	private final Vector2 sclSize = new Vector2();

	public Vector2 getOriSize(){
		return size;
	}
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
		Gizmos.rect(getCenter().x, getCenter().y, getSize().x, getSize().y);
	}

}

