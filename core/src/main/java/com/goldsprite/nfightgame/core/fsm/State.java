/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core.fsm;

public class State implements IState {
	protected IStateMachine fsm;

	@Override
	public void enter() {
	}

	@Override
	public void running(float delta) {
	}

	@Override
	public void exit() {
	}

	public IStateMachine getStateMachine(){
		return fsm;
	}

}

