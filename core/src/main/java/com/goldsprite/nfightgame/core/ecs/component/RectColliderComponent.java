/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core.ecs.component;
import com.goldsprite.utils.math.*;
import com.goldsprite.nfightgame.core.ecs.renderer.*;
import com.badlogic.gdx.graphics.*;

public class RectColliderComponent extends ColliderComponent {
	private Vector2 size = new Vector2();

	public void setSize(float sizeX, float sizeY) {
		this.size.set(sizeX, sizeY);
	}

	@Override
	public void drawGizmos() {
		Gizmos.setColor(Color.PURPLE);
		Gizmos.setHollow(false);
		Gizmos.rect(getCenter().x, getCenter().y, size.x, size.y);
	}

}

