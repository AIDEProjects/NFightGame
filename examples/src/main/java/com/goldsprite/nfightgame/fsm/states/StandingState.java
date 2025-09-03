package com.goldsprite.nfightgame.fsm.states;

import com.goldsprite.nfightgame.fsm.enums.StateType;
import com.goldsprite.nfightgame.fsm.interfaces.IEntityFsm;
import com.goldsprite.utils.math.Vector2;

public class StandingState<F extends IEntityFsm> extends EntityState<F> {
	private Vector2 standBodyCollOffset, standBodyCollSize;

	@Override
	public void init() {
		standBodyCollOffset = fsm.getBodyCollider().getOffsetPosition().clone();
		standBodyCollSize = fsm.getBodyCollider().getOriSize().clone();
	}

	@Override
	public void enter() {
		//动画
		fsm.getAnim().setCurAnim(StateType.Standing);
	}

	@Override
	public void running(float delta) {
		//回到待机
		if (fsm.getAnim().isFinished()) {
			fsm.getBodyCollider().getOffsetPosition().set(standBodyCollOffset);
			fsm.getBodyCollider().getOriSize().set(standBodyCollSize);
			fsm.changeState(IdleState.class);
		}
		//受击
		if (fsm.getKeyHurt()) {
			fsm.changeState(HurtState.class);
		}
	}
}
