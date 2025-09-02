package com.goldsprite.nfightgame.ecs.components.fsm.interfaces;

import com.goldsprite.gdxcore.ecs.component.AnimatorComponent;
import com.goldsprite.gdxcore.ecs.component.ColliderComponent;
import com.goldsprite.gdxcore.ecs.component.RectColliderComponent;
import com.goldsprite.gdxcore.ecs.component.RigidbodyComponent;
import com.goldsprite.nfightgame.ecs.components.basics.EntityComponent;
import com.goldsprite.nfightgame.ecs.components.basics.EntityInputManagerComponent;

public interface IEntityFsm extends IFsm {
	EntityComponent getEnt();
	AnimatorComponent getAnim();
	RigidbodyComponent getRigi();
	EntityInputManagerComponent getInputs();

	boolean isFalling();
	boolean isGround();

	ColliderComponent getFootTrigger();

	RectColliderComponent getBodyCollider();

	void setFootTrigger(ColliderComponent footTrigger);

	void setBodyCollider(RectColliderComponent bodyCollider);

	ColliderComponent getAttackTrigger();

	void setAttackTrigger(ColliderComponent c);

	//RoleInfoAndController Area
	void move(float dirX);

	float getKeyDirX();

	void setKeyDirX(float key_dirX);

	boolean getMoveKeyProtect();

	void setMoveKeyProtect(boolean moveKeyProtect);

	boolean getKeyJump();

	void setKeyJump(boolean down);

	boolean getKeyCrouch();
	void setKeyCrouch(boolean down);

	boolean getKeyAttack();
	void setKeyAttack(boolean down);

	boolean getKeySpeedBoost();
	void setKeySpeedBoost(boolean down);

	boolean getKeyHurt();
	void setKeyHurt(boolean down);

	void beHurt(float damage);

	void consumeHurtKey();

	float getBeHurt_damage();

	void addState(IState state);

	IState getCurrentState();

	void changeState(Class<? extends IState> key, boolean resetAnim);

	boolean isResetAnim();
}
