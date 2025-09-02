package com.goldsprite.nfightgame.inputs;

import com.goldsprite.nfightgame.inputs.widgets.VirtualJoystick;
import com.goldsprite.utils.math.Vector2;

public class VirtualJoystickBinding extends Vector2KeyBinding {
	private VirtualJoystick joystick;

	public VirtualJoystick getJoystick() {
		return joystick;
	}

	public void setJoystick(VirtualJoystick joystick) {
		this.joystick = joystick;
	}

	@Override
	public boolean updateValue() {
		if (joystick == null) return false;

		Vector2 vector = action.readValue(Vector2.class);
		vector.set(joystick.getVector());

//		return upKey || downKey || leftKey || rightKey;//只有松开所有键才视为无更新
//		return !dir.isZero();//同时按下左右或同时上下则视为无更新
		//当数据有变更时才视为无更新
		boolean isUpdated = !vector.equals(lastVector);
		setLastVector(vector);
		return isUpdated;
	}

	@Override
	public boolean getActionRight() {
		if (joystick == null) return false;
		float angle = getJoystick().getAngle();
		return angle >= 315 || (angle < 45 && angle > 0);
	}

	@Override
	public boolean getActionUp() {
		if (joystick == null) return false;
		float angle = getJoystick().getAngle();
		return angle >= 45 && angle < 135;
	}

	@Override
	public boolean getActionLeft() {
		if (joystick == null) return false;
		float angle = getJoystick().getAngle();
		return angle >= 135 && angle < 225;
	}

	@Override
	public boolean getActionDown() {
		if (joystick == null) return false;
		float angle = getJoystick().getAngle();
		return angle >= 225 && angle < 315;
	}

	@Override
	public boolean matchesKey(int keyboardCode) {
		return false;
	}
}
