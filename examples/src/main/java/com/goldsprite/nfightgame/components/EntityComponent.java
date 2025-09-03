package com.goldsprite.nfightgame.components;

import com.badlogic.gdx.math.MathUtils;
import com.goldsprite.gdxcore.ecs.component.Component;

public class EntityComponent extends Component {
	private float speed = 300;
	private boolean isSpeedBoost;
	private float boostSpeedMultiplier = 1.5f;
	public float jumpForce = 1100;
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

	public void hurt(float damage) {
		health = MathUtils.clamp(health - damage, 0, maxHealth);
	}

	public float getSpeed() {
		//返回正常或疾跑速度
		return  speed * (!isSpeedBoost ? 1 : boostSpeedMultiplier);
	}
	public float getOriSpeed(){
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public float getBoostSpeedMultiplier() {
		return boostSpeedMultiplier;
	}
	public void setBoostSpeedMultiplier(float boostSpeedMultiplier) {
		this.boostSpeedMultiplier = boostSpeedMultiplier;
	}

	public boolean isDead() {
		return health <= 0;
	}

	public void heal() {
		heal(maxHealth);
	}

	private void heal(float health) {
		setHealth(health);
	}

	public void changeSpeedBoost(boolean speedBoost) {
		this.isSpeedBoost = speedBoost;
	}
}
