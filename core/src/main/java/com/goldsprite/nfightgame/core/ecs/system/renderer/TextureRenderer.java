package com.goldsprite.nfightgame.core.ecs.system.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.TextureComponent;
import com.goldsprite.utils.math.Vector2;

public class TextureRenderer extends Renderer {
	private final SpriteBatch batch;

	public TextureRenderer(){
		batch = new SpriteBatch();
	}

	@Override
	public void update(float delta){
		batch.setProjectionMatrix(gm.getCamera().combined);
		batch.begin();
		for(GObject gobject : gobjects){
			TextureComponent texComp = gobject.getComponent(TextureComponent.class);
			TextureRegion region = texComp.getRegion();
			if(TextureComponent.emptyRegion.equals(region)) continue;
			texComp.updateRegionFlip();
			Vector2 leftDownPos = texComp.getLeftDownPos();
			Vector2 renderSize = texComp.getRenderSize();
			batch.draw(region, leftDownPos.x, leftDownPos.y, renderSize.x, renderSize.y);
		}
		batch.end();
	}
}
