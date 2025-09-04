package com.goldsprite.nfightgame.fsm.fsms;

import com.goldsprite.nfightgame.fsm.states.*;

public class LizardmanFsmComponent extends EntityFsmComponent<LizardmanFsmComponent, EntityState<LizardmanFsmComponent>>{
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
