package com.goldsprite.nfightgame.ecs.components.fsm.states;

import com.goldsprite.nfightgame.ecs.components.fsm.enums.StateType;
import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IEntityFsm;

public class JumpState<F extends IEntityFsm> extends EntityState<F> {
	boolean isJumped;
	boolean isOnWall,lastKeyJump;//用于解决蹭墙连跳

	@Override
	public void enter() {
		isJumped = false;
		lastKeyJump = false;
//			Gdx.app.log("", "enter jump"+(int)(new Random().nextFloat()*100));
		//动画
		fsm.getAnim().setCurAnim(StateType.Jump);
		//跳跃力
		if (!fsm.isFalling()) jumping();
	}

	@Override
	public void exit() {
//			Gdx.app.log("", "exit jump"+(int)(new Random().nextFloat()*100));
	}

	@Override
	public void running(float delta) {
//			Gdx.app.log("", "isGround: "+fsm.isGround());
		//已离开地面或身体有接触点(碰撞)则标记已起跳
		if (!fsm.isGround() || fsm.getBodyCollider().isCollision()) isJumped = true;
		//持续更新速度与朝向
		fsm.move(fsm.getKeyDirX());
		//蹭墙跳
		if (!isOnWall && fsm.getBodyCollider().isCollision()) {
			if(fsm.getKeyJump()){
				if(!lastKeyJump){
					fsm.setKeyJump(false);
					lastKeyJump = true;
				}else{
					jumping();
					isOnWall = true;
					lastKeyJump = false;
				}
			}else{
				jumping();
				isOnWall = true;
			}
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
