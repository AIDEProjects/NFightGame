package com.goldsprite.nfightgame.ecs.components.fsm.states;

import com.goldsprite.nfightgame.ecs.components.fsm.enums.StateType;
import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IEntityFsm;

public class AttackState<F extends IEntityFsm> extends EntityState<F> {
	private int atkFrame = 1;

	@Override
	public void enter() {
		fsm.getAnim().setCurAnim(StateType.Attack);
		//开启前摇保护
		fsm.setMoveKeyProtect(true);
		//在地面时停住玩家
		if(fsm.isGround()) fsm.move(0);
	}

	@Override
	public void exit() {
		fsm.getAttackTrigger().setEnable(false);
	}

	@Override
	public void running(float delta) {
		/// TODO: 这里后面可以改为动画器的帧事件
		//攻击帧时才启用攻击碰撞器
		int frameIndex = fsm.getAnim().getAnimFrameIndex(fsm.getAnim().current);
		if (frameIndex == atkFrame) fsm.getAttackTrigger().setEnable(true);

		//动画结束回到待机
		if (fsm.getAnim().isFinished()) {
			fsm.changeState(IdleState.class);
		}
		//移动打断
		if (fsm.getKeyDirX() != 0 && !fsm.getMoveKeyProtect()) {
			fsm.changeState(MoveState.class);
		}
		//受击
		if (fsm.getKeyHurt()) {
			fsm.changeState(HurtState.class);
		}
	}

	public int getAtkFrame() {
		return atkFrame;
	}
	public void setAtkFrame(int atkFrame) {
		this.atkFrame = atkFrame;
	}
}
