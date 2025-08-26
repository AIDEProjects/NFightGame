/**
 * @Author
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core;
import com.badlogic.gdx.graphics.g2d.*;

public class AnimRole extends TexRole{
	public Animation<TextureRegion> anim;
	public float stateTime;

	public void setFrames(float frameDuration, Object[] regions, boolean loop){
		anim = new Animation(frameDuration, regions);
		anim.setPlayMode(loop?Animation.PlayMode.LOOP:Animation.PlayMode.NORMAL);
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		step(delta);
	}

	public void step(float delta){
		stateTime += delta;
	}

	@Override
	public TextureRegion getRegion()
	{
		return region = (TextureRegion)anim.getKeyFrame(stateTime);
	}

}

