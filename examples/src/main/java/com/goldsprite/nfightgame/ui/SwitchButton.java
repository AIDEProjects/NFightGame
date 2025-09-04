package com.goldsprite.nfightgame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SwitchButton extends Table {
	private boolean checked = false;
	private final Image knob;
	private final Image bg;

	public SwitchButton(Skin skin) {
		super(skin);

		// 背景
		bg = new Image(skin.newDrawable("white", Color.GRAY));
		bg.setSize(100, 50);
		bg.setColor(Color.GRAY);

		// 圆点
		knob = new Image(skin.newDrawable("white", Color.WHITE));
		knob.setSize(40, 40);

		Stack stack = new Stack();
		stack.add(bg);
		stack.add(knob);

		this.add(stack).size(100, 50);

		// 初始 knob 在左
		knob.setPosition(5, 5);

		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				toggle();
			}
		});
	}

	public void toggle() {
		checked = !checked;
		float targetX = checked ? 55 : 5; // 右侧 or 左侧
		bg.setColor(checked ? Color.GREEN : Color.GRAY);

		knob.addAction(Actions.moveTo(targetX, 5, 0.2f, Interpolation.smooth));
	}

	public boolean isChecked() {
		return checked;
	}
}

