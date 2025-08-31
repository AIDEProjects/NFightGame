package com.goldsprite.nfightgame.ecs.components.fsm.states;

import com.goldsprite.nfightgame.ecs.components.fsm.enums.StateType;
import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IEntityFsm;

public class DeathState<F extends IEntityFsm> extends EntityState<F> {
	@Override
	public void enter() {
		fsm.getAnim().setCurAnim(StateType.Death);
	}

	@Override
	public void running(float delta) {
//			//清理
//			if(fsm.getAnim().isFinished()) {
//				fsm.getGObject().destroy();
//			}
	}
}
