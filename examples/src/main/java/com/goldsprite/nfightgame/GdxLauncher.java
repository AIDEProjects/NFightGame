package com.goldsprite.nfightgame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.*;
import com.goldsprite.gdxcore.screens.*;
import com.goldsprite.infinityworld.assets.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.goldsprite.nfightgame.screens.MainGameScreen;
import com.goldsprite.nfightgame.screens.*;
import com.goldsprite.nfightgame.screens.tests.TestDeltaScreen;

public class GdxLauncher extends ApplicationAdapter {
	private ScreenManager screenManager;
	private Stage uiStage;
	private InputMultiplexer imp;
	private Skin uiSkin;

	@Override
	public void create() {
		new GlobalAssets();
		screenManager = new ScreenManager();
		screenManager.setViewport(new FitViewport(960, 540, new OrthographicCamera()));
//		screenManager.setCurScreen(RoleRendererExamples.class, true);
//		screenManager.setCurScreen(RoleCollisionExamples.class, true);
		screenManager.setCurScreen(MainGameScreen.class, true);
//	screenManager.setCurScreen(TestDeltaScreen.class, true);


		/*imp = new InputMultiplexer();
		uiSkin = GlobalAssets.getInstance().editorSkin;
		uiStage = new Stage();
		imp.addProcessor(uiStage);
		screenManager.enableInput(imp);

		TextButton textBtn = new TextButton("kk", uiSkin);
		uiStage.addActor(textBtn);*/
	}

	@Override
	public void render() {
		screenManager.render();

		/*uiStage.act();
		uiStage.draw();*/
	}

	@Override
	public void resize(int width, int height) {
		screenManager.resize(width, height);
	}
}

