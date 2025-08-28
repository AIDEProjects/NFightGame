/**
 * @Author
 * @AIDE AIDE+
 */
package com.goldsprite.nfightgame.core.ecs.component;

import com.goldsprite.utils.math.Vector2;

public class ColliderComponent extends Component {
	public Vector2 lastPos = new Vector2();
	protected boolean isEnabled = true;
	protected boolean showGizmos = true;
	protected boolean isCollision = false;
	protected boolean isTrigger = false;
	protected Vector2 centerPosition = new Vector2();
	protected Vector2 offsetPosition = new Vector2();

	public void setToLastPos() {
		transform.getPosition().set(lastPos);
	}

	public Vector2 getCenter() {
		return centerPosition.set(offsetPosition).scl(transform.getFace()).scl(transform.getScale())
				.add(transform.getPosition());
	}

	public void setOffsetPosition(float offsetX, float offsetY) {
		offsetPosition.set(offsetX, offsetY);
	}

	@Override
	public void update(float delta) {
		if (isEnabled && showGizmos)
			drawGizmos();
	}

	protected void drawGizmos() {
	}

	public boolean isCollision() {
		return isCollision;
	}

	public void setIsCollision(boolean isCollision) {
		this.isCollision = isCollision;
	}

	public boolean isTrigger() {
		return isTrigger;
	}

	public void setTrigger(boolean trigger) {
		isTrigger = trigger;
	}
	
	public void setEnable(boolean isEnable) {
		this.isEnabled = isEnable;
	}

	public boolean isEnable() {
		return isEnabled;
	}
}

