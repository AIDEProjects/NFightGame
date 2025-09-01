package com.goldsprite.gdxcore.ui;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.goldsprite.gdxcore.logs.LogViewerService;

public class GStage extends Stage{
	private LogViewerService logViewer;

	public GStage(){
		super();
		init();
	}
	public GStage(Viewport viewport) {
		super(viewport);
		init();
	}

	public GStage(Viewport viewport, Batch batch) {
		super(viewport, batch);
		init();
	}

	private void init(){
		//logViewer = LogViewerService.getInstance();
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		boolean handle = super.touchDown(screenX, screenY, pointer, button);
		//if(logViewer!= null) logViewer.logDebug("舞台按下事件: screenX: %s, screenY: %s", screenX, screenY);
		return handle;
	}

}
