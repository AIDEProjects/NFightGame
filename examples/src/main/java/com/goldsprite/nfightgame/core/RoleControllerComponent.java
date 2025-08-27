package com.goldsprite.nfightgame.core;

import com.badlogic.gdx.Gdx;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.AnimatorComponent;
import com.goldsprite.nfightgame.core.ecs.component.Component;
import com.goldsprite.nfightgame.core.ecs.component.TransformComponent;
import com.goldsprite.utils.math.Vector2;

public class RoleControllerComponent extends Component {

	private Rocker rocker;
	private TransformComponent target;
	private float speed = 100;

	public void setRocker(Rocker rocker) {
		this.rocker = rocker;
	}

	@Override
	public void update(float delta) {
		moveRole(delta);
	}

	private void moveRole(float delta) {
		Vector2 vel = rocker.getValue();

		//位移
		float velX = vel.x * speed * delta;
		target.getPosition().add(velX, 0);
		//动画
		AnimatorComponent animator = target.getComponent(AnimatorComponent.class);
		animator.setCurAnim(vel.x==0?"idle":"run");
		//翻转
		if(vel.x != 0) target.setFace((int)Math.signum(vel.x), target.getFace().y);
	}

	public void setTarget(TransformComponent target) {
		this.target = target;
	}

	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
