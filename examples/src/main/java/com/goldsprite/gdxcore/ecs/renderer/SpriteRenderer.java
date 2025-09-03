package com.goldsprite.gdxcore.ecs.renderer;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goldsprite.gdxcore.ecs.component.WorldTxtComponent;
import com.goldsprite.gdxcore.ecs.interfaces.IComponent;
import com.goldsprite.gdxcore.ecs.component.SpriteComponent;
import com.goldsprite.utils.math.Vector2;

public class SpriteRenderer extends Renderer {
	private final SpriteBatch batch;

	public SpriteRenderer(){
		batch = new SpriteBatch();
	}

	@Override
	public void update(float delta){
		if(!isEnabled()) return;

		batch.setProjectionMatrix(gm.getCamera().combined);
		batch.begin();
		for(IComponent component : gobjects){
			//绘制精灵
			if(component instanceof SpriteComponent){
				SpriteComponent sprite = (SpriteComponent)component;
				TextureRegion region = sprite.getRegion();
				//跳过未启用与未初始化精灵
				if(!sprite.isEnable() || SpriteComponent.emptyRegion.equals(region)) continue;
				//应用精灵翻转
				sprite.updateRegionFlip();
				//绘制: 中心转左下原点坐标, 计算拉伸
				Vector2 leftDownPos = sprite.getLeftDownPos();
				Vector2 renderSize = sprite.getRenderSize();
				batch.draw(region, leftDownPos.x, leftDownPos.y, renderSize.x, renderSize.y);
			}
			//绘制世界文字
			else if(component instanceof WorldTxtComponent){
				WorldTxtComponent txt = (WorldTxtComponent)component;
				BitmapFont fnt = txt.getFont();
				String text = txt.getText();
				//跳过未启用与未初始化文字
				if(!txt.isEnable() || fnt == null) continue;
				//绘制: 中心转左下原点坐标, 计算拉伸
				Vector2 leftDownPos = txt.getLeftDownPos();
				fnt.draw(batch, text, leftDownPos.x, leftDownPos.y);
			}
		}
		batch.end();
	}
}
