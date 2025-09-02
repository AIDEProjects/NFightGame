package com.goldsprite.nfightgame.inputs.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.goldsprite.utils.math.Vector2;

public class VirtualJoystick extends Touchpad {
	private final Vector2 vector = new Vector2();
	private float angle = -1;

	public VirtualJoystick(float size, float deadzonePercent, Skin skin) {
		super(deadzonePercent * size / 2, skin);
	}

	@Override
	public void act(float delta) {
		updateJoystickVector();
	}

	public void updateJoystickVector() {
		vector.set(getKnobPercentX(), getKnobPercentY());

		if (vector.x == 0 && vector.y == 0) {
			angle = -1;
			return;
		}
		angle = (float) Math.toDegrees(Math.atan2(vector.y, vector.x));
		if (angle < 0) angle += 360;
	}

	public float getAngle() {
		return angle;
	}
	public Vector2 getVector() {
		return vector;
	}
}
