package com.goldsprite.nfightgame.core.components;

import com.badlogic.gdx.math.MathUtils;
import com.goldsprite.nfightgame.core.ecs.component.Component;

public class EntityComponent extends Component {
	private float speed = 300;
	private float maxHealth = 20;
	private float health = maxHealth;

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth, boolean isFull) {
		this.maxHealth = maxHealth;
		if (isFull) this.health = maxHealth;
	}
	public void setMaxHealth(float maxHealth) {
		setMaxHealth(maxHealth, false);
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

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
