/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core.ecs.component;

import com.goldsprite.utils.math.*;

public class ColliderComponent extends Component {
	protected boolean showGizmos = true;
	protected boolean isCollision = false;
	protected Vector2 centerPosition = new Vector2();
	protected Vector2 offsetPosition = new Vector2();
	public Vector2 lastPos = new Vector2();

	public void setToLastPos(){
		transform.getPosition().set(lastPos);
	}

	public Vector2 getCenter() {
		return centerPosition.set(offsetPosition).scl(transform.getFace()).scl(transform.getScale())
				.add(transform.getPosition());
	}

	public void setOffsetPosition(float offsetX, float offsetY) {
		offsetPosition.set(offsetX, offsetY);
	}

	public void setIsCollision(boolean isCollision) {
		this.isCollision = isCollision;
	}

	public boolean isCollision() {
		return isCollision;
	}

	@Override
	public void update(float delta) {
		if (showGizmos)
			drawGizmos();
	}

	protected void drawGizmos() {
	}

}

