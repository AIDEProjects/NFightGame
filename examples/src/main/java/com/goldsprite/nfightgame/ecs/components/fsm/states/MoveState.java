package com.goldsprite.nfightgame.ecs.components.fsm.states;

import com.goldsprite.nfightgame.ecs.components.fsm.enums.StateType;
import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IEntityFsm;

public class MoveState<F extends IEntityFsm> extends EntityState<F>{
	private float normalRunFrameDuration;

	@Override
	public void init() {
		normalRunFrameDuration = fsm.getAnim().getAnim(StateType.Move).getFrameDuration();
	}

	@Override
	public void enter() {
//			Gdx.app.log("", "enter run"+(int)(new Random().nextFloat()*100));
		float dirX = fsm.getKeyDirX();
		//动画
		fsm.getAnim().setCurAnim(StateType.Move);
		//更新速度与朝向
		fsm.move(dirX);
	}

	@Override
	public void exit() {
//			Gdx.app.log("", "exit run"+(int)(new Random().nextFloat()*100));
	}

	@Override
	public void running(float delta) {
		//持续更新速度与朝向
		fsm.move(fsm.getKeyDirX());
		//切换疾跑
		boolean speedBoost = fsm.getKeySpeedBoost();
		float duration = normalRunFrameDuration / (!speedBoost ? 1 : fsm.getEnt().getBoostSpeedMultiplier());
		fsm.getEnt().changeSpeedBoost(speedBoost);
		fsm.getAnim().getCurrentAnim().setFrameDuration(duration);

		//回到待机 横轴方向输入为0
		if (fsm.getKeyDirX() == 0) {
			fsm.changeState(IdleState.class);
		}
		//进入跳跃 按下跳跃或正在坠落
		if (fsm.getKeyJump() || (fsm.isFalling() && !fsm.isGround())) {
			fsm.changeState(JumpState.class);
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
