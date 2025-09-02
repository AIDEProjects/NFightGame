package com.goldsprite.nfightgame.inputs;

import com.badlogic.gdx.Gdx;

public class KeyBinding extends InputBinding {
	private int keyCode;
	private boolean lastValue;

	public KeyBinding() {}
	public KeyBinding(int keyCode) {
		this.keyCode = keyCode;
	}

	@Override
	public boolean matchesKey(int code) {
		return keyCode == code;
	}


	/**
	 * @return 表示该绑定是否还有输入
	 */
	@Override
	public boolean updateValue() {
		boolean down = getKeyPressed();
		action.setValue(down);

		//按下视为有更新
//		return down;
		//数据变更时视为有更新
		boolean isModified = down != lastValue;
		lastValue = down;
		return isModified;
	}

	public boolean getKeyPressed() {
		return Gdx.input.isKeyPressed(keyCode);
	}

	@Override
	public boolean isHold() {
		return getKeyPressed();
	}

	public int getKeycode() {
		return keyCode;
	}
}

