package com.goldsprite.nfightgame.core.ecs.component;

import com.goldsprite.utils.math.Vector2;

public class RigidbodyComponent extends Component {
	private Vector2 velocity = new Vector2();
	private boolean gravity = true;

	public Vector2 getVelocity() {
		return this.velocity;
	}

	public boolean isGravity() {
		return gravity;
	}

	public void setGravity(boolean gravity) {
		this.gravity = gravity;
	}
}
