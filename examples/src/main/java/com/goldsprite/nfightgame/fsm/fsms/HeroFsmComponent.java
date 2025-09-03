package com.goldsprite.nfightgame.fsm.fsms;

import com.goldsprite.nfightgame.fsm.states.*;

public class HeroFsmComponent extends EntityFsmComponent<HeroFsmComponent, EntityState<HeroFsmComponent>> {

	//Component Area


	//RoleInfoAndController Area


	//Input Area
	@Override
	public void inputHandle() {
		super.inputHandle();
	}


	//State Area
	@Override
	public void initStates() {
		addState(new IdleState<>());
		addState(new MoveState<>());
		addState(new JumpState<>());

		addState(new CrouchingState<>());
		addState(new CrouchWalkState<>());
		addState(new StandingState<>());
		addState(new AttackState<>());
		getState(AttackState.class).setAtkFrame(1);
		addState(new HurtState<>());
		addState(new DeathState<>());
		addState(new RespawnState<>());
	}

}
