package com.goldsprite.gdxcore.ui;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class GWindow extends Window {
	public static int editorWindowResizeBorderSize = 20;

	public float safeDistance = 15;
	public Actor limitTarget;

	public GWindow(String title, Skin skin){
		super(title, skin);
		setResizeBorder(editorWindowResizeBorderSize);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		setKeepWithinParent();
	}

	public void setKeepWithinParent(){
		if(getParent() == null || limitTarget == null) return;

		if(!(limitTarget.getWidth() <= 100+safeDistance*2 || limitTarget.getHeight() <= 75+safeDistance*2)){
			//限制最大
			float limitWidth = MathUtils.clamp(getWidth(), 100, limitTarget.getWidth()-safeDistance*2);
			float limitHeight = MathUtils.clamp(getHeight(), 75, limitTarget.getHeight()-safeDistance*2);
			setSize(limitWidth, limitHeight);
			//LogViewerService.log("Window大小限制: width: %s, height: %s", limitWidth, limitHeight);

			//限制位置不越界父系
			float limitX = MathUtils.clamp(getX(), limitTarget.getX()+safeDistance, limitTarget.getRight()-getWidth()-safeDistance);
			float limitY = MathUtils.clamp(getY(), limitTarget.getY()+safeDistance, limitTarget.getTop()-getHeight()-safeDistance);
			setPosition(limitX, limitY);
		}
	}

}
