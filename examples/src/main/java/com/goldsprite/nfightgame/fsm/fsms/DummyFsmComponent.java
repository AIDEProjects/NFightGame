package com.goldsprite.nfightgame.fsm.fsms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.goldsprite.gdxcore.ecs.component.RectColliderComponent;
import com.goldsprite.nfightgame.fsm.interfaces.IEntityFsm;
import com.goldsprite.nfightgame.fsm.states.EntityState;
import com.goldsprite.nfightgame.fsm.enums.StateType;
import com.goldsprite.nfightgame.fsm.states.IdleState;
import com.goldsprite.nfightgame.fsm.states.RespawnState;

public class DummyFsmComponent extends EntityFsmComponent<DummyFsmComponent, EntityState<DummyFsmComponent>> {

	private RectColliderComponent bodyCollider;

	//Component Area
	@Override
	public void initMachine() {
		super.initMachine();
	}

	public RectColliderComponent getBodyCollider() {
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
		addState(new RespawnState());
	}

	@Override
	public void respawn() {
		changeState(RespawnState.class);
	}

	public static class IdleState extends EntityState<DummyFsmComponent> {
		@Override
		public void enter() {
			//动画
			fsm.anim.setCurAnim(StateType.Idle);
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

	public static class HurtState extends EntityState<DummyFsmComponent> {
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

	public static class DeathState extends EntityState<DummyFsmComponent> {
		@Override
		public void enter() {
			fsm.anim.setCurAnim(StateType.Death);
			fsm.getBodyCollider().setEnable(false);
		}

		@Override
		public void exit() {
		}

		@Override
		public void running(float delta) {
//			//清理
//			if(fsm.anim.isFinished()) {
//				fsm.getGObject().destroy();
//			}
		}
	}

	public static class RespawnState extends EntityState<DummyFsmComponent> {
		@Override
		public void enter() {
			fsm.getAnim().setCurAnim(StateType.Respawn);
		}

		@Override
		public void exit() {
			fsm.getBodyCollider().setEnable(true);
		}

		@Override
		public void running(float delta) {
			//起身后治愈角色并回到待机
			if (fsm.getAnim().isFinished()) {
				fsm.getEnt().heal();
				fsm.changeState(IdleState.class);
			}
		}
	}

}
