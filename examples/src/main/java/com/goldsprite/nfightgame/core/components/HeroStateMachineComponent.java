package com.goldsprite.nfightgame.core.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.goldsprite.nfightgame.core.ecs.component.AnimatorComponent;
import com.goldsprite.nfightgame.core.ecs.component.ColliderComponent;
import com.goldsprite.nfightgame.core.ecs.component.Component;
import com.goldsprite.nfightgame.core.ecs.component.RigidbodyComponent;
import com.goldsprite.nfightgame.core.fsm.IState;
import com.goldsprite.nfightgame.core.fsm.IStateMachine;
import com.goldsprite.nfightgame.core.fsms.enums.StateType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class HeroStateMachineComponent extends Component implements IStateMachine {
	private Map<Class, HeroState> states = new LinkedHashMap<>();
	private Class current;
	private float key_dirX;
	private boolean key_jump;
	public EntityComponent ent;
	public AnimatorComponent anim;
	public RigidbodyComponent rigi;
	private ColliderComponent footCollider;


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
		if(dirX == 0) return;
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


	//Input Area
	private void inputHandle() {
		//持续左右
		boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.D);
		//跳
		key_jump = Gdx.input.isKeyJustPressed(Input.Keys.K);

		if(leftPressed) key_dirX = -1;
		if(rightPressed) key_dirX = 1;
		if(!leftPressed && !rightPressed) key_dirX = 0;

	}
	public float getKey_dirX() {
		return key_dirX;
	}
	public boolean getKey_Jump(){
		return key_jump;
	}


	//State Area
	public void running(float delta){
		getCurrentState().running(delta);
	}

	public HeroState getCurrentState(){
		return states.get(current);
	}

	public void changeState(Class<? extends HeroState> key){
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
	}

	private void addState(HeroState state) {
		if(current == null) current = state.getClass();
		states.put(state.getClass(), state);
		state.setFsm(this);
	}

	public ColliderComponent getFootCollider() {
		return footCollider;
	}

	public void setFootCollider(ColliderComponent footCollider) {
		this.footCollider = footCollider;
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
			if(fsm.getKey_dirX() != 0){
				fsm.changeState(RunState.class);
			}
			//进入跳跃 按下跳跃或正在坠落
			if(fsm.getKey_Jump() || (fsm.isFalling() && !fsm.isGround())){
				fsm.changeState(JumpState.class);
			}
		}
	}

	public class RunState extends HeroState {
		@Override
		public void enter() {
//			Gdx.app.log("", "enter run"+(int)(new Random().nextFloat()*100));
			float dirX = fsm.getKey_dirX();
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
			fsm.move(fsm.getKey_dirX());

			//回到待机 横轴方向输入为0
			if(fsm.getKey_dirX() == 0){
				fsm.changeState(IdleState.class);
			}
			//进入跳跃 按下跳跃或正在坠落
			if(fsm.getKey_Jump() || (fsm.isFalling() && !fsm.isGround())){
				fsm.changeState(JumpState.class);
			}
		}
	}

	public class JumpState extends HeroState {
		boolean leaveGround;
		@Override
		public void enter() {
			leaveGround = false;
//			Gdx.app.log("", "enter jump"+(int)(new Random().nextFloat()*100));
			float jumpForce = fsm.ent.jumpForce;
			//动画
			fsm.anim.setCurAnim(StateType.Jump);
			//跳跃力
			if(!fsm.isFalling()) fsm.rigi.getVelocity().setY(jumpForce);
		}

		@Override
		public void exit() {
//			Gdx.app.log("", "exit jump"+(int)(new Random().nextFloat()*100));
		}

		@Override
		public void running(float delta) {
//			Gdx.app.log("", "isGround: "+fsm.isGround());
			//已离开地面
			if(!fsm.isGround()) leaveGround = true;
			//持续更新速度与朝向
			fsm.move(fsm.getKey_dirX());

			//回到待机
			if(leaveGround && fsm.footCollider.isCollision()){
				fsm.changeState(IdleState.class);
			}
		}
	}
}
