package com.goldsprite.nfightgame.ecs.components.fsm.states;

import com.goldsprite.nfightgame.ecs.components.fsm.enums.StateType;
import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IEntityFsm;

public class HurtState<F extends IEntityFsm> extends EntityState<F> {
	@Override
	public void enter() {
		//扣血
		fsm.getEnt().hurt(fsm.getBeHurt_damage());

		//动画
		fsm.getAnim().setCurAnim(StateType.Hurt);
	}

	@Override
	public void exit() {
		//消费掉hurtKey: 效果为受击结束前不会再次受击
		fsm.consumeHurtKey();
	}

	@Override
	public void running(float delta) {
		//被杀死转死亡
		if (fsm.getAnim().isFinished() && fsm.getEnt().isDead()) {
			fsm.changeState(DeathState.class);
			return;
		}
		//回到待机
		if (fsm.getAnim().isFinished()) {
			fsm.changeState(IdleState.class);
		}
	}
}
