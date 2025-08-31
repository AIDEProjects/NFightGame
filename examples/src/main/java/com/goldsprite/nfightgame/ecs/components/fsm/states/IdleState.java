package com.goldsprite.nfightgame.ecs.components.fsm.states;

import com.goldsprite.nfightgame.ecs.components.fsm.enums.StateType;
import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IEntityFsm;

public class IdleState<F extends IEntityFsm> extends EntityState<F>{
	@Override
	public void enter() {
		//动画
		fsm.getAnim().setCurAnim(StateType.Idle);
		//静止玩家
		fsm.getRigi().getVelocity().setX(0);
	}

	@Override
	public void exit() {
	}

	@Override
	public void running(float delta) {
		//进入奔跑 横轴方向输入不为0
		if (fsm.getKeyDirX() != 0) {
			fsm.changeState(MoveState.class);
		}
		//进入跳跃 按下跳跃或正在坠落
		if (fsm.getKeyJump() || (fsm.isFalling() && !fsm.isGround())) {
			fsm.changeState(JumpState.class);
		}
		//下蹲
		if (fsm.getKeyCrouch()) {
			fsm.changeState(CrouchingState.class);
		}
		//攻击
		if (fsm.getKeyAttack()) {
			fsm.changeState(AttackState.class);
		}
		//受击
		if (fsm.getKeyHurt()) {
			fsm.changeState(HurtState.class);
		}
	}
}
