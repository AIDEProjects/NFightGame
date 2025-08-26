package com.goldsprite.nfightgame.core;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AnimatorComponent extends Component {
	private final TextureComponent texComp;
	public HashMap<String, Animation<TextureRegion>> anims = new HashMap<>();
	public float stateTime;
	public Animation<TextureRegion> current;

	public AnimatorComponent(TextureComponent texComp){
		this.texComp = texComp;
	}

	public void setCurAnim(String animName){
		stateTime = 0;
		current = anims.get(animName);
	}
	public void addAnim(String animName, Animation<TextureRegion> anim){
		if(current == null) current = anim;
		anims.put(animName, anim);
	}

	@Override
	public void act(float delta) {
		step(delta);
	}

	public void step(float delta){
		texComp.setRegion((TextureRegion) current.getKeyFrame(stateTime));
		stateTime += delta;
	}
}
