/**
 * @Author 
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.*;
import com.goldsprite.gdxcore.screens.*;
import com.goldsprite.infinityworld.assets.*;
import com.badlogic.gdx.utils.*;

public class FBOExampleScreen extends GScreen
{
	private Skin skin;
	private SpriteBatch bufBatch;
	private Texture texture;
	private TextureRegion region;
	private FrameBuffer frameBuffer;
	private OrthographicCamera camera;
	private Viewport viewport;

	private static final float SCREEN_WIDTH = 960;
	private static final float SCREEN_HEIGHT = 540;
	private static final float MARGIN = 20f;

	private Texture avatar;

	private Stage stage;

	@Override
	public void create() {
		skin = new Skin(Gdx.files.internal(GlobalAssets.skinPath));
		avatar = new Texture("shikeik_avatar.png");

		// 创建相机
		camera = new OrthographicCamera();
		viewport = new FitViewport(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, camera);
		
		stage = new Stage(viewport);
		getImp().addProcessor(stage);
		{
			Window window = new Window("Window", skin);
			//window.setFillParent(true);
			window.setMovable(true);
			window.setResizable(true);
			window.bottom().left();
			window.setSize(240, 240);

			MyWidget widget = new MyWidget();
			widget.init();
			//window.add(widget).expand().fill();

			stage.addActor(window);
		}
	}

	@Override
	public void render(float delta) {
		// 清除屏幕
		Gdx.gl.glClearColor(0.35f, 0.35f, 0.35f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render(delta);
	
		stage.act(delta);
		Batch batch = stage.getBatch();
		batch.begin();
		batch.draw(region, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
		batch.end();
		stage.draw();

	}

	public class MyWidget extends Widget {

		public void init(){
			// 创建 FrameBuffer 和纹理
			frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 512, 512, false); // 创建 512x512 的 FrameBuffer
			texture = frameBuffer.getColorBufferTexture();  // 获取 FrameBuffer 渲染后的纹理
			region = new TextureRegion(texture);
			region.flip(false, true);

			bufBatch = new SpriteBatch();
		}

		@Override
		public void act(float current) {
			// 渲染到 FrameBuffer
			{
				viewport.apply();
				frameBuffer.begin();
				ScreenUtils.clear(0,0,0,0);// 清空之前的内容
				bufBatch.setProjectionMatrix(camera.combined);
				bufBatch.begin();
				bufBatch.setColor(1f, 0f, 0f, 1f);
				bufBatch.draw(avatar, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight()/2);//左下1/4
				bufBatch.end();
				frameBuffer.end();  // 渲染结束
				getViewport().apply(true);
			}
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.draw(region, getX()+getWidth()/2, getY()+getHeight()/2, getWidth()/2, getHeight()/2);
		}

	}

}

