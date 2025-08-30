package com.goldsprite.nfightgame.core.components;

import com.goldsprite.nfightgame.core.ecs.component.AnimatorComponent;
import com.goldsprite.nfightgame.core.ecs.component.Component;
import com.goldsprite.nfightgame.core.fsms.enums.StateType;

public class DummyControllerComponent extends Component {
	@Override
	public void update(float delta) {
		//攻击播放完自动关闭
		boolean isHurt = getAnimator().current.equals(StateType.Hurt);
		boolean playFinish = getAnimator().anims.get(getAnimator().current).isAnimationFinished(getAnimator().stateTime);
		if(isHurt && playFinish){
			getAnimator().setCurAnim(StateType.Idle);
		}
	}

	public AnimatorComponent getAnimator(){
		return getComponent(AnimatorComponent.class);
	}
}
