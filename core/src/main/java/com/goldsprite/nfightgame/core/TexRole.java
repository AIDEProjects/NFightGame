package com.goldsprite.nfightgame.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goldsprite.utils.math.Vector2;
import com.goldsprite.utils.math.Vector2Int;

public class TexRole {
	public Texture roleTex;
	public TextureRegion region;
	public Vector2Int texSplit;
	public Vector2 pos = new Vector2(200, 250);//位置
	public Vector2Int face = new Vector2Int(1, 1);//人物朝向(-1左下1右上
	public Vector2 roleBound = new Vector2();//角色原始大小
	public float roleScl = 2f;//角色缩放
	public Vector2Int originOffset = new Vector2Int(119, 36);
	public Vector2Int atkOffset = new Vector2Int(53, 60);//53, 60//172, 96
	public Vector2 flipAtkOffset = new Vector2();
	public Vector2 sclAtkOffset = new Vector2();
	public float atkRange = 24;

	public TexRole() {
	}

	public void initTex(){
		roleTex = new Texture(Gdx.files.internal("hero/hero_sheet.png"));
		texSplit = new Vector2Int(256, 256);
		region = new TextureRegion(roleTex, 0, 0, texSplit.x, texSplit.y);
	}

	public TextureRegion getRegion(){
		return region;
	}

	public void act(float delta){
		roleBound.set(getRegion().getRegionWidth(), getRegion().getRegionHeight());
		flipRole();
	}

	public void flipRole(){
		//翻转人物
		boolean flipX, flipY;
		if(face.x >= 0){
			flipX = getRegion().isFlipX();
		}else
			flipX = !getRegion().isFlipX();
		if(face.y >= 0){
			flipY = getRegion().isFlipY();
		}else
			flipY = !getRegion().isFlipY();
		getRegion().flip(flipX, flipY);
	}

	Vector2 leftDownPos = new Vector2();
	Vector2 sclSize = new Vector2();
	public Vector2 flipOriginOffset = new Vector2();//脚底实际偏移
	public void draw(SpriteBatch batch) {
		//绘制图像
		batch.draw(region, getLeftDownPos().x, getLeftDownPos().y, getSclSize().x, getSclSize().y);
	}

	public Vector2 getSclSize() {
		return sclSize.set(roleBound.x * roleScl, roleBound.y * roleScl);
	}

	public Vector2 getFlipOriginOffset(){
		//计算翻转实际偏移
		flipOriginOffset.set(originOffset.x, originOffset.y);
		if(region.isFlipX()) flipOriginOffset.x = roleBound.x-originOffset.x;
		if(region.isFlipY()) flipOriginOffset.y = roleBound.y-originOffset.y;
		flipOriginOffset.scl(roleScl);
		return flipOriginOffset;
	}
	public Vector2 getLeftDownPos() {
		//返回左下位置
		return leftDownPos.set(pos.x - getFlipOriginOffset().x, pos.y - getFlipOriginOffset().y);
	}

	Vector2 atkPos = new Vector2();
	public Vector2 getFlipAtkOffset(){
		flipAtkOffset.set(atkOffset.x * (region.isFlipX()?-1:1), atkOffset.y * (region.isFlipY()?-1:1));
		return flipAtkOffset.scl(roleScl);
	}
	
	public Vector2 getAtkPos(){
		return atkPos.set(pos.x+getFlipAtkOffset().x, pos.y + getFlipAtkOffset().y);
	}

}
