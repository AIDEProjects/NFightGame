package com.goldsprite.nfightgame.core.components;

import com.badlogic.gdx.math.Vector3;
import com.goldsprite.nfightgame.core.ecs.GameSystem;
import com.goldsprite.nfightgame.core.ecs.component.Component;
import com.goldsprite.nfightgame.core.ecs.component.TransformComponent;

public class FollowCamComponent extends Component {
	private TransformComponent target;

	@Override
	public void update(float delta) {
		followCamera();
	}

	private void followCamera(){
		//跟随相机
		Vector3 camPos = GameSystem.getInstance().getCamera().position;
		camPos.x = transform.getPosition().x;
		camPos.y = transform.getPosition().y;
		GameSystem.getInstance().getCamera().update();
	}

	public TransformComponent getTarget() {
		return target;
	}

	public void setTarget(TransformComponent target) {
		this.target = target;
	}
}
