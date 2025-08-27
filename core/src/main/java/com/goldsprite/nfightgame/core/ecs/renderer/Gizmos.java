package com.goldsprite.nfightgame.core.ecs.renderer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;
import java.util.ArrayList;
import java.util.List;

public class Gizmos {
	// 单例实例
	private static Gizmos instance;

	// 绘制配置
	private static Color currentColor = Color.WHITE;
	private static ShapeRenderer.ShapeType currentShapeType = ShapeRenderer.ShapeType.Line;

	// 形状基类
	private static abstract class ShapeData {
		Color color;
		ShapeRenderer.ShapeType shapeType;

		abstract void render(ShapeRenderer renderer);
	}

	// 圆形数据结构
	private static class CircleData extends ShapeData {
		float x, y, radius;

		CircleData set(float x, float y, float radius, Color color, ShapeRenderer.ShapeType shapeType) {
			this.x = x;
			this.y = y;
			this.radius = radius;
			this.color = color;
			this.shapeType = shapeType;
			return this;
		}

		@Override
		void render(ShapeRenderer renderer) {
			renderer.setColor(color);
			renderer.circle(x, y, radius);
		}
	}

	// 矩形数据结构
	private static class RectData extends ShapeData {
		float x, y, width, height;

		RectData set(float x, float y, float width, float height, Color color, ShapeRenderer.ShapeType shapeType) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.color = color;
			this.shapeType = shapeType;
			return this;
		}

		@Override
		void render(ShapeRenderer renderer) {
			renderer.setColor(color);
			renderer.rect(x-width/2, y-height/2, width, height);
		}
	}

	// 对象池
	private final Pool<CircleData> circlePool = new Pool<CircleData>() {
		@Override
		protected CircleData newObject() {
			return new CircleData();
		}
	};

	private final Pool<RectData> rectPool = new Pool<RectData>() {
		@Override
		protected RectData newObject() {
			return new RectData();
		}
	};

	private List<ShapeData> shapes = new ArrayList<>();
	private ShapeRenderer shapeRenderer;
	private Camera camera;

	// 获取单例实例
	public static Gizmos getInstance() {
		if (instance == null) {
			instance = new Gizmos();
		}
		return instance;
	}

	// 私有构造函数，初始化ShapeRenderer
	private Gizmos() {
		shapeRenderer = new ShapeRenderer();
	}

	// 设置相机（必须在render前调用）
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	// 设置当前颜色
	public static void setColor(Color color) {
		currentColor = color;
	}

	// 设置当前形状类型（空心/实心）
	public static void setHollow(boolean isHollow) {
		currentShapeType = isHollow ? ShapeRenderer.ShapeType.Line : ShapeRenderer.ShapeType.Filled;
	}

	// 静态方法：添加圆形到绘制队列
	public static void circle(float x, float y, float radius) {
		Gizmos renderer = getInstance();
		CircleData data = renderer.circlePool.obtain();
		data.set(x, y, radius, currentColor, currentShapeType);
		renderer.shapes.add(data);
	}

	// 静态方法：添加矩形到绘制队列
	public static void rect(float x, float y, float width, float height) {
		Gizmos renderer = getInstance();
		RectData data = renderer.rectPool.obtain();
		data.set(x, y, width, height, currentColor, currentShapeType);
		renderer.shapes.add(data);
	}

	// 绘制所有存储的形状
	public void render(float delta) {
		if (camera == null) {
			throw new IllegalStateException("Camera must be set before rendering.");
		}

		shapeRenderer.setProjectionMatrix(camera.combined);

		// 按形状类型分组绘制以减少状态切换
		renderByShapeType(ShapeRenderer.ShapeType.Filled);
		renderByShapeType(ShapeRenderer.ShapeType.Line);
		clear();
	}

	// 按形状类型分组绘制
	private void renderByShapeType(ShapeRenderer.ShapeType type) {
		boolean began = false;

		for (ShapeData shape : shapes) {
			if (shape.shapeType != type)
				continue;

			if (!began) {
				shapeRenderer.begin(type);
				began = true;
			}

			shape.render(shapeRenderer);
		}

		if (began) {
			shapeRenderer.end();
		}
	}

	// 清空所有形状
	public void clear() {
		for (ShapeData shape : shapes) {
			if (shape instanceof CircleData) {
				circlePool.free((CircleData) shape);
			} else if (shape instanceof RectData) {
				rectPool.free((RectData) shape);
			}
		}
		shapes.clear();
	}

	// 清理资源
	public void dispose() {
		shapeRenderer.dispose();
		circlePool.clear();
		rectPool.clear();
	}
}

