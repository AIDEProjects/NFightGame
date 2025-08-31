package com.goldsprite.nfightgame.core.components.fsms;

import com.badlogic.gdx.Gdx;
import com.goldsprite.nfightgame.core.components.EntityComponent;
import com.goldsprite.nfightgame.core.ecs.component.AnimatorComponent;
import com.goldsprite.nfightgame.core.ecs.component.Component;
import com.goldsprite.nfightgame.core.ecs.component.RigidbodyComponent;
import com.goldsprite.nfightgame.core.fsm.IFsm;
import com.goldsprite.nfightgame.core.fsm.IState;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class EntityFsmComponent<F extends IFsm, S extends IState> extends Component implements IFsm {
	protected Map<Class<? extends IState>, S> states = new LinkedHashMap<>();
	protected Class<? extends IState> current;
	protected EntityComponent ent;
	protected AnimatorComponent anim;
	protected RigidbodyComponent rigi;
	private float beHurt_damage;
	private boolean key_hurt;
	protected boolean resetAnim;

	//Component Area
	public void init() {
		//初始化依赖配置
		initMachine();

		initStates();
	}

	protected void initMachine() {
		ent = getComponent(EntityComponent.class);
		anim = getComponent(AnimatorComponent.class);
		rigi = getComponent(RigidbodyComponent.class);
	}

	@Override
	public void update(float delta) {
		inputHandle();

		running(delta);
	}

	public EntityComponent getEnt() {
		return ent;
	}
	public AnimatorComponent getAnim() {
		return anim;
	}
	public RigidbodyComponent getRigi() {
		return rigi;
	}


	//Input Area
	public void inputHandle() {}

	public void beHurt(float damage) {
		key_hurt = true;
		beHurt_damage = damage;
	}

	public void consumeHurtKey() {
		key_hurt = false;
	}

	public boolean getKeyHurt() {
		return key_hurt;
	}

	public float getBeHurt_damage() {
		return beHurt_damage;
	}


	//State Area
	public void initStates() {}
	public void running(float delta){
		getCurrentState().running(delta);
	}

	public S getCurrentState(){
		return states.get(current);
	}

	public void changeState(Class<? extends S> key){
		changeState(key, true);
	}
	public void changeState(Class<? extends S> key, boolean resetAnim){
		this.resetAnim = resetAnim;
		Gdx.app.log(getClass().getSimpleName(), "changeState "+current.getSimpleName()+" -> "+key.getSimpleName() +" "+ new Random().nextInt(1000));
//		Gdx.app.log("", "bodyIsCollision: "+bodyCollider.isCollision());
		if(current != null)getCurrentState().exit();
		current = key;
		getCurrentState().enter();
	}

	public void addState(S state) {
		if(current == null) current = state.getClass();
		states.put(state.getClass(), state);
		state.setFsm(this);
	}

	public boolean isResetAnim() {
		return resetAnim;
	}
}
