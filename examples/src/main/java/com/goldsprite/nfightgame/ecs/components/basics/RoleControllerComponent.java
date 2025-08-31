package com.goldsprite.nfightgame.ecs.components.basics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.goldsprite.gdxcore.ecs.component.*;
import com.goldsprite.nfightgame.ecs.ui.Rocker;
import com.goldsprite.gdxcore.ecs.GameSystem;
import com.goldsprite.nfightgame.ecs.components.fsm.enums.StateType;
import com.goldsprite.utils.math.Vector2;

import java.util.function.*;

public class RoleControllerComponent extends Component {

	private Rocker rocker;
	private TransformComponent target;
	private float speed = 100;
	private final float jumpForce = 1100;

	private AnimatorComponent animator;
	private CircleColliderComponent attackTrigger;

	public void bindAttackTrigger(CircleColliderComponent trigger) {
		this.attackTrigger = trigger;
		Consumer<ColliderComponent> onTargetBeHurtListener = (collider) -> {
			AnimatorComponent animator = collider.getComponent(AnimatorComponent.class);
			animator.setCurAnim(StateType.Hurt);
			int facingPlayer = (int)Math.signum(transform.getPosition().x - collider.getTransform().getPosition().x);
			collider.getTransform().getFace().setX(facingPlayer);
			EntityComponent targetEnt = collider.getComponent(EntityComponent.class);
			targetEnt.hurt(5);
		};
		trigger.onTriggerEnterListeners.add(onTargetBeHurtListener);
	}

	public void setRocker(Rocker rocker) {
		this.rocker = rocker;
	}

	@Override
	public void update(float delta) {
		//移动角色
		moveRole(delta);

		//攻击播放完自动关闭
		boolean isAtk = getAnimator().current.equals(StateType.Attack);
		boolean playFinish = getAnimator().anims.get(getAnimator().current).isAnimationFinished(getAnimator().stateTime);
		if(isAtk && playFinish){
			getAnimator().setCurAnim(StateType.Idle);
		}
		boolean isSliding = getAnimator().current.equals(StateType.Sliding);
		if(isSliding && playFinish){
			getAnimator().setCurAnim(StateType.Idle);
		}

		//攻击帧时才启用攻击碰撞器
		int frameIndex = getAnimator().anims.get(getAnimator().current).getKeyFrameIndex(getAnimator().stateTime);
		boolean enable = isAtk && frameIndex == 1;
		attackTrigger.setEnable(enable);

		//跟随相机
		Vector3 camPos = GameSystem.getInstance().getCamera().position;
		camPos.x = transform.getPosition().x;
		camPos.y = transform.getPosition().y;
		GameSystem.getInstance().getCamera().update();
	}

	public boolean crouching, sliding;
	private void moveRole(float delta) {
		Vector2 vel = rocker.getValue();
		if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))crouching = !crouching;
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))sliding = true;
		if(Gdx.input.isKeyPressed(Input.Keys.A)) vel.x = -1;
		if(Gdx.input.isKeyPressed(Input.Keys.D)) vel.x = 1;
		if(Gdx.input.isKeyPressed(Input.Keys.W)) vel.y = 1;
		if(Gdx.input.isKeyPressed(Input.Keys.S)) vel.y = -1;
		if(Gdx.input.isKeyPressed(Input.Keys.J)) attack();
		//if((crouching && vel.x == 0 && !getAnimator().isAnim(StateType.CrouchWalk)) || vel.y < -0.5f) enterCrouch();
		if(sliding){
			getAnimator().setCurAnim(StateType.Sliding);
			vel.x = target.getTransform().getFace().getX();
		}


		float downVal = 0.3f;
		//位移
		float velX = vel.x * speed;
		RigidbodyComponent rigi = getRigi();
		if(Math.abs(vel.x)>downVal || vel.x==0) rigi.getVelocity().setX(velX);
//		if(vel.y!=0) rigi.getVelocity().setY(vel.y * speed);
		//动画
		StateType moveType = crouching?StateType.CrouchWalk:StateType.Move;
		//if(getAnimator().current.equals(StateType.Run) && vel.x == 0) getAnimator().setCurAnim(StateType.Idle);
		if(Math.abs(vel.x)>downVal) getAnimator().setCurAnim(moveType);
		//if(Math.abs(vel.x)<downVal && getAnimator().isAnim(StateType.CrouchWalk)) getAnimator().setCurAnim(StateType.Crouch, false);
		//翻转
		if(Math.abs(vel.x)>downVal) target.getFace().setX((int)Math.signum(vel.x));

		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) jump();

		RectColliderComponent bodyCollider = target.getComponent(RectColliderComponent.class);
		if(getAnimator().isAnim(StateType.Crouching) || getAnimator().isAnim(StateType.CrouchWalk)){
			bodyCollider.setOffsetPosition(0, 30);
			bodyCollider.setSize(35, 70);
		}else{
			bodyCollider.setOffsetPosition(0, 50);
			bodyCollider.setSize(35, 110);
		}
	}

	private void enterSliding() {
		getAnimator().setCurAnim(StateType.Sliding);
	}

	public void enterCrouch() {
		getAnimator().setCurAnim(StateType.Crouching);
	}

	private RigidbodyComponent getRigi() {
		return target.getComponent(RigidbodyComponent.class);
	}

	public void jump() {
		getRigi().getVelocity().add(0, jumpForce);
	}

	public void attack() {
		getAnimator().setCurAnim(StateType.Attack);
	}

	public void setTarget(TransformComponent target) {
		this.target = target;
	}

	public AnimatorComponent getAnimator(){
		return target.getComponent(AnimatorComponent.class);
	}

	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
