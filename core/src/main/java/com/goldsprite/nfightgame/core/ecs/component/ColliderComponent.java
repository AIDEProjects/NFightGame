/**
 * @Author
 * @AIDE AIDE+
 */
package com.goldsprite.nfightgame.core.ecs.component;

import com.goldsprite.utils.math.Vector2;
import java.util.*;
import java.util.function.*;

public class ColliderComponent extends Component {
	protected boolean isCollision = false;
	protected boolean isTrigger = false;
	protected Vector2 centerPosition = new Vector2();
	protected Vector2 offsetPosition = new Vector2();
	protected List<ColliderComponent> collisingTargets = new ArrayList<>();
	public final List<Consumer<ColliderComponent>> onTriggerEnterListeners = new ArrayList<>();
	public final List<Consumer<ColliderComponent>> onTriggerExitListeners = new ArrayList<>();

	public void onTriggerEnter(ColliderComponent c2) {
		onTriggerEnterListeners.forEach(c -> c.accept(c2));
	}

	public void onTriggerExit(ColliderComponent c2) {
		onTriggerExitListeners.forEach(c -> c.accept(c2));
	}

	public Vector2 getCenter() {
		return centerPosition.set(offsetPosition).scl(transform.getFace()).scl(transform.getScale())
				.add(transform.getPosition());
	}

	public void setOffsetPosition(float offsetX, float offsetY) {
		offsetPosition.set(offsetX, offsetY);
	}

	public boolean isCollision() {
		return isCollision;
	}

	public void setIsCollision(boolean isCollision) {
		this.isCollision = isCollision;
	}

	public boolean isCollisingTarget(ColliderComponent c2){
		return collisingTargets.contains(c2);
	}

	public void addCollisingTarget(ColliderComponent c){
		collisingTargets.add(c);
	}

	public void removeCollisingTarget(ColliderComponent c){
		collisingTargets.remove(c);
	}

	public boolean isTrigger() {
		return isTrigger;
	}

	public void setTrigger(boolean trigger) {
		isTrigger = trigger;
	}
}

