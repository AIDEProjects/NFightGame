package com.goldsprite.nfightgame.core;

import com.goldsprite.nfightgame.core.ecs.component.AnimatorComponent;
import com.goldsprite.nfightgame.core.ecs.component.Component;

public class DummyControllerComponent extends Component {
	@Override
	public void update(float delta) {
		//攻击播放完自动关闭
		boolean isHurt = getAnimator().current.equals("hurt");
		boolean playFinish = getAnimator().anims.get(getAnimator().current).isAnimationFinished(getAnimator().stateTime);
		if(isHurt && playFinish){
			getAnimator().setCurAnim("idle");
		}
	}

	public AnimatorComponent getAnimator(){
		return getComponent(AnimatorComponent.class);
	}
}
