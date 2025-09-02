package com.goldsprite.nfightgame.inputs.widgets;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class VirtualButton extends TextButton {
	private Enum virtualKey;

	public VirtualButton(String txt, Skin skin) {
		super(txt, skin);

		addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//				InputManager.getInstance().actionDown(virtualKey);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				InputManager.getInstance().actionUp(virtualKey);
			}
		});
	}

	@Override
	public void act(float delta) {
//		if(isPressed()) InputManager.getInstance().actionHold(virtualKey);
	}

	public Enum getVirtualKey() {
		return virtualKey;
	}
	public void bindVirtualKey(Enum virtualKey) {
		this.virtualKey = virtualKey;
	}
}
