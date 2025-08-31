/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core.fsm;

public interface IState<F extends IFsm>{
	void enter();
	void running(float delta);
	void exit();

	void setFsm(F fsm);
}
