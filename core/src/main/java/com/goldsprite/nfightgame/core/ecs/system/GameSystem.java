package com.goldsprite.nfightgame.core.ecs.system;

import com.goldsprite.nfightgame.core.ecs.renderer.*;
import com.badlogic.gdx.graphics.*;
import com.goldsprite.nfightgame.core.ecs.component.*;

public class GameSystem {
	private static GameSystem instance;
	private static Camera camera;
	private TextureRenderer textureRenderer;
	private Gizmos gizmosRenderer;
	private PhysicsSystem physicsSystem;

	private GameSystem() {
		instance = this;
		createSystem();
	}

	private void createSystem() {
		textureRenderer = new TextureRenderer();
		textureRenderer.setCamera(getCamera());

		gizmosRenderer = Gizmos.getInstance();
		gizmosRenderer.setCamera(getCamera());

		physicsSystem = new PhysicsSystem();
	}

	public static void setCamera(Camera cam) {
		camera = cam;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setPhysicsSystem(PhysicsSystem physicsSystem) {
		this.physicsSystem = physicsSystem;
	}

	public PhysicsSystem getPhysicsSystem() {
		return physicsSystem;
	}

	public void setGizmosRenderer(Gizmos gizmosRenderer) {
		this.gizmosRenderer = gizmosRenderer;
	}

	public Gizmos getGizmosRenderer() {
		return gizmosRenderer;
	}

	public void setTextureRenderer(TextureRenderer textureRenderer) {
		this.textureRenderer = textureRenderer;
	}

	public TextureRenderer getTextureRenderer() {
		return textureRenderer;
	}

	public static GameSystem getInstance() {
		if (instance == null)
			new GameSystem();
		return instance;
	}

	public static void manageGameComponent(IComponent component, String mode) {
		switch (mode) {
			case "add" : {
					if (component instanceof TextureComponent) {
						getInstance().textureRenderer.addGObject(component.getGObject());
					}
				break;
			}
			case "remove" : {
					if (component instanceof TextureComponent) {
						getInstance().textureRenderer.removeGObject(component.getGObject());
					}
				break;
			}
		}

	}

}

