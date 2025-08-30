package com.goldsprite.nfightgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.AnimatorComponent;
import com.goldsprite.nfightgame.core.ecs.component.Component;
import com.goldsprite.nfightgame.core.ecs.component.RigidbodyComponent;
import com.goldsprite.nfightgame.core.ecs.component.TransformComponent;
import com.goldsprite.nfightgame.core.ecs.system.GameSystem;
import com.goldsprite.utils.math.Vector2;
import com.goldsprite.nfightgame.core.ecs.component.*;
import java.util.function.*;

public class RoleControllerComponent extends Component {

	private Rocker rocker;
	private TransformComponent target;
	private float speed = 100, jumpForce = 1100;

	private AnimatorComponent animator;
	private CircleColliderComponent attackTrigger;

	public void bindAttackTrigger(CircleColliderComponent trigger) {
		this.attackTrigger = trigger;
		Consumer<ColliderComponent> onTargetBeHurtListener = (collider) -> {
			AnimatorComponent animator = collider.getComponent(AnimatorComponent.class);
			animator.setCurAnim("hurt");
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
		boolean isAtk = getAnimator().current.equals("attack");
		boolean playFinish = getAnimator().anims.get(getAnimator().current).isAnimationFinished(getAnimator().stateTime);
		if(isAtk && playFinish){
			getAnimator().setCurAnim("idle");
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

	private void moveRole(float delta) {
		Vector2 vel = rocker.getValue();
		boolean crouching = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
		if(Gdx.input.isKeyPressed(Input.Keys.A)) vel.x = -1;
		if(Gdx.input.isKeyPressed(Input.Keys.D)) vel.x = 1;
		if(Gdx.input.isKeyPressed(Input.Keys.W)) vel.y = 1;
		if(Gdx.input.isKeyPressed(Input.Keys.S)) vel.y = -1;
		if(Gdx.input.isKeyPressed(Input.Keys.J)) attack();
		if((crouching && vel.x == 0 && !getAnimator().isAnim("crouchMove")) || vel.y < -0.5f) enterCrouch();

		float downVal = 0.3f;
		//位移
		float velX = vel.x * speed;
		RigidbodyComponent rigi = getRigi();
		if(Math.abs(vel.x)>downVal || vel.x==0) rigi.getVelocity().setX(velX);
//		if(vel.y!=0) rigi.getVelocity().setY(vel.y * speed);
		//动画
		String moveType = crouching?"crouchMove":"run";
		if(getAnimator().current.equals("run") && vel.x == 0) getAnimator().setCurAnim("idle");
		if(Math.abs(vel.x)>downVal) getAnimator().setCurAnim(moveType);
		if(Math.abs(vel.x)<downVal && getAnimator().isAnim("crouchMove")) getAnimator().setCurAnim("crouch", false);
		//翻转
		if(Math.abs(vel.x)>downVal) target.getFace().setX((int)Math.signum(vel.x));

		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) jump();

		RectColliderComponent bodyCollider = target.getComponent(RectColliderComponent.class);
		if(getAnimator().isAnim("crouch") || getAnimator().isAnim("crouchMove")){
			bodyCollider.setOffsetPosition(0, 30);
			bodyCollider.setSize(35, 70);
		}else{
			bodyCollider.setOffsetPosition(0, 50);
			bodyCollider.setSize(35, 110);
		}
	}

	public void enterCrouch() {
		getAnimator().setCurAnim("crouch");
	}

	private RigidbodyComponent getRigi() {
		return target.getComponent(RigidbodyComponent.class);
	}

	public void jump() {
		getRigi().getVelocity().add(0, jumpForce);
	}

	public void attack() {
		getAnimator().setCurAnim("attack");
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
