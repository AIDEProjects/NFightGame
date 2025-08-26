/**
 * @Author 
 * @AIDE AIDE+
*/
package com.goldsprite.nfightgame.core;
import com.badlogic.gdx.graphics.g2d.*;
import java.util.*;

public class AnimatorRole extends TexRole{
	public HashMap<String, Animation> anims = new HashMap<>();
	public float stateTime;
	public Animation current;
	
	public void setCurAnim(String animName){
		stateTime = 0;
		current = anims.get(animName);
	}
	public void addAnim(String animName, Animation anim){
		if(current == null) current = anim;
		anims.put(animName, anim);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
	}

	public void step(float delta){
		stateTime += delta;
	}

	@Override
	public TextureRegion getRegion()
	{
		return region = (TextureRegion)current.getKeyFrame(stateTime);
	}
}
