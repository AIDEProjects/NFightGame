package com.goldsprite.nfightgame.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.nfightgame.assets.GlobalAssets;
import com.goldsprite.nfightgame.screens.examples.ExampleSelectScreen;
import com.goldsprite.nfightgame.screens.games.EngineEditorScreen;
import com.goldsprite.nfightgame.screens.games.MainGameScreen;
import com.goldsprite.utils.math.Vector2;

public class StartScreen extends GScreen {
	private static float WORLD_WIDTH;
	private static float WORLD_HEIGHT;
	private OrthographicCamera cam; // 用于绘制实体的相机
	private SpriteBatch batch; // 绘制实体的 Batch
	private Stage uiStage; // 用于绘制 UI 的 Stage
	private Skin uiSkin; // UI 的皮肤
	private Texture tex;

	private StartScreen.SplashSimpleAnim anim;

	private Class<? extends GScreen>
		gameScreen = MainGameScreen.class,
		editorScreen = EngineEditorScreen.class,
		examplesScreen = ExampleSelectScreen.class;

	private float yOff = 100;

	@Override
	public void create() {
		WORLD_WIDTH = getViewport().getWorldWidth();
		WORLD_HEIGHT = getViewport().getWorldHeight();
		cam = (OrthographicCamera) getViewport().getCamera();

		// 初始化 Batch
		batch = new SpriteBatch();

		//创建logo材质
		tex = new Texture("greetings.png");

		//创建渐变动画
		anim = new SplashSimpleAnim();
		anim.onCompleted = () -> {
		};

		//ui
		uiStage = new Stage(getViewport());
		getImp().addProcessor(uiStage);

		// 创建简单的 UI 元素
		createUI();
	}

	private void createUI() {
		// 加载 UI 的 Skin
		uiSkin = GlobalAssets.getInstance().defaultSkin;
		TextButton.TextButtonStyle textButtonStyle = uiSkin.get(TextButton.TextButtonStyle.class);
		BitmapFont fnt = GlobalAssets.getInstance().editorFont;
		textButtonStyle.font = fnt;

		float btnWidth = WORLD_WIDTH * 0.25f;
		float btnHeight = WORLD_HEIGHT * 0.1f;
		float btnPosX = WORLD_WIDTH / 2 + WORLD_WIDTH * 0.1f;
		float btnPosY = WORLD_HEIGHT / 2 - WORLD_HEIGHT / 5;

		TextButton button = new TextButton("进入游戏", uiSkin);
		button.setSize(btnWidth, btnHeight);
		button.setPosition(btnPosX, btnPosY+yOff, Align.center);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getScreenManager().setCurScreen(gameScreen, true);
			}
		});

		TextButton button3 = new TextButton("进入编辑器", uiSkin);
		button3.setSize(btnWidth, btnHeight);
		button3.setPosition(btnPosX, btnPosY - WORLD_HEIGHT/7 + yOff, Align.center);
		button3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getScreenManager().setCurScreen(editorScreen, true);
			}
		});

		TextButton button2 = new TextButton("进入演示选择", uiSkin);
		button2.setSize(btnWidth, btnHeight);
		button2.setPosition(btnPosX, btnPosY - WORLD_HEIGHT/7*2 + yOff, Align.center);
		button2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getScreenManager().setCurScreen(examplesScreen, true);
			}
		});

		uiStage.addActor(button);
		uiStage.addActor(button2);
		uiStage.addActor(button3);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		//绘制世界实体内容
		{
			// 更新相机
			cam.update();
			batch.setProjectionMatrix(cam.combined);

			// 绘制实体
			batch.begin();
			float scl = 0.18f;
			Vector2 texSize = new Vector2(tex.getWidth(), tex.getHeight()).scl(scl);
			drawWithAlpha(delta, batch, () -> {
				batch.draw(tex, WORLD_WIDTH / 2f - texSize.x / 2, WORLD_HEIGHT / 2f - texSize.y / 2 + yOff, texSize.x, texSize.y);
			});
			batch.end();

		}

		// 绘制 UI
		uiStage.act(delta);
		uiStage.draw();
	}

	private void drawWithAlpha(float delta, Batch batch, Runnable runnable) {
		Color color = batch.getColor();
		float oldA = color.a;
		color.a = anim.step(delta);
		batch.setColor(color);
		if (runnable != null) runnable.run();
		color.a = oldA;
		batch.setColor(color);
	}

	@Override
	public void resize(int width, int height) {
		// 更新 Viewport 尺寸
		if(uiStage !=null) uiStage.getViewport().update(width, height, true);
	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void dispose() {
		// 清理资源
		batch.dispose();
		uiStage.dispose();
	}

	/**
	 * 渐变动画:
	 */
	static class SplashSimpleAnim {
		float duration = 1.4f*1.5f;//秒
		float t;
		float startR = 0;
		float endR = 1f;
		Runnable onCompleted;
		boolean isCompleted;

		public float step(float delta) {
			if (t > duration) return endR;
			t += delta;

			float normalTime = Math.min(1, t / duration);
			float interpolation = lerp(startR, endR, normalTime);
			if (normalTime == 1) {
				isCompleted = true;
				if (onCompleted != null) onCompleted.run();
			}
			return interpolation;
		}

		public void reset() {
			t = 0;
		}

		public float lerp(float a, float b, float t) {
			return a + (b - a) * t;
		}
	}

}
