package com.goldsprite.nfightgame.core.ecs.component;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.*;

public class AnimatorComponent extends Component {
	private final SpriteComponent texComp;
	public Map<String, Animation<TextureRegion>> anims = new LinkedHashMap<>();
	public float stateTime;
	public String current;

	public AnimatorComponent(SpriteComponent texComp){
		this.texComp = texComp;
	}

	public void setCurAnim(String animName){
		setCurAnim(animName, true);
	}
	public void setCurAnim(String animName, boolean reset){
		if(!current.equals(animName) && reset) stateTime = 0;
		current = animName;
	}

	public void addAnim(String animName, Animation<TextureRegion> anim){
		if(current == null) current = animName;
		anims.put(animName, anim);
	}

	@Override
	public void update(float delta) {
		step(delta);
	}

	public void step(float delta){
		texComp.setRegion(getAnim(current).getKeyFrame(stateTime));
		stateTime += delta;
	}

	private Animation<TextureRegion> getAnim(String animName) {
		return anims.get(animName);
	}

	public boolean isAnim(String animName) {
		return anims.containsKey(animName) && current.equals(animName);
	}
}
