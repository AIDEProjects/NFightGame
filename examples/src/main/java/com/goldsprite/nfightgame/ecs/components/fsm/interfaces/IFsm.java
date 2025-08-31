/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.ecs.components.fsm.interfaces;

public interface IFsm {
	void running(float delta);

	void changeState(Class<? extends IState> key);
	<T extends IState> T getState(Class<T> key);
}
