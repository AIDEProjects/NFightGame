package com.goldsprite.nfightgame.core.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.goldsprite.nfightgame.core.ecs.component.AnimatorComponent;
import com.goldsprite.nfightgame.core.ecs.component.Component;
import com.goldsprite.nfightgame.core.ecs.component.RigidbodyComponent;
import com.goldsprite.nfightgame.core.fsm.IState;
import com.goldsprite.nfightgame.core.fsm.IStateMachine;
import com.goldsprite.nfightgame.core.fsm.State;
import com.goldsprite.nfightgame.core.fsms.enums.StateType;

import java.util.LinkedHashMap;
import java.util.Map;

public class HeroStateMachineComponent extends Component implements IStateMachine {
	private Map<Class, HeroState> states = new LinkedHashMap<>();
	private Class current;
	private float key_dirX;
	public EntityComponent ent;
	public AnimatorComponent anim;
	public RigidbodyComponent rigi;


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


	//StateMachine Area
	public void running(float delta){
		getCurrentState().running(delta);
	}

	public HeroState getCurrentState(){
		return states.get(current);
	}

	public void changeState(Class<? extends HeroState> key){
		if(current != null)getCurrentState().exit();
		current = key;
		getCurrentState().enter();
	}


	//Input Area
	private void inputHandle() {
		boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.D);
		boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.W);
		boolean downPressed = Gdx.input.isKeyPressed(Input.Keys.S);

		key_dirX = leftPressed ? -1 : rightPressed ? 1 : 0;

	}

	public float getKey_dirX() {
		return key_dirX;
	}


	//State Area
	private void initStates() {
		addState(new IdleState());
		addState(new RunState());
	}

	private void addState(HeroState state) {
		if(current == null) current = state.getClass();
		states.put(state.getClass(), state);
		state.setFsm(this);
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
			anim.setCurAnim(StateType.Idle);
		}

		@Override
		public void running(float delta) {
			//进入奔跑
			if(fsm.getKey_dirX() != 0){
				fsm.changeState(RunState.class);
			}
		}
	}

	public class RunState extends HeroState {
		@Override
		public void enter() {
			float dirX = fsm.getKey_dirX();
			//翻转
			int faceX = (int)Math.signum(dirX);
			fsm.transform.setFace(faceX, 1);
			//动画
			fsm.anim.setCurAnim(StateType.Run);
			//运动
			float speed = fsm.ent.getSpeed() * dirX;
			fsm.rigi.getVelocity().setX(speed);
		}

		@Override
		public void exit() {
			//退出时停下
			fsm.rigi.getVelocity().setX(0);
		}

		@Override
		public void running(float delta) {
			//回到待机
			if(fsm.getKey_dirX() == 0){
				fsm.changeState(IdleState.class);
			}
		}
	}
}
