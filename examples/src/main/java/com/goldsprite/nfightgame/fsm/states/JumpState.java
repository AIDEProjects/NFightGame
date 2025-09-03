package com.goldsprite.nfightgame.fsm.states;

import com.goldsprite.nfightgame.fsm.enums.StateType;
import com.goldsprite.nfightgame.fsm.interfaces.IEntityFsm;

public class JumpState<F extends IEntityFsm> extends EntityState<F> {
	boolean isJumped;
	boolean isOnWall,isKeyJumpUp;//用于解决蹭墙连跳

	@Override
	public void enter() {
		isJumped = false;
		isKeyJumpUp = false;
		//动画
		fsm.getAnim().setCurAnim(StateType.Jump);
		//跳跃力
		if (!fsm.isFalling()) jumping();
	}

	@Override
	public void exit() {
	}

	@Override
	public void running(float delta) {
		//是否松手
		if(!fsm.getKeyJump())
			isKeyJumpUp = true;
		//已离开地面或身体有接触点(碰撞)则标记已起跳
		if (!fsm.isGround() || fsm.getBodyCollider().isCollision()) isJumped = true;
		//持续更新速度与朝向
		fsm.move(fsm.getKeyDirX());
		//蹭墙跳
		if (!isOnWall && fsm.getBodyCollider().isCollision() && isKeyJumpUp && fsm.getKeyJump()) {
			jumping();
			isOnWall = true;
			isKeyJumpUp = false;
		}
		if (isOnWall && !fsm.getBodyCollider().isCollision()){
			isOnWall = false;
		}

		//回到待机
		if (isJumped && fsm.isGround()) {
			fsm.changeState(IdleState.class);
		}
		//受击
		if (fsm.getKeyHurt()) {
			fsm.changeState(HurtState.class);
		}
		//攻击
		if(fsm.getKeyAttack()){
			fsm.changeState(AttackState.class);
		}
	}

	private void jumping() {
		float jumpForce = fsm.getEnt().jumpForce;
		fsm.getRigi().getVelocity().setY(jumpForce);
	}
}
