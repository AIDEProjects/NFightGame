/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core.fsm;

public class State implements IState<IFsm> {
	protected IFsm fsm;

	@Override
	public void enter() {
	}

	@Override
	public void running(float delta) {
	}

	@Override
	public void exit() {
	}

	@Override
	public void setFsm(IFsm fsm) {
	}

	public IFsm getStateMachine(){
		return fsm;
	}

}

