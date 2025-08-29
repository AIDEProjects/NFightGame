/**
 * @Author 
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core;
import com.goldsprite.nfightgame.core.ecs.component.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.utils.*;
import com.goldsprite.nfightgame.core.ecs.system.*;

public class HealthBarComponent extends SpriteComponent {
	private TextureRegion[] regions;
	private static SpriteBatch bufBatch = new SpriteBatch();
	private OrthographicCamera cam;
	private FrameBuffer fb;
	private Texture renderTex;
	private TextureRegion renderRegion;
	private boolean isDirty = true;
	
	public void initHealthBar(int texWidth, int texHeight) {
		cam = new OrthographicCamera();
		cam.setToOrtho(false, texWidth, texHeight);
		fb = new FrameBuffer(Pixmap.Format.RGBA8888, texWidth, texHeight, false);
		renderTex = fb.getColorBufferTexture();
		renderRegion = new TextureRegion(renderTex);
	}

	public void loadRegions(String[] pathes) {
		regions = new TextureRegion[3];
		for (int i = 0; i < regions.length; i++) {
			regions[i] = new TextureRegion(new Texture(Gdx.files.internal(pathes[i])));
		}
	}

	@Override
	public void update(float delta) {
		if (isDirty) {
			flushRenderTexture();
			isDirty = false;
		}
	}

	private void flushRenderTexture() {
		fb.begin();
		ScreenUtils.clear(Color.WHITE);
		
		cam.update();
		bufBatch.setProjectionMatrix(cam.combined);
		bufBatch.begin();
		for(TextureRegion region : regions){
			bufBatch.draw(region, 0, 0, region.getRegionWidth(), region.getRegionHeight());
		}
		bufBatch.end();
		
		fb.end();
		
		renderTex = fb.getColorBufferTexture();
		renderRegion.setRegion(renderTex);
		
		GameSystem.getInstance().getViewport().apply(true);
	}
	
	public void setDirty(boolean dirty){
		isDirty = dirty;
	}
	
	public boolean isDirty(){
		return isDirty;
	}
	
	public TextureRegion getRenderTexture(){
		return renderRegion;
	}

	@Override
	public TextureRegion getRegion(){
		return renderRegion;
	}

}

