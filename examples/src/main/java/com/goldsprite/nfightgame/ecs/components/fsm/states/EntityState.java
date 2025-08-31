package com.goldsprite.nfightgame.ecs.components.fsm.states;

import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IFsm;
import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IState;

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
