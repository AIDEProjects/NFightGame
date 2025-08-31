package com.goldsprite.nfightgame.ecs.components.fsm.fsms;

import com.goldsprite.nfightgame.ecs.components.fsm.states.*;

public class LizardManFsmComponent extends EntityFsmComponent<LizardManFsmComponent, EntityState<LizardManFsmComponent>>{
	@Override
	public void initStates() {
		addState(new IdleState<>());
		addState(new MoveState<>());
		addState(new JumpState<>());
		addState(new AttackState<>());
		getState(AttackState.class).setAtkFrame(5);
		addState(new HurtState<>());
		addState(new DeathState<>());
		addState(new RespawnState<>());
	}
}
