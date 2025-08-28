package com.goldsprite.nfightgame.core.ecs.component;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goldsprite.utils.math.Vector2;
import com.goldsprite.utils.math.Vector2Int;

public class SpriteComponent extends Component {
	public static final TextureRegion emptyRegion = new TextureRegion();
	private TextureRegion region = emptyRegion;//材质
	private Vector2 size = new Vector2(1, 1);//大小
	private Vector2 scale = new Vector2(1, 1);
	private Vector2Int originOffset = new Vector2Int();//原点距左下偏移
	private Vector2Int spriteFace = new Vector2Int(1, 1);
	private Vector2Int realFace = new Vector2Int(1, 1);

	public TextureRegion getRegion() {
		return region;
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
		getTextureSize().set(region.getRegionWidth(), region.getRegionHeight());
	}

	public Vector2Int getSpriteFace() {
		return spriteFace;
	}
	public void setSpriteFace(int spriteFaceX, int spriteFaceY) {
		spriteFace.set(spriteFaceX, spriteFaceY);
	}

	public Vector2Int getRealFace() {
		return (Vector2Int) realFace.set(transform.getFace()).scl(spriteFace);
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

	public void updateRegionFlip() {
		updateRegionFlip(getRegion());
	}
	public void updateRegionFlip(TextureRegion region) {
		//翻转人物
		boolean flipX, flipY;
		if(getRealFace().x >= 0){
			flipX = region.isFlipX();
		}else
			flipX = !region.isFlipX();
		if(getRealFace().y >= 0){
			flipY = region.isFlipY();
		}else
			flipY = !region.isFlipY();
		region.flip(flipX, flipY);
	}

	private Vector2 flipOriginOffset = new Vector2();
	private Vector2 leftDownPos = new Vector2();
	public Vector2 getLeftDownPos() {
		//计算翻转实际偏移
		flipOriginOffset.set(originOffset.x, originOffset.y);
		if(region.isFlipX()) flipOriginOffset.x = getTextureSize().x-originOffset.x;
		if(region.isFlipY()) flipOriginOffset.y = getTextureSize().y-originOffset.y;
		flipOriginOffset.scl(scale).scl(transform.getScale());
		//返回左下位置
		return leftDownPos.set(transform.getPosition().x - getFlipOriginOffset().x, transform.getPosition().y - getFlipOriginOffset().y);
	}

	private Vector2 renderSize = new Vector2();
	public Vector2 getRenderSize() {
		return renderSize.set(getTextureSize()).scl(scale).scl(transform.getScale());
	}

	public Vector2 getTextureSize() {
		return size;
	}

	public Vector2 getScale(){
		return scale;
	}

	public void setScale(float sclX, float sclY) {
		this.scale.set(sclX, sclY);
	}

	public void setSize(float sizeX, float sizeY) {
		this.size.set(sizeX, sizeY);
	}
}
