package com.goldsprite.nfightgame.core;

import com.badlogic.gdx.math.MathUtils;
import com.goldsprite.nfightgame.core.ecs.component.Component;

public class EntityComponent extends Component {
	private float maxHealth = 20;
	private float health = maxHealth;

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float nowHealth) {
		health = MathUtils.clamp(nowHealth, 0, maxHealth);
	}

	public void hurt(int damage) {
		health = MathUtils.clamp(health - damage, 0, maxHealth);
	}
}
