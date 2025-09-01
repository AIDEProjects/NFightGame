package com.goldsprite.nfightgame.inputs;

public class KeyListener {
	IOnKeyDownCallback down;
	IOnKeyUpCallback up;
	IOnKeyHoldCallback hold;

	public KeyListener onDown(IOnKeyDownCallback d) { this.down = d; return this; }
	public KeyListener onUp(IOnKeyUpCallback u) { this.up = u; return this; }
	public KeyListener onHold(IOnKeyHoldCallback h) { this.hold = h; return this; }
}
