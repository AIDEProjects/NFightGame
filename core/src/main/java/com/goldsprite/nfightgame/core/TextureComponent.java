package com.goldsprite.nfightgame.core;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goldsprite.utils.math.Vector2;
import com.goldsprite.utils.math.Vector2Int;

public class TextureComponent implements IComponent {
	private TextureRegion region;//材质
	private Vector2 position = new Vector2();//原点位置
	private Vector2Int face = new Vector2Int(1, 1);//朝向
	private Vector2 size = new Vector2(1, 1);//大小
	private Vector2 scale = new Vector2(1, 1);//缩放
	private Vector2Int originOffset = new Vector2Int();//原点距左下偏移

	public TextureRegion getRegion() {
		return region;
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
		size.set(region.getRegionWidth(), region.getRegionHeight());
	}


	public Vector2 getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale.set(scale);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}

	public Vector2Int getOriginOffset() {
		return originOffset;
	}

	public void setOriginOffset(float offsetX, float offsetY) {
		this.originOffset.set(offsetX, offsetY);
	}

	public Vector2 getFlipOriginOffset(){
		return flipOriginOffset;
	}

	public void updateRegionFlip(){
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

	private Vector2 flipOriginOffset = new Vector2();
	private Vector2 leftDownPos = new Vector2();
	public Vector2 getLeftDownPos() {
		//计算翻转实际偏移
		flipOriginOffset.set(originOffset.x, originOffset.y);
		if(region.isFlipX()) flipOriginOffset.x = size.x-originOffset.x;
		if(region.isFlipY()) flipOriginOffset.y = size.y-originOffset.y;
		flipOriginOffset.scl(scale);
		//返回左下位置
		return leftDownPos.set(position.x - getFlipOriginOffset().x, position.y - getFlipOriginOffset().y);
	}

	private Vector2 renderSize = new Vector2();
	public Vector2 getRenderSize() {
		return renderSize.set(size).scl(scale);
	}

	public Vector2Int getFace() {
		return face;
	}

	public void setFace(int faceX, int faceY) {
		face.set(faceX, faceY);
	}
}
