package com.goldsprite.gdxcore.ecs.component;

import com.badlogic.gdx.graphics.Color;
import com.goldsprite.gdxcore.ecs.renderer.Gizmos;
import com.goldsprite.utils.math.Vector2;
import com.goldsprite.utils.math.Vector2Int;

public class TransformComponent extends Component {
	private final Vector2 position = new Vector2();//原点位置
	private final Vector2 scale = new Vector2(1, 1);//缩放
	private final Vector2Int face = new Vector2Int(1, 1);//朝向
	public Vector2 getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale.set(scale);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}

	public Vector2Int getFace() {
		return face;
	}

	public void setFace(int faceX, int faceY) {
		face.set(faceX, faceY);
	}

	@Override
	protected void drawGizmos() {
		Gizmos.setColor(Color.YELLOW);
		Gizmos.setHollow(true);
		Gizmos.circle(position.x, position.y, 5);
	}
}
