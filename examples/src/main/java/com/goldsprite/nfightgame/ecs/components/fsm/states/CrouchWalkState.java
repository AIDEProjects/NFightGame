package com.goldsprite.nfightgame.ecs.components.fsm.states;

import com.goldsprite.nfightgame.ecs.components.fsm.enums.StateType;
import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IEntityFsm;

public class CrouchWalkState<F extends IEntityFsm> extends EntityState<F> {
	@Override
	public void enter() {
		//动画
		fsm.getAnim().setCurAnim(StateType.CrouchWalk);
	}

	@Override
	public void running(float delta) {
		//接收运动输入
		fsm.move(fsm.getKeyDirX());

		//回到蹲下
		if (fsm.getKeyDirX() == 0) {
			//设置到结束动画
			fsm.getAnim().stateTime = fsm.getAnim().getCurrentAnim().getAnimationDuration();
			fsm.changeState(CrouchingState.class, false);
		}
		//受击
		if (fsm.getKeyHurt()) {
			fsm.changeState(HurtState.class);
		}
	}
}
