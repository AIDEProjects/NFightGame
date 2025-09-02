package com.goldsprite.nfightgame.inputs;

import com.goldsprite.nfightgame.inputs.widgets.VirtualButton;

public class VirtualButtonBinding extends KeyBinding {
	private VirtualButton virtualButton;

	public void setVirtualButton(VirtualButton virtualButton) {
		this.virtualButton = virtualButton;
	}

	@Override
	public boolean getKeyPressed() {
		return virtualButton != null && virtualButton.isPressed();
	}

	@Override
	public boolean matchesKey(int code) {
		return false;
	}
}
