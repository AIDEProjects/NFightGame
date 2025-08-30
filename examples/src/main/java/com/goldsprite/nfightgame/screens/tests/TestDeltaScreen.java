package com.goldsprite.nfightgame.screens.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.goldsprite.gdxcore.screens.GScreen;

//三个球, 第一个每帧移动并使用delta插值预计每秒30像素, 第二个每帧移动1像素60帧率每秒移动60像素, 第三个控制时间步长每秒只运行30次也是30像素
public class TestDeltaScreen extends GScreen {
	private ShapeRenderer shapeRenderer;

	// 使用deltaTime的物体
	private float deltaObjectX = 50;
	private float deltaObjectY = 300;
	private final float DELTA_OBJECT_SPEED = 30; // 像素/秒

	// 不使用deltaTime的物体（基于帧数）
	private float frameObjectX = 50;
	private float frameObjectY = 200;
	private final float FRAME_OBJECT_SPEED = 1; // 像素/帧

	// 固定时间步长的物体
	private float fixedObjectX = 50;
	private float fixedObjectY = 100;
	private final float FIXED_OBJECT_SPEED = 30; // 像素/秒
	private float accumulator = 0;
	private final float FIXED_TIME_STEP = 1/10f; // 60 FPS的固定时间步长

	// 帧率控制
	private long lastFrameTime;
	private long targetFrameTime = (long)(1000/60f); // 默认60FPS (1000ms/60 ≈ 16ms)
	private boolean enableFrameRateControl = true;

	// 性能信息
	private float fps;
	private long frameCount;
	private long lastFpsUpdate;

	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();
		lastFrameTime = TimeUtils.millis();
		lastFpsUpdate = TimeUtils.millis();
	}

	float vx1, vx2, vx3;
	@Override
	public void render(float delta) {
		// 控制帧率（如果启用）
		if (enableFrameRateControl) {
			while (TimeUtils.timeSinceMillis(lastFrameTime) < targetFrameTime) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		lastFrameTime = TimeUtils.millis();

		// 计算当前帧率
		frameCount++;
		if (TimeUtils.timeSinceMillis(lastFpsUpdate) > 1000) {
			fps = frameCount;
			frameCount = 0;
			lastFpsUpdate = TimeUtils.millis();
		}

		float deltaTime = Gdx.graphics.getDeltaTime();

		// 更新使用deltaTime的物体
		
		vx1 += DELTA_OBJECT_SPEED * deltaTime*deltaTime*2;
		deltaObjectX += vx1;
		if (deltaObjectX > Gdx.graphics.getWidth()) {
			deltaObjectX = 50;
			vx1 = 0;
		}

		// 更新不使用deltaTime的物体（基于帧数）
		frameObjectX += FRAME_OBJECT_SPEED;
		if (frameObjectX > Gdx.graphics.getWidth()) {
			frameObjectX = 50;
		}

		// 使用固定时间步长更新物体
		accumulator += deltaTime;
		while (accumulator >= FIXED_TIME_STEP) {
			vx3 += DELTA_OBJECT_SPEED * FIXED_TIME_STEP /60f*2;
			fixedObjectX += vx3;
			if (fixedObjectX > Gdx.graphics.getWidth()) {
				fixedObjectX = 50;
			}
			accumulator -= FIXED_TIME_STEP;
		}

		// 清屏
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// 绘制物体
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		// 绘制使用deltaTime的物体（绿色）
		shapeRenderer.setColor(0, 1, 0, 1);
		shapeRenderer.circle(deltaObjectX, deltaObjectY, 20);

		// 绘制不使用deltaTime的物体（红色）
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.circle(frameObjectX, frameObjectY, 20);

		// 绘制使用固定时间步长的物体（蓝色）
		shapeRenderer.setColor(0, 0, 1, 1);
		shapeRenderer.circle(fixedObjectX, fixedObjectY, 20);

		shapeRenderer.end();

		// 绘制信息
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.line(50, 350, Gdx.graphics.getWidth() - 50, 350);
		shapeRenderer.end();

		// 显示帧率和说明
		Gdx.graphics.setTitle("DeltaTime Test - FPS: " + (int)fps +
			" | Delta: " + deltaTime +
			" | 绿:deltaTime 红:基于帧 蓝:固定步长");
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

	// 用于控制帧率的方法
	public void setTargetFPS(int fps) {
		targetFrameTime = 1000 / fps;
	}

	public void enableFrameRateControl(boolean enable) {
		enableFrameRateControl = enable;
	}
}
