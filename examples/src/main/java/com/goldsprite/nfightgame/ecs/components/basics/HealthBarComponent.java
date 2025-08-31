/**
 * @Author
 * @AIDE AIDE+
 */
package com.goldsprite.nfightgame.ecs.components.basics;

import com.goldsprite.gdxcore.ecs.component.SpriteComponent;
import com.goldsprite.gdxcore.ecs.component.TransformComponent;
import com.goldsprite.utils.math.Vector2;

public class HealthBarComponent extends SpriteComponent {

	private TransformComponent owner;
	private final Vector2 positionOffset = new Vector2();
	private SpriteComponent[] barTexes;
	private boolean visible = true;

	public void bindEntity(TransformComponent trans) {
		this.owner = trans;
	}

	@Override
	public void update(float delta) {
		//拥有者移除后同步移除自身
		if(owner.getGObject().isDestroyed()){
			gObject.destroy();
			return;
		}

		//位置同步
		transform.getPosition().set(positionOffset).add(owner.getPosition());

		//血量同步
		EntityComponent ent = owner.getComponent(EntityComponent.class);
		float healthPercent = ent.getHealth() / ent.getMaxHealth();
		barTexes[1].getRegion().setRegion(0f, 0f, healthPercent, 1f);

		//隐藏死亡生物的血条
		boolean isDead = ent.isDead();
		if((isDead && visible) || (!isDead && !visible)){
			for(SpriteComponent s : barTexes){
				s.setEnable(visible = !isDead);
			}
		}
	}

	public void setPositionOffset(float x, float y) {
		this.positionOffset.set(x, y);
	}

	public void setHealthBarTextures(SpriteComponent[] barTexes) {
		this.barTexes = barTexes;
	}
}

