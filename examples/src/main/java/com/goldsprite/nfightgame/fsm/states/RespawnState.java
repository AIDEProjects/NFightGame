package com.goldsprite.nfightgame.fsm.states;

import com.goldsprite.nfightgame.fsm.enums.StateType;
import com.goldsprite.nfightgame.fsm.interfaces.IEntityFsm;

public class RespawnState<F extends IEntityFsm> extends EntityState<F> {
	@Override
	public void enter() {
		fsm.getAnim().setCurAnim(StateType.Respawn);
	}

	@Override
	public void running(float delta) {
		//起身后治愈角色并回到待机
		if (fsm.getAnim().isFinished()) {
			fsm.getEnt().heal();
			fsm.changeState(IdleState.class);
		}
	}
}
