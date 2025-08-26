package com.goldsprite.nfightgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goldsprite.utils.math.Vector2;

public class TexRoleRenderer {
	private final SpriteBatch batch;

	public TexRoleRenderer(SpriteBatch batch) {
		this.batch = batch;
	}

	public Vector2 flipFootOffset = new Vector2();//脚底实际偏移
	public void drawOnFoot(TexRole role) {
		//翻转人物
		boolean flipX, flipY;
		if(role.face.x >= 0){
			flipX = role.region.isFlipX();
		}else
			flipX = !role.region.isFlipX();
		if(role.face.y >= 0){
			flipY = role.region.isFlipY();
		}else
			flipY = !role.region.isFlipY();
		role.region.flip(flipX, flipY);

		//计算翻转与缩放后实际位置
		TextureRegion region = role.region;
		role.roleBound.set(role.region.getRegionWidth(), role.region.getRegionHeight());
		flipFootOffset.set(role.footOffset.x, role.footOffset.y);
		if(region.isFlipX())flipFootOffset.x = role.roleBound.x-role.footOffset.x;
		if(region.isFlipY())flipFootOffset.y = role.roleBound.y-role.footOffset.y;
		flipFootOffset.scl(role.roleScl);
		float pivotX = role.pivotPos.x - flipFootOffset.x;
		float pivotY = role.pivotPos.y - flipFootOffset.y;
		float roleWidth = role.roleBound.x * role.roleScl;
		float roleHeight = role.roleBound.y * role.roleScl;
		//绘制图像
		batch.draw(region, pivotX, pivotY, roleWidth, roleHeight);
	}
}
