package com.goldsprite.nfightgame.core.ecs.system.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goldsprite.nfightgame.core.ecs.GObject;
import com.goldsprite.nfightgame.core.ecs.component.SpriteComponent;
import com.goldsprite.utils.math.Vector2;
import com.goldsprite.nfightgame.core.ecs.component.*;

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
			SpriteComponent texComp = (SpriteComponent)component;
			TextureRegion region = texComp.getRegion();
			if(SpriteComponent.emptyRegion.equals(region)) continue;
			texComp.updateRegionFlip();
			Vector2 leftDownPos = texComp.getLeftDownPos();
			Vector2 renderSize = texComp.getRenderSize();
			batch.draw(region, leftDownPos.x, leftDownPos.y, renderSize.x, renderSize.y);
		}
		batch.end();
	}
}
