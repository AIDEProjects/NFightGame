/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core.fsm;

public interface IState{
	void enter();
	void running(float delta);
	void exit();
}
