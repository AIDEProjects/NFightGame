package com.goldsprite.nfightgame.inputs;

import com.badlogic.gdx.Gdx;
import com.goldsprite.utils.math.Vector2;

public class Vector2KeyBinding extends InputBinding {
	public KeyBinding Up;
	public KeyBinding Down;
	public KeyBinding Left;
	public KeyBinding Right;
	protected Vector2 lastVector = new Vector2();
	public void setLastVector(Vector2 dir) {
		this.lastVector.set(dir);
	}

	@Override
	public boolean matchesKey(int keyboardCode) {
		return Up.matchesKey(keyboardCode)
			|| Down.matchesKey(keyboardCode)
			|| Left.matchesKey(keyboardCode)
			|| Right.matchesKey(keyboardCode);
	}

	@Override
	public boolean updateValue() {
		Vector2 vector = action.readValue(Vector2.class);
		vector.set(0);
		if(getActionUp()) vector.y++;
		if(getActionDown()) vector.y--;
		if(getActionLeft()) vector.x--;
		if(getActionRight()) vector.x++;

//		return upKey || downKey || leftKey || rightKey;//只有松开所有键才视为无更新
//		return !dir.isZero();//同时按下左右或同时上下则视为无更新
		//当数据有变更时才视为无更新
		boolean isUpdated = !vector.equals(lastVector);
		setLastVector(vector);
		return isUpdated;
	}

	public boolean getActionUp() {
		return Gdx.input.isKeyPressed(Up.getKeycode());
	}
	public boolean getActionDown() {
		return Gdx.input.isKeyPressed(Down.getKeycode());
	}
	public boolean getActionLeft() {
		return Gdx.input.isKeyPressed(Left.getKeycode());
	}
	public boolean getActionRight() {
		return Gdx.input.isKeyPressed(Right.getKeycode());
	}

	@Override
	public boolean isHold() {
		return getActionUp() || getActionDown() || getActionLeft() || getActionRight();
	}
}
