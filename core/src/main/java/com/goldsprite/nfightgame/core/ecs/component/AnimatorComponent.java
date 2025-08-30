package com.goldsprite.nfightgame.core.ecs.component;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.*;

public class AnimatorComponent extends Component {
	private final SpriteComponent texComp;
	public Map<Enum, Animation<TextureRegion>> anims = new LinkedHashMap<>();
	public float stateTime;
	public Enum current;

	public AnimatorComponent(SpriteComponent texComp){
		this.texComp = texComp;
	}

	public void setCurAnim(Enum key){
		setCurAnim(key, true);
	}
	public void setCurAnim(Enum key, boolean reset){
		if(!current.equals(key) && reset) stateTime = 0;
		current = key;
	}

	public void addAnim(Enum key, Animation<TextureRegion> anim){
		if(current == null) current = key;
		anims.put(key, anim);
	}

	@Override
	public void update(float delta) {
		step(delta);
	}

	public void step(float delta){
		texComp.setRegion(getAnim(current).getKeyFrame(stateTime));
		stateTime += delta;
	}

	public Animation<TextureRegion> getAnim(Enum key) {
		return anims.get(key);
	}

	public boolean isAnim(Enum key) {
		return anims.containsKey(key) && current.equals(key);
	}
	public Animation<TextureRegion> getCurrentAnim(){
		return getAnim(current);
	}
	public boolean isFinished() {
		return getAnim(current).isAnimationFinished(stateTime);
	}
}
