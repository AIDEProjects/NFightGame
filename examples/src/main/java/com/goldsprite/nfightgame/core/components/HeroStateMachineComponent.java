package com.goldsprite.nfightgame.core.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.goldsprite.nfightgame.core.ecs.component.*;
import com.goldsprite.nfightgame.core.fsm.IState;
import com.goldsprite.nfightgame.core.fsm.IStateMachine;
import com.goldsprite.nfightgame.core.fsms.enums.StateType;
import com.goldsprite.utils.math.Vector2;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class HeroStateMachineComponent extends Component implements IStateMachine {
	private Map<Class, HeroState> states = new LinkedHashMap<>();
	private Class current;
	private float key_dirX;
	private boolean key_jump, key_crouch;
	public EntityComponent ent;
	public AnimatorComponent anim;
	public RigidbodyComponent rigi;
	private ColliderComponent footCollider;
	private RectColliderComponent bodyCollider;
	private Vector2 standBodyCollOffset, standBodyCollSize;
	private Vector2 crouchBodyCollOffset, crouchBodyCollSize;
	private boolean resetAnim;


	//Component Area
	public void init() {
		//初始化依赖配置
		initMachine();

		initStates();
	}

	private void initMachine() {
		ent = getComponent(EntityComponent.class);
		anim = getComponent(AnimatorComponent.class);
		rigi = getComponent(RigidbodyComponent.class);
	}

	@Override
	public void update(float delta) {
		inputHandle();

		running(delta);
	}


	//RoleInfoAndController Area
	private void move(float dirX) {
		//翻转
		int faceX = (int)Math.signum(dirX);
		if(faceX != 0) transform.setFace(faceX, 1);
		//运动
		float speed = ent.getSpeed() * dirX;
		rigi.getVelocity().setX(speed);
	}
	private boolean isFalling(){
		return rigi.getVelocity().y < -100f;
	}
	private boolean isGround(){
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
		crouchBodyCollOffset = standBodyCollOffset.clone().sub(0, standBodyCollSize.y/4f);
		crouchBodyCollSize = standBodyCollSize.clone().div(1, 2);
	}


	//Input Area
	private void inputHandle() {
		//持续左右
		boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.D);
		//跳
		key_jump = Gdx.input.isKeyJustPressed(Input.Keys.K);
		//蹲
		key_crouch = Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT);

		if(leftPressed) key_dirX = -1;
		if(rightPressed) key_dirX = 1;
		if(!leftPressed && !rightPressed) key_dirX = 0;

	}
	public float getKeyDirX() {
		return key_dirX;
	}
	public boolean getKeyJump(){
		return key_jump;
	}
	public boolean getKeyCrouch(){
		return key_crouch;
	}


	//State Area
	public void running(float delta){
		getCurrentState().running(delta);
	}

	public HeroState getCurrentState(){
		return states.get(current);
	}

	public void changeState(Class<? extends HeroState> key){
		changeState(key, true);
	}
	public void changeState(Class<? extends HeroState> key, boolean resetAnim){
		this.resetAnim = resetAnim;
		Gdx.app.log(""+new Random().nextInt(100), "changeState "+current.getSimpleName()+" -> "+key.getSimpleName());
//		Gdx.app.log("", "bodyIsCollision: "+bodyCollider.isCollision());
		if(current != null)getCurrentState().exit();
		current = key;
		getCurrentState().enter();
	}

	private void initStates() {
		addState(new IdleState());
		addState(new RunState());
		addState(new JumpState());
		addState(new CrouchingState());
		addState(new CrouchWalkState());
		addState(new StandingState());
	}

	public void addState(HeroState state) {
		if(current == null) current = state.getClass();
		states.put(state.getClass(), state);
		state.setFsm(this);
	}

	public boolean isResetAnim() {
		return resetAnim;
	}


	public static class HeroState implements IState {
		protected HeroStateMachineComponent fsm;

		@Override
		public void enter() {}
		@Override
		public void running(float delta) {}
		@Override
		public void exit() {}

		public HeroStateMachineComponent getStateMachine() {
			return fsm;
		}

		public void setFsm(HeroStateMachineComponent fsm) {
			this.fsm = fsm;
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
			if(fsm.getKeyDirX() != 0){
				fsm.changeState(RunState.class);
			}
			//进入跳跃 按下跳跃或正在坠落
			if(fsm.getKeyJump() || (fsm.isFalling() && !fsm.isGround())){
				fsm.changeState(JumpState.class);
			}
			//下蹲
			if(fsm.getKeyCrouch()){
				fsm.changeState(CrouchingState.class);
			}
		}
	}

	public class RunState extends HeroState {
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

			//回到待机 横轴方向输入为0
			if(fsm.getKeyDirX() == 0){
				fsm.changeState(IdleState.class);
			}
			//进入跳跃 按下跳跃或正在坠落
			if(fsm.getKeyJump() || (fsm.isFalling() && !fsm.isGround())){
				fsm.changeState(JumpState.class);
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
			if(!fsm.isFalling()) jumping();
		}

		@Override
		public void exit() {
//			Gdx.app.log("", "exit jump"+(int)(new Random().nextFloat()*100));
		}

		@Override
		public void running(float delta) {
//			Gdx.app.log("", "isGround: "+fsm.isGround());
			//已离开地面
			if(!fsm.isGround() || fsm.bodyCollider.isCollision()) isJumped = true;
			//持续更新速度与朝向
			fsm.move(fsm.getKeyDirX());
			//蹭墙跳
			if(fsm.bodyCollider.isCollision() && fsm.getKeyJump()){
				jumping();
			}

			//回到待机
			if(isJumped && isGround()){
				fsm.changeState(IdleState.class);
			}
		}

		private void jumping() {
			float jumpForce = fsm.ent.jumpForce;
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
			if(!crouched && fsm.anim.isFinished()) {
				crouched = true;
				fsm.bodyCollider.getOffsetPosition().set(fsm.crouchBodyCollOffset);
				fsm.bodyCollider.getOriSize().set(fsm.crouchBodyCollSize);
			}

			//站立
			if(fsm.anim.isFinished() && fsm.getKeyCrouch()){
				fsm.changeState(StandingState.class);
			}
			//蹲行
			if(fsm.anim.isFinished() && fsm.getKeyDirX() != 0){
				fsm.changeState(CrouchWalkState.class);
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
			if(fsm.getKeyDirX() == 0){
				//设置到结束动画
				fsm.anim.stateTime = fsm.anim.getCurrentAnim().getAnimationDuration();
				fsm.changeState(CrouchingState.class, false);
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
			if(fsm.anim.isFinished()){
				fsm.bodyCollider.getOffsetPosition().set(fsm.standBodyCollOffset);
				fsm.bodyCollider.getOriSize().set(fsm.standBodyCollSize);
				fsm.changeState(IdleState.class);
			}
		}
	}
}
