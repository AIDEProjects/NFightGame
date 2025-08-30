package com.goldsprite.nfightgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.goldsprite.gdxcore.screens.ScreenManager;
import com.goldsprite.infinityworld.assets.GlobalAssets;
import com.goldsprite.nfightgame.screens.MainGameScreen;
import com.goldsprite.nfightgame.screens.tests.TestDeltaScreen;
import com.goldsprite.nfightgame.screens.tests.TestFsmScreen;

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
		screenManager.setCurScreen(MainGameScreen.class, true);
//		screenManager.setCurScreen(TestDeltaScreen.class, true);
//		screenManager.setCurScreen(TestFsmScreen.class, true);


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

