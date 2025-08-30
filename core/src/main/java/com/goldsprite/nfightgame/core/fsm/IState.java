/**
 * @Author 
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core.fsm;

public interface IState{
	void enter();
	void update(float delta);
	void exit();
	
	IStateMachine getStateMachine();
}
