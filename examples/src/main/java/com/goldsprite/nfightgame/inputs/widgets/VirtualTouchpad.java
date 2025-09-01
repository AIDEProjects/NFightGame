package com.goldsprite.nfightgame.inputs.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.goldsprite.nfightgame.inputs.GameVirtualKey;
import com.goldsprite.nfightgame.inputs.InputManager;

public class VirtualTouchpad extends Touchpad {
	private boolean isTouchpadPressed;
	private Enum prevDir;
	private Enum virtualKeyUp, virtualKeyDown, virtualKeyLeft, virtualKeyRight;

	public VirtualTouchpad(float size, float deadzonePercent, Skin skin) {
		super(deadzonePercent * size/2, skin);
	}

	@Override
	public void act(float delta) {
		virtualInputUpdate();
	}

	private void virtualInputUpdate() {
		Enum dir = getVirtualDir();

		//down
		if(!isTouchpadPressed && dir != null){
			isTouchpadPressed = true;
			InputManager.getInstance().actionDown(dir);
			prevDir = dir;
		}
		//hold
		if(isTouchpadPressed && dir != null){
			if(dir != prevDir) {
				InputManager.getInstance().actionUp(prevDir);//换向
				InputManager.getInstance().actionDown(dir);
			}else {
				InputManager.getInstance().actionHold(dir);
			}
			prevDir = dir;
		}
		//up
		if(isTouchpadPressed && dir == null){
			isTouchpadPressed = false;
			InputManager.getInstance().actionUp(prevDir);
		}
	}

	public Enum getVirtualDir(){
		Enum dir = null;
		float x = getKnobPercentX();
		float y = getKnobPercentY();

		if (x == 0 && y == 0) return dir;

		float angle = (float) Math.toDegrees(Math.atan2(y, x));
		if (angle < 0) angle += 360;

		if (angle >= 315 || angle < 45) dir = virtualKeyRight;
		if (angle >= 45 && angle < 135) dir = virtualKeyUp;
		if (angle >= 135 && angle < 225) dir = virtualKeyLeft;
		if(angle >= 225 && angle < 315) dir = virtualKeyDown;

		return dir;
	}

	public void bindVirtualKey(Enum up, Enum down, Enum left, Enum right) {
		this.virtualKeyUp = up;
		this.virtualKeyDown = down;
		this.virtualKeyLeft = left;
		this.virtualKeyRight = right;
	}
}
