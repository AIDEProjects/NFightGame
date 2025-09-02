package com.goldsprite.nfightgame.inputs;

public abstract class InputBinding {
	protected InputAction action;

	public void setInputAction(InputAction action) {
		this.action = action;
	}

	public boolean matchesKey(int keyboardCode) {
		return false;
	}

	/**
	 * @return 是否变更了值
	 */
	public abstract boolean updateValue();

	/**
	 * @return 是否有任意输入
	 */
	public abstract boolean isHold();
}
