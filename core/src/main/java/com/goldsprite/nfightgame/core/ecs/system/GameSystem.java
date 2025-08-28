package com.goldsprite.nfightgame.core.ecs.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.ColliderComponent;
import com.goldsprite.nfightgame.core.ecs.component.IComponent;
import com.goldsprite.nfightgame.core.ecs.component.TextureComponent;
import com.goldsprite.nfightgame.core.ecs.system.manager.PhysicsSystem;
import com.goldsprite.nfightgame.core.ecs.system.renderer.Gizmos;
import com.goldsprite.nfightgame.core.ecs.system.renderer.TextureRenderer;

import java.util.ArrayList;
import java.util.List;

public class GameSystem {
	private static GameSystem instance;

	private Camera camera;
	private TextureRenderer textureRenderer;
	private Gizmos gizmosRenderer;
	private PhysicsSystem physicsSystem;
	private List<GObject> gobjects = new ArrayList<GObject>();

	public static final float STEP = 1f / 60f; // 固定逻辑帧率 60Hz
	private static final float MAX_DELTA = 0.25f; // 最大累积时间，避免卡死

	private float accumulator = 0f;
	private float alpha = 0f; // 插值参数

	public GameSystem() {
		instance = this;
		createSystem();
	}

	private void createSystem() {
		textureRenderer = new TextureRenderer();
		gizmosRenderer = new Gizmos();
		physicsSystem = new PhysicsSystem();
	}

	public static GameSystem getInstance() {
		return instance;
	}

	public static void manageGameComponent(IComponent component, ManageMode mode) {
		if (component instanceof TextureComponent) {
			switch (mode) {
				case ADD:
					getInstance().textureRenderer.addGObject(component.getGObject());
					break;
				case REMOVE:
					getInstance().textureRenderer.removeGObject(component.getGObject());
					break;
			}
		}
		if (component instanceof ColliderComponent) {
			switch (mode) {
				case ADD:
					getInstance().physicsSystem.addGObject((ColliderComponent) component);
					break;
				case REMOVE:
					getInstance().physicsSystem.removeGObject((ColliderComponent) component);
					break;
			}
		}
	}

	public static void manageGObject(GObject gobject, ManageMode mode) {
		switch (mode) {
			case ADD:
				getInstance().gobjects.add(gobject);
				break;
			case REMOVE:
				getInstance().gobjects.remove(gobject);
				break;
		}
	}

	public void gameLoop(float delta) {
//		delta = 1/120f;

		// 1. 获取本帧delta，限制最大值（避免长时间挂起导致跳几秒逻辑）
		delta = Math.min(delta, MAX_DELTA);
		// 2. 累加时间
		accumulator += delta;
		// 3. 逻辑步进
		while (accumulator >= STEP) {
			fixedUpdate(STEP); // 固定逻辑步长
			accumulator -= STEP;
		}
//		// 4. 计算插值因子 (0~1)，用于渲染时平滑
//		alpha = delta / STEP;
		// 5. 渲染（带插值）
		update(delta);
	}

	private void fixedUpdate(float fixedDelta) {
		physicsSystem.fixedUpdate(fixedDelta);

		for (GObject obj : gobjects) {
			obj.fixedUpdate(fixedDelta);
		}
	}

	private void update(float delta) {

		for (GObject obj : gobjects) {
			obj.update(delta);
		}

		textureRenderer.update(delta);
		gizmosRenderer.update(delta);
	}

	// getters / setters
	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public TextureRenderer getTextureRenderer() {
		return textureRenderer;
	}

	public Gizmos getGizmosRenderer() {
		return gizmosRenderer;
	}

	public PhysicsSystem getPhysicsSystem() {
		return physicsSystem;
	}
}
