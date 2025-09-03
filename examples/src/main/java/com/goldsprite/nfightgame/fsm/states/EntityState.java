package com.goldsprite.nfightgame.fsm.states;

import com.goldsprite.nfightgame.fsm.interfaces.IFsm;
import com.goldsprite.nfightgame.fsm.interfaces.IState;

public class EntityState<F extends IFsm> implements IState<F> {
	protected F fsm;

	public void init(){}
	@Override
	public void enter() {}
	@Override
	public void running(float delta) {}
	@Override
	public void exit() {}
	@Override
	public void setFsm(F fsm) {
		this.fsm = fsm;
		init();
	}
}
