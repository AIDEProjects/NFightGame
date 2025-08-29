/**
 * @Author
 * @AIDE AIDE+
 */
package com.goldsprite.nfightgame.core;

import com.goldsprite.nfightgame.core.ecs.component.SpriteComponent;
import com.goldsprite.nfightgame.core.ecs.component.TransformComponent;
import com.goldsprite.utils.math.Vector2;

public class HealthBarComponent extends SpriteComponent {

	private TransformComponent owner;
	private final Vector2 positionOffset = new Vector2();
	private SpriteComponent barTex;

	public void bindEntity(TransformComponent trans) {
		this.owner = trans;
	}

	@Override
	public void update(float delta) {
		//位置同步
		transform.getPosition().set(positionOffset).add(owner.getPosition());

		//血量同步
		EntityComponent ent = owner.getComponent(EntityComponent.class);
		float healthPercent = ent.getHealth() / ent.getMaxHealth();
		barTex.getRegion().setRegion(0f, 0f, healthPercent, 1f);
	}

	public void setPositionOffset(float x, float y) {
		this.positionOffset.set(x, y);
	}

	public void setHealthBarTextures(SpriteComponent barTex) {
		this.barTex = barTex;
	}
}

