package com.goldsprite.nfightgame.ecs.components.fsm.states;

import com.goldsprite.nfightgame.ecs.components.fsm.enums.StateType;
import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IEntityFsm;
import com.goldsprite.utils.math.Vector2;

public class CrouchingState<F extends IEntityFsm> extends EntityState<F> {
	boolean crouched;
	private Vector2 crouchBodyCollOffset, crouchBodyCollSize;

	@Override
	public void init() {
		Vector2 standBodyCollOffset = fsm.getBodyCollider().getOffsetPosition();
		Vector2 standBodyCollSize = fsm.getBodyCollider().getOriSize();
		crouchBodyCollOffset = standBodyCollOffset.clone().sub(0, standBodyCollSize.y / 4f);
		crouchBodyCollSize = standBodyCollSize.clone().div(1, 2);
	}

	@Override
	public void enter() {
		//重置已蹲下
		crouched = false;
		//动画
		fsm.getAnim().setCurAnim(StateType.Crouching, fsm.isResetAnim());
	}

	@Override
	public void running(float delta) {
		//已蹲下了
		if (!crouched && fsm.getAnim().isFinished()) {
			crouched = true;
			fsm.getBodyCollider().getOffsetPosition().set(crouchBodyCollOffset);
			fsm.getBodyCollider().getOriSize().set(crouchBodyCollSize);
		}

		//站立
		if (fsm.getAnim().isFinished() && fsm.getKeyCrouch()) {
			fsm.changeState(StandingState.class);
		}
		//蹲行
		if (fsm.getAnim().isFinished() && fsm.getKeyDirX() != 0) {
			fsm.changeState(CrouchWalkState.class);
		}
		//受击
		if (fsm.getKeyHurt()) {
			fsm.changeState(HurtState.class);
		}
	}
}
