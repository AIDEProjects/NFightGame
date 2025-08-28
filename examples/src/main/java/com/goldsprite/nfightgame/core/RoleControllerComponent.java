package com.goldsprite.nfightgame.core;

import com.badlogic.gdx.Gdx;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.AnimatorComponent;
import com.goldsprite.nfightgame.core.ecs.component.Component;
import com.goldsprite.nfightgame.core.ecs.component.RigidbodyComponent;
import com.goldsprite.nfightgame.core.ecs.component.TransformComponent;
import com.goldsprite.utils.math.Vector2;
import com.goldsprite.nfightgame.core.ecs.component.*;
import java.util.function.*;

public class RoleControllerComponent extends Component {

	private Rocker rocker;
	private TransformComponent target;
	private float speed = 100;

	private AnimatorComponent animator;
	private CircleColliderComponent attackTrigger;

	public void bindAttackTrigger(CircleColliderComponent trigger) {
		this.attackTrigger = trigger;
		Consumer<ColliderComponent> onTargetBeHurtListener = (collider) -> {
			AnimatorComponent animator = collider.getComponent(AnimatorComponent.class);
			animator.setCurAnim("hurt");
		};
		trigger.addOnTriggerEnterListener(onTargetBeHurtListener);
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
	}

	private void moveRole(float delta) {
		Vector2 vel = rocker.getValue();
//		vel.x = 1;

		//位移
		float velX = vel.x * speed;
		RigidbodyComponent rigi = target.getComponent(RigidbodyComponent.class);
		rigi.getVelocity().setX(velX);
		if(vel.y!=0) rigi.getVelocity().setY(vel.y * speed);
		//动画
		if(getAnimator().current.equals("run") && vel.x == 0) getAnimator().setCurAnim("idle");
		if(getAnimator().current.equals("idle") && vel.x != 0) getAnimator().setCurAnim("run");
		//翻转
		if(vel.x != 0) target.getFace().setX((int)Math.signum(vel.x));
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
