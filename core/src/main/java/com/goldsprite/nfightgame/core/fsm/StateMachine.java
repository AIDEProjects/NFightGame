/**
 * @Author 
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core.fsm;
import java.util.*;

public class StateMachine implements IStateMachine{
	private Map<Enum, IState> states = new LinkedHashMap<>();
	private Enum current;
	
	public void update(float delta){
		getCurrentState().update(delta);
	}
	
	public <T extends IState> T getCurrentState(){
		return (T)states.get(current);
	}
	
}
