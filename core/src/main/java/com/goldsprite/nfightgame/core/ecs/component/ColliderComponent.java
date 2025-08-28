/**
 * @Author
 * @AIDE AIDE+
 */
package com.goldsprite.nfightgame.core.ecs.component;

import com.goldsprite.utils.math.Vector2;
import java.util.*;
import java.util.function.*;

public class ColliderComponent extends Component {
	protected boolean isEnabled = true;
	protected boolean showGizmos = true;
	protected boolean isCollision = false;
	protected boolean isTrigger = false;
	protected Vector2 centerPosition = new Vector2();
	protected Vector2 offsetPosition = new Vector2();
	protected List<ColliderComponent> collisingTargets = new ArrayList<>();
	protected List<Consumer<ColliderComponent>> onTriggerEnterListeners = new ArrayList<>();

	public void addOnTriggerEnterListener(Consumer<ColliderComponent> listener){
		onTriggerEnterListeners.add(listener);
	}
	public void removeOnTriggerEnterListener(Consumer<ColliderComponent> listener){
		onTriggerEnterListeners.remove(listener);
	}
	
	public void onTriggerEnter(ColliderComponent c2) {
		//onTriggerEnterListeners.forEach(c -> c.accept(c2));
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
	
	public void setEnable(boolean isEnable) {
		this.isEnabled = isEnable;
	}

	public boolean isEnable() {
		return isEnabled;
	}
}

