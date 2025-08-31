package com.goldsprite.nfightgame.core.components.fsms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.goldsprite.nfightgame.core.components.fsms.HeroFsmComponent.HeroState;
import com.goldsprite.nfightgame.core.ecs.component.ColliderComponent;
import com.goldsprite.nfightgame.core.ecs.component.RectColliderComponent;
import com.goldsprite.nfightgame.core.fsm.IState;
import com.goldsprite.nfightgame.core.fsms.enums.StateType;
import com.goldsprite.utils.math.Vector2;

public class HeroFsmComponent extends EntityFsmComponent<HeroFsmComponent, HeroState> {
	private float key_dirX;
	private boolean key_jump, key_crouch, key_attack, key_speedBoost;
	private boolean moveKeyProtect;
	private ColliderComponent footCollider;
	private RectColliderComponent bodyCollider;
	private ColliderComponent attackTrigger;
	private Vector2 standBodyCollOffset, standBodyCollSize;
	private Vector2 crouchBodyCollOffset, crouchBodyCollSize;


	//Component Area
	@Override
	protected void initMachine() {
		super.initMachine();
	}


	//RoleInfoAndController Area
	private void move(float dirX) {
		//翻转
		int faceX = (int) Math.signum(dirX);
		if (faceX != 0) transform.setFace(faceX, 1);
		//运动
		float speed = ent.getSpeed() * dirX;
		rigi.getVelocity().setX(speed);
	}

	public void respawn() {
		changeState(RespawnState.class);
	}

	private boolean isFalling() {
		return rigi.getVelocity().y < -100f;
	}

	private boolean isGround() {
		return footCollider.isCollision();
	}

	public ColliderComponent getFootCollider() {
		return footCollider;
	}

	public void setFootCollider(ColliderComponent footCollider) {
		this.footCollider = footCollider;
	}

	public void setBodyCollider(RectColliderComponent bodyCollider) {
		this.bodyCollider = bodyCollider;
		standBodyCollOffset = bodyCollider.getOffsetPosition().clone();
		standBodyCollSize = bodyCollider.getOriSize().clone();
		crouchBodyCollOffset = standBodyCollOffset.clone().sub(0, standBodyCollSize.y / 4f);
		crouchBodyCollSize = standBodyCollSize.clone().div(1, 2);
	}

	public ColliderComponent getAttackTrigger() {
		return attackTrigger;
	}

	public void setAttackTrigger(ColliderComponent c) {
		attackTrigger = c;

		//设定攻击效果
		attackTrigger.onTriggerEnterListeners.add(victim -> {
			EntityFsmComponent victimEnt = victim.getComponent(EntityFsmComponent.class);
			if(victimEnt == null) return;

			victimEnt.beHurt(5);
			Gdx.app.log("", "伤害");
		});
	}


	//Input Area
	@Override
	public void inputHandle() {
		//持续左右
		boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.D);
		boolean leftJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.A);
		boolean rightJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.D);
		key_jump = Gdx.input.isKeyJustPressed(Input.Keys.K);//跳
		key_crouch = Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT);//蹲
		key_attack = Gdx.input.isKeyJustPressed(Input.Keys.J);//攻击
		key_speedBoost = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);//疾跑
		if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) beHurt(5);//受击(测试
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) respawn();//重生(测试

		if (leftPressed) key_dirX = -1;
		if (rightPressed) key_dirX = 1;
		if (!leftPressed && !rightPressed) key_dirX = 0;
		if (moveKeyProtect && (leftJustPressed || rightJustPressed)) moveKeyProtect = false;

	}

	public float getKeyDirX() {
		return key_dirX;
	}

	public void setKeyDirX(float key_dirX) {
		this.key_dirX = key_dirX;
	}

	public boolean getKeyJump() {
		return key_jump;
	}

	public boolean getKeyCrouch() {
		return key_crouch;
	}

	public boolean getKeyAttack() {
		return key_attack;
	}

	public boolean getKeySpeedBoost() {
		return key_speedBoost;
	}

	public boolean getMoveKeyProtect() {
		return moveKeyProtect;
	}

	public void setMoveKeyProtect(boolean moveKeyProtect) {
		this.moveKeyProtect = moveKeyProtect;
	}


	//State Area
	@Override
	public void initStates() {
		addState(new IdleState());
		addState(new RunState());
		addState(new JumpState());
		addState(new CrouchingState());
		addState(new CrouchWalkState());
		addState(new StandingState());
		addState(new AttackState());
		addState(new HurtState());
		addState(new DeathState());
		addState(new RespawnState());
	}


	public static class HeroState implements IState<HeroFsmComponent> {
		protected HeroFsmComponent fsm;

		public void init(){}
		@Override
		public void enter() {}
		@Override
		public void running(float delta) {}
		@Override
		public void exit() {}
		@Override
		public void setFsm(HeroFsmComponent fsm) {
			this.fsm = fsm;
			init();
		}
	}

	public class IdleState extends HeroState {
		@Override
		public void enter() {
//			Gdx.app.log("", "enter idle"+(int)(new Random().nextFloat()*100));
			//动画
			anim.setCurAnim(StateType.Idle);
			//静止玩家
			fsm.rigi.getVelocity().setX(0);
		}

		@Override
		public void exit() {
//			Gdx.app.log("", "exit idle"+(int)(new Random().nextFloat()*100));
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

	public class RunState extends HeroState {
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

	public class JumpState extends HeroState {
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
			if (!fsm.isGround() || fsm.bodyCollider.isCollision()) isJumped = true;
			//持续更新速度与朝向
			fsm.move(fsm.getKeyDirX());
			//蹭墙跳
			if (fsm.bodyCollider.isCollision() && fsm.getKeyJump()) {
				jumping();
			}

			//回到待机
			if (isJumped && isGround()) {
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

	public class CrouchingState extends HeroState {
		boolean crouched;

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
				fsm.bodyCollider.getOffsetPosition().set(fsm.crouchBodyCollOffset);
				fsm.bodyCollider.getOriSize().set(fsm.crouchBodyCollSize);
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

	public class CrouchWalkState extends HeroState {
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

	public class StandingState extends HeroState {
		@Override
		public void enter() {
			//动画
			fsm.anim.setCurAnim(StateType.Standing);
		}

		@Override
		public void running(float delta) {
			//回到待机
			if (fsm.anim.isFinished()) {
				fsm.bodyCollider.getOffsetPosition().set(fsm.standBodyCollOffset);
				fsm.bodyCollider.getOriSize().set(fsm.standBodyCollSize);
				fsm.changeState(IdleState.class);
			}
			//受击
			if (fsm.getKeyHurt()) {
				fsm.changeState(HurtState.class);
			}
		}
	}

	public class AttackState extends HeroState {
		int atkFrame = 1;

		@Override
		public void enter() {
			fsm.anim.setCurAnim(StateType.Attack);
			//开启前摇保护
			fsm.setMoveKeyProtect(true);
			//在地面时停住玩家
			if(fsm.isGround()) fsm.move(0);
		}

		@Override
		public void exit() {
			attackTrigger.setEnable(false);
		}

		@Override
		public void running(float delta) {
			/// TODO: 这里后面可以改为动画器的帧事件
			//攻击帧时才启用攻击碰撞器
			int frameIndex = fsm.anim.getAnimFrameIndex(fsm.anim.current);
			if (frameIndex == atkFrame) attackTrigger.setEnable(true);

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

	public class HurtState extends HeroState {
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

	public static class DeathState extends HeroState {
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

	public class RespawnState extends HeroState {
		@Override
		public void enter() {
			fsm.anim.setCurAnim(StateType.Respawn);
		}

		@Override
		public void running(float delta) {
			//起身后治愈角色并回到待机
			if (fsm.anim.isFinished()) {
				ent.heal();
				fsm.changeState(IdleState.class);
			}
		}
	}
}
