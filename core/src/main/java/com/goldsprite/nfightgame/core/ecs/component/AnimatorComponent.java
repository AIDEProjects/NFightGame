package com.goldsprite.nfightgame.core.ecs.component;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AnimatorComponent extends Component {
	private final SpriteComponent texComp;
	public HashMap<String, Animation<TextureRegion>> anims = new HashMap<>();
	public float stateTime;
	public String current;

	public AnimatorComponent(SpriteComponent texComp){
		this.texComp = texComp;
	}

	public void setCurAnim(String animName){
		if(!current.equals(animName)) stateTime = 0;
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
}
