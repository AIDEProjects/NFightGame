package com.goldsprite.nfightgame.core.components.fsms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.goldsprite.nfightgame.core.ecs.component.*;
import com.goldsprite.nfightgame.core.fsm.IState;
import com.goldsprite.nfightgame.core.components.fsms.DummyFsmComponent.DummyState;
import com.goldsprite.nfightgame.core.fsms.enums.StateType;

public class DummyFsmComponent extends EntityFsmComponent<DummyFsmComponent, DummyState> {

	private RectColliderComponent bodyCollider;

	//Component Area
	@Override
	public void initMachine() {
		super.initMachine();
	}

	public ColliderComponent getBodyCollider() {
		return bodyCollider;
	}
	public void setBodyCollider(RectColliderComponent bodyCollider) {
		this.bodyCollider = bodyCollider;
	}


	//Input Area


	@Override
	public void inputHandle() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_0)) beHurt(5);
	}

	//State Area
	@Override
	public void initStates() {
		addState(new IdleState());
		addState(new HurtState());
		addState(new DeathState());
	}

	public static class DummyState implements IState<DummyFsmComponent> {
		protected DummyFsmComponent fsm;

		@Override
		public void enter() {}
		@Override
		public void running(float delta) {}
		@Override
		public void exit() {}
		@Override
		public void setFsm(DummyFsmComponent fsm) {
			this.fsm = fsm;
		}
	}

	public class IdleState extends DummyState {
		@Override
		public void enter() {
			//动画
			anim.setCurAnim(StateType.Idle);
			//静止角色
			fsm.rigi.getVelocity().setX(0);
		}

		@Override
		public void running(float delta) {
			//受击
			if (fsm.getKeyHurt()) {
				fsm.changeState(HurtState.class);
			}
		}
	}

	public class HurtState extends DummyState {
		@Override
		public void enter() {
			//扣血
			fsm.getEnt().hurt(fsm.getBeHurt_damage());

			//被杀死转死亡
			if (fsm.getEnt().isDead()) {
				fsm.changeState(DeathState.class);
				return;
			}

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
			//回到待机
			if (fsm.anim.isFinished()) {
				fsm.changeState(IdleState.class);
			}
		}
	}

	public class DeathState extends DummyState {
		@Override
		public void enter() {
			//清理
			fsm.getGObject().destroy();
		}
	}
}
