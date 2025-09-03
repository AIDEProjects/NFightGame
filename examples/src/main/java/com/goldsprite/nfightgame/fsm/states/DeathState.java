package com.goldsprite.nfightgame.fsm.states;

import com.goldsprite.nfightgame.fsm.enums.StateType;
import com.goldsprite.nfightgame.fsm.interfaces.IEntityFsm;

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
