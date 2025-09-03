package com.goldsprite.nfightgame.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.goldsprite.utils.math.Vector2;

public class Rocker extends Touchpad {
	private final Vector2 value = new Vector2();

	public Rocker(float deadzoneRadius, Skin skin) {
		super(deadzoneRadius, skin);
	}

	public Vector2 getValue() {
		return value.set(getKnobPercentX(), getKnobPercentY());
	}
}
