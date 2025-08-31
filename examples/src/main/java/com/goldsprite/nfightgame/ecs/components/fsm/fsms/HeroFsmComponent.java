package com.goldsprite.nfightgame.ecs.components.fsm.fsms;

import com.goldsprite.nfightgame.ecs.components.fsm.states.*;

public class HeroFsmComponent extends EntityFsmComponent<HeroFsmComponent, EntityState<HeroFsmComponent>> {

	//Component Area


	//RoleInfoAndController Area


	//Input Area
	@Override
	public void inputHandle() {
		super.inputHandle();
	}


	//State Area
	@Override
	public void initStates() {
		addState(new IdleState<>());
		addState(new MoveState<>());
		addState(new JumpState<>());

		addState(new CrouchingState<>());
		addState(new CrouchWalkState<>());
		addState(new StandingState<>());
		addState(new AttackState<>());
		getState(AttackState.class).setAtkFrame(1);
		addState(new HurtState<>());
		addState(new DeathState<>());
		addState(new RespawnState<>());

//		addState(new IdleState());
//		addState(new RunState());
//		addState(new JumpState());
//		addState(new CrouchingState());
//		addState(new CrouchWalkState());
//		addState(new StandingState());
//		addState(new AttackState());
//		addState(new HurtState());
//		addState(new DeathState());
//		addState(new RespawnState());
	}

	/*
	public static class IdleState extends EntityState<HeroFsmComponent> {
		@Override
		public void enter() {
			//动画
			fsm.anim.setCurAnim(StateType.Idle);
			//静止玩家
			fsm.rigi.getVelocity().setX(0);
		}

		@Override
		public void exit() {
		}

		@Override
		public void running(float delta) {
			//进入奔跑 横轴方向输入不为0
			if (fsm.getKeyDirX() != 0) {
				fsm.changeState(RunState.class);
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

	public static class RunState extends EntityState<HeroFsmComponent> {
		private float normalRunFrameDuration;

		@Override
		public void init() {
			normalRunFrameDuration = fsm.anim.getAnim(StateType.Run).getFrameDuration();
		}

		@Override
		public void enter() {
//			Gdx.app.log("", "enter run"+(int)(new Random().nextFloat()*100));
			float dirX = fsm.getKeyDirX();
			//动画
			fsm.anim.setCurAnim(StateType.Run);
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
			float duration = normalRunFrameDuration / (!speedBoost ? 1 : fsm.ent.getBoostSpeedMultiplier());
			fsm.ent.changeSpeedBoost(speedBoost);
			fsm.anim.getCurrentAnim().setFrameDuration(duration);

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

	public static class JumpState extends EntityState<HeroFsmComponent> {
		boolean isJumped;

		@Override
		public void enter() {
			isJumped = false;
//			Gdx.app.log("", "enter jump"+(int)(new Random().nextFloat()*100));
			//动画
			fsm.anim.setCurAnim(StateType.Jump);
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
			if (fsm.getBodyCollider().isCollision() && fsm.getKeyJump()) {
				jumping();
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
			fsm.rigi.getVelocity().setY(jumpForce);
		}
	}

	public static class CrouchingState<F extends IEntityFsm> extends EntityState<F> {
		boolean crouched;
		private Vector2 crouchBodyCollOffset, crouchBodyCollSize;

		@Override
		public void init() {
			Vector2 standBodyCollOffset = fsm.getBodyCollider().getOffsetPosition();
			Vector2 standBodyCollSize = fsm.getBodyCollider().getOriSize();
			crouchBodyCollOffset = standBodyCollOffset.clone().sub(0, standBodyCollSize.y / 4f);
			crouchBodyCollSize = standBodyCollSize.clone().div(1, 2);
		}

		@Override
		public void enter() {
			//重置已蹲下
			crouched = false;
			//动画
			fsm.anim.setCurAnim(StateType.Crouching, fsm.resetAnim);
		}

		@Override
		public void running(float delta) {
			//已蹲下了
			if (!crouched && fsm.anim.isFinished()) {
				crouched = true;
				fsm.getBodyCollider().getOffsetPosition().set(crouchBodyCollOffset);
				fsm.getBodyCollider().getOriSize().set(crouchBodyCollSize);
			}

			//站立
			if (fsm.anim.isFinished() && fsm.getKeyCrouch()) {
				fsm.changeState(StandingState.class);
			}
			//蹲行
			if (fsm.anim.isFinished() && fsm.getKeyDirX() != 0) {
				fsm.changeState(CrouchWalkState.class);
			}
			//受击
			if (fsm.getKeyHurt()) {
				fsm.changeState(HurtState.class);
			}
		}
	}

	public class CrouchWalkState<F extends IEntityFsm> extends EntityState<F> {
		@Override
		public void enter() {
			//动画
			fsm.anim.setCurAnim(StateType.CrouchWalk);
		}

		@Override
		public void running(float delta) {
			//接收运动输入
			fsm.move(fsm.getKeyDirX());

			//回到蹲下
			if (fsm.getKeyDirX() == 0) {
				//设置到结束动画
				fsm.anim.stateTime = fsm.anim.getCurrentAnim().getAnimationDuration();
				fsm.changeState(CrouchingState.class, false);
			}
			//受击
			if (fsm.getKeyHurt()) {
				fsm.changeState(HurtState.class);
			}
		}
	}

	public static class StandingState<F extends IEntityFsm> extends EntityState<F> {
		private Vector2 standBodyCollOffset, standBodyCollSize;

		@Override
		public void init() {
			standBodyCollOffset = fsm.getBodyCollider().getOffsetPosition().clone();
			standBodyCollSize = fsm.getBodyCollider().getOriSize().clone();
		}

		@Override
		public void enter() {
			//动画
			fsm.anim.setCurAnim(StateType.Standing);
		}

		@Override
		public void running(float delta) {
			//回到待机
			if (fsm.anim.isFinished()) {
				fsm.getBodyCollider().getOffsetPosition().set(standBodyCollOffset);
				fsm.getBodyCollider().getOriSize().set(standBodyCollSize);
				fsm.changeState(IdleState.class);
			}
			//受击
			if (fsm.getKeyHurt()) {
				fsm.changeState(HurtState.class);
			}
		}
	}

	public static class AttackState<F extends IEntityFsm> extends EntityState<F> {
		int atkFrame = 1;

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
			int frameIndex = fsm.anim.getAnimFrameIndex(fsm.anim.current);
			if (frameIndex == atkFrame) fsm.getAttackTrigger().setEnable(true);

			//动画结束回到待机
			if (fsm.anim.isFinished()) {
				fsm.changeState(IdleState.class);
			}
			//移动打断
			if (fsm.getKeyDirX() != 0 && !fsm.getMoveKeyProtect()) {
				fsm.changeState(RunState.class);
			}
			//受击
			if (fsm.getKeyHurt()) {
				fsm.changeState(HurtState.class);
			}
		}
	}

	public static class HurtState<F extends IEntityFsm> extends EntityState<F> {
		@Override
		public void enter() {
			//扣血
			fsm.getEnt().hurt(fsm.getBeHurt_damage());

			//动画
			fsm.anim.setCurAnim(StateType.Hurt);
		}

		@Override
		public void exit() {
			//消费掉hurtKey: 效果为受击结束前不会再次受击
			fsm.consumeHurtKey();
		}

		@Override
		public void running(float delta) {
			//被杀死转死亡
			if (fsm.anim.isFinished() && fsm.getEnt().isDead()) {
				fsm.changeState(DeathState.class);
				return;
			}
			//回到待机
			if (fsm.anim.isFinished()) {
				fsm.changeState(IdleState.class);
			}
		}
	}

	public static class DeathState<F extends IEntityFsm> extends EntityState<F> {
		@Override
		public void enter() {
			fsm.anim.setCurAnim(StateType.Death);
		}

		@Override
		public void running(float delta) {
//			//清理
//			if(fsm.anim.isFinished()) {
//				fsm.getGObject().destroy();
//			}
		}
	}

	public static class RespawnState<F extends IEntityFsm> extends EntityState<F> {
		@Override
		public void enter() {
			fsm.anim.setCurAnim(StateType.Respawn);
		}

		@Override
		public void running(float delta) {
			//起身后治愈角色并回到待机
			if (fsm.anim.isFinished()) {
				fsm.ent.heal();
				fsm.changeState(IdleState.class);
			}
		}
	}
	*/

}
