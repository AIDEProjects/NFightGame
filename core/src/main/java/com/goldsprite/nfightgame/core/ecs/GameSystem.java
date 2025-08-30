package com.goldsprite.nfightgame.core.ecs;

import com.badlogic.gdx.graphics.Camera;
import com.goldsprite.nfightgame.core.ecs.component.ColliderComponent;
import com.goldsprite.nfightgame.core.ecs.component.IComponent;
import com.goldsprite.nfightgame.core.ecs.component.SpriteComponent;
import com.goldsprite.nfightgame.core.ecs.system.PhysicsSystem;
import com.goldsprite.nfightgame.core.ecs.renderer.Gizmos;
import com.goldsprite.nfightgame.core.ecs.renderer.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.utils.viewport.*;

public class GameSystem {
	private static GameSystem instance;

	private Viewport viewport;
	private SpriteRenderer spriteRenderer;
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
		spriteRenderer = new SpriteRenderer();
		gizmosRenderer = new Gizmos();
		physicsSystem = new PhysicsSystem();
	}

	public static GameSystem getInstance() {
		return instance;
	}

	public static void manageGameComponent(IComponent component, ManageMode mode) {
		if (component instanceof SpriteComponent) {
			switch (mode) {
				case ADD:
					getInstance().spriteRenderer.addGObject(component);
					break;
				case REMOVE:
					getInstance().spriteRenderer.removeGObject(component);
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
		physicsSystem.update(delta);

		for (GObject obj : gobjects) {
			obj.update(delta);
		}

		spriteRenderer.update(delta);
		gizmosRenderer.update(delta);
	}

	public Camera getCamera() {
		return viewport.getCamera();
	}
	public Viewport getViewport() {
		return viewport;
	}

	public void setViewport(Viewport viewport) {
		this.viewport = viewport;
	}

	public SpriteRenderer getTextureRenderer() {
		return spriteRenderer;
	}

	public Gizmos getGizmosRenderer() {
		return gizmosRenderer;
	}

	public PhysicsSystem getPhysicsSystem() {
		return physicsSystem;
	}
}
