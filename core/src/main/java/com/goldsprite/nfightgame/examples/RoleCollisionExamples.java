/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.examples;
import com.goldsprite.gdxcore.screens.*;
import com.badlogic.gdx.*;
import com.goldsprite.nfightgame.core.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.MathUtils;
import com.goldsprite.utils.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.goldsprite.infinityworld.assets.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class RoleCollisionExamples extends GScreen {
	//private GObject role1;
	//private GObject obj1;
	private ShapeRenderer sr;
	private float cx = 200, cy = 200, r = 50, last_cx, last_cy;
	private float velX, velY;
	private float r2x = 200, r2y = 200, r2w = 120, r2h = 150;

	private float rx = 400, ry = 200, rw = 120, rh = 150;
	private float c2x = 400, c2y = 200, r2 = 50;

	private String skinPath = "ui_skins/shade/skin/uiskin.atlas";
	private Skin uiSkin;
	private TextButton cgBtn;
	private Stage uiStage;
	private int mode = 0;

	@Override
	public void create() {
		getImp().addProcessor(new InputAdapter() {
			Vector2 screenCoord = new Vector2();

			@Override
			public boolean touchDown(int x, int y, int p, int b) {
				float gWidth = Gdx.graphics.getWidth();
				float gHeight = Gdx.graphics.getHeight();
				if(mode == 0){
					float vel = 100 * 1/60f;
					boolean up = y<gHeight/2;
					boolean left = x<gWidth/2;
					boolean down = y>gHeight/2;
					boolean right = x>gWidth/2;
					if(left && down) velX = -vel;
					if(right && down) velX = vel;
					if(up && left) velY = -vel;
					if(up && right) velY = vel;
				}
				return false;
			}

			@Override
			public boolean touchUp(int x, int y, int p, int b) {
				velX = velY = 0;
				return false;
			}

			public boolean touchDragged(int x, int y, int p) {
				screenCoord.set(x, y);
				Vector2 worldCoord = screenToWorldCoord(screenCoord);
				if (/*mode == 0 || */mode == 1) {
					last_cx = cx;
					last_cy = cy;
					cx = worldCoord.x-150;
					cy = worldCoord.y;
				}
				if (mode == 2) {
					r2x = worldCoord.x -150;
					r2y = worldCoord.y;
				}
				return false;
			}
		});
		sr = new ShapeRenderer();

		uiSkin = GlobalAssets.getInstance().editorSkin;
		uiStage = new Stage();
		uiStage.setViewport(getViewport());
		getImp().addProcessor(uiStage);

		cgBtn = new TextButton("切换", uiSkin);
		cgBtn.setPosition(50, getViewSize().y - 120);
		cgBtn.setSize(80, 60);
		cgBtn.addListener(new ClickListener(){
				public void clicked(InputEvent e, float x, float y){
					mode++;
					mode%=3;
				}
			});

		uiStage.addActor(cgBtn);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.7f, 0.7f, 0.7f, 1f);

		sr.setProjectionMatrix(getCamera().combined);
		sr.begin(ShapeRenderer.ShapeType.Filled);

		if(mode == 0){
			last_cx = cx;
			last_cy = cy;
			cx += velX;
			cy += velY;
		}

		boolean isColl = false;
		if (mode == 0){
			isColl = circleToRectCollision();
			if(isColl){
				cx = last_cx;
				cy = last_cy;
			}
		}
		if (mode == 1)
			isColl = circleToCircleCollision();
		if (mode == 2)
			isColl = rectToRectCollision();

		sr.setColor(Color.PURPLE);
		if (mode == 0 || mode == 2)
			sr.rect(rx - rw / 2, ry - rh / 2, rw, rh);
		if (mode == 1)
			sr.circle(c2x, c2y, r2);

		sr.setColor(isColl ? Color.RED : Color.YELLOW);
		if (mode == 0 || mode == 1)
			sr.circle(cx, cy, r);
		if (mode == 2)
			sr.rect(r2x - r2w / 2, r2y - r2h / 2, r2w, r2h);

		sr.end();

		uiStage.act(delta);
		uiStage.draw();
	}

	private boolean rectToRectCollision() {
		//AABB
		float minX = rx - rw / 2, maxX = rx + rw / 2, minY = ry - rh / 2, maxY = ry + rh / 2;
		float min2X = r2x - r2w / 2, max2X = r2x + r2w / 2, min2Y = r2y - r2h / 2, max2Y = r2y + r2h / 2;
		return !(minX > max2X || min2X > maxX || minY > max2Y || min2Y > maxY);
	}

	private boolean circleToCircleCollision() {
		float disX = cx - c2x;
		float disY = cy - c2y;
		float distance = (float) Math.sqrt(disX * disX + disY * disY);

		return distance < r + r2;
	}

	private boolean rectToCircleCollision() {
		return circleToRectCollision();
	}

	private boolean circleToRectCollision() {
		float closestX = MathUtils.clamp(cx, rx - rw / 2, rx + rw / 2);
		float closestY = MathUtils.clamp(cy, ry - rh / 2, ry + rh / 2);

		float distance = (float) Math.sqrt(Math.pow(cx - closestX, 2) + Math.pow(cy - closestY, 2));

		return distance < r;
	}

}

