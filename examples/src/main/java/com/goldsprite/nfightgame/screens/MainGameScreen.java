package com.goldsprite.nfightgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.goldsprite.gdxcore.screens.GScreen;
import com.goldsprite.infinityworld.assets.GlobalAssets;
import com.goldsprite.nfightgame.core.Rocker;
import com.goldsprite.nfightgame.core.RoleControllerComponent;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.*;
import com.goldsprite.nfightgame.core.ecs.renderer.Gizmos;
import com.goldsprite.nfightgame.core.ecs.renderer.TextureRenderer;
import com.goldsprite.nfightgame.core.ecs.system.PhysicsSystem;
import com.goldsprite.utils.math.Vector2Int;

public class MainGameScreen extends GScreen {
	private TextureRenderer textureRenderer;
	private Gizmos gizmosRenderer;
	private PhysicsSystem physicsSystem;

	private GObject hero, wall, ground;

	private Skin uiSkin;
	private Stage uiStage;
	private Rocker rocker;

	@Override
	public void create() {
		createUI();

		createSystem();

		createGObjects();
	}

	private void createSystem() {
		textureRenderer = new TextureRenderer();
		textureRenderer.setCamera(getCamera());

		gizmosRenderer = Gizmos.getInstance();
		gizmosRenderer.setCamera(getCamera());

		physicsSystem = new PhysicsSystem();
	}

	private void createGObjects() {
		hero = new GObject();

		hero.transform.setFace(1, 1);
		hero.transform.setPosition(100, 250);
		hero.transform.setScale(1.5f);

		RigidbodyComponent rigi = hero.addComponent(new RigidbodyComponent());
		rigi.setGravity(true);

		TextureComponent texture = hero.addComponent(new TextureComponent());
		texture.setOriginOffset(119, 36);

		AnimatorComponent animator = hero.addComponent(new AnimatorComponent(texture));
		TextureRegion[] frames1 = splitFrames("hero/hero_sheet.png", 0, 3);
		TextureRegion[] frames2 = splitFrames("hero/hero_sheet.png", 1, 4);
		TextureRegion[] frames3 = splitFrames("hero/hero_sheet.png", 2, 2);
		Animation<TextureRegion> anim1 = new Animation<>(.25f, frames1);
		anim1.setPlayMode(Animation.PlayMode.LOOP);
		Animation<TextureRegion> anim2 = new Animation<>(.2f, frames2);
		anim2.setPlayMode(Animation.PlayMode.LOOP);
		Animation<TextureRegion> anim3 = new Animation<>(.2f, frames3);
		anim3.setPlayMode(Animation.PlayMode.NORMAL);
		animator.addAnim("idle", anim1);
		animator.addAnim("run", anim2);
		animator.addAnim("attack", anim3);

		CircleColliderComponent heroFootCollider = hero.addComponent(new CircleColliderComponent());
		heroFootCollider.setOffsetPosition(0, 15);
		heroFootCollider.setRadius(15);
		physicsSystem.addGObject(heroFootCollider);

		CircleColliderComponent heroAtkCollider = hero.addComponent(new CircleColliderComponent());
		heroAtkCollider.setTrigger(true);
		heroAtkCollider.setOffsetPosition(53, 60);
		heroAtkCollider.setRadius(24);
		physicsSystem.addGObject(heroAtkCollider);

		RoleControllerComponent roleController = hero.addComponent(new RoleControllerComponent());
		roleController.setRocker(rocker);
		roleController.setTarget(hero.transform);
		roleController.setSpeed(500);

		textureRenderer.addGObject(hero);


		wall = new GObject();
		wall.transform.setPosition(500, 300);

		RectColliderComponent wallCollider = wall.addComponent(new RectColliderComponent());
		wallCollider.setSize(100, 200);
		physicsSystem.addGObject(wallCollider);


		ground = new GObject();
		ground.transform.setPosition(400, 150);

		RectColliderComponent groundCollider = ground.addComponent(new RectColliderComponent());
		groundCollider.setSize(600, 40);
		physicsSystem.addGObject(groundCollider);
	}

	private void createUI() {
		uiSkin = GlobalAssets.getInstance().editorSkin;
		uiStage = new Stage();
		uiStage.setViewport(getViewport());
		getImp().addProcessor(uiStage);

		rocker = new Rocker(0, uiSkin);
		rocker.setPosition(20, 20);
		rocker.setSize(150, 150);
		uiStage.addActor(rocker);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(.7f, .7f, .7f, 1);

		gameLogic(delta);

		uiStage.act(delta);
		uiStage.draw();
	}

	private void gameLogic(float delta) {

		physicsSystem.update(delta);

		hero.update(delta);
		wall.update(delta);
		ground.update(delta);

		textureRenderer.render(delta);

		gizmosRenderer.render(delta);
	}

	public TextureRegion[] splitFrames(String path, int col, int count){
		Texture hero_sheet = new Texture(Gdx.files.internal(path));
		Vector2Int cell = new Vector2Int(256, 256);
		TextureRegion[] frames = new TextureRegion[count];
		for (int i = 0; i < count; i++) {
			frames[i] = new TextureRegion(hero_sheet, i*cell.x, col*cell.y, cell.x, cell.y);
		}
		return frames;
	}

}
