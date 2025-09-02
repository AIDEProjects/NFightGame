package com.goldsprite.nfightgame.ecs.components.fsm.fsms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.goldsprite.gdxcore.ecs.component.*;
import com.goldsprite.nfightgame.ecs.components.basics.EntityComponent;
import com.goldsprite.nfightgame.ecs.components.basics.EntityInputManagerComponent;
import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IState;
import com.goldsprite.nfightgame.ecs.components.fsm.interfaces.IEntityFsm;
import com.goldsprite.nfightgame.ecs.components.fsm.states.RespawnState;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class EntityFsmComponent<F extends IEntityFsm, S extends IState> extends Component implements IEntityFsm {
	protected Map<Class<? extends IState>, S> states = new LinkedHashMap<>();
	protected Class<? extends IState> current;
	protected EntityComponent ent;
	protected AnimatorComponent anim;
	protected RigidbodyComponent rigi;
	protected EntityInputManagerComponent inputs;
	private RectColliderComponent bodyCollider;
	private ColliderComponent footTrigger;
	private ColliderComponent attackTrigger;
	private float key_dirX;
	private boolean keyJump, key_crouch, key_attack, key_speedBoost;
	private boolean moveKeyProtect;
	private float beHurt_damage;
	private boolean key_hurt;
	protected boolean resetAnim;
	protected boolean isEnableInput = false;//启用禁用玩家输入

	//Component Area
	public void init() {
		//初始化依赖配置
		initMachine();

		initStates();
	}

	protected void initMachine() {
		ent = getComponent(EntityComponent.class);
		if(ent == null) throw new RuntimeException("ent is null");
		anim = getComponent(AnimatorComponent.class);
		if(anim == null) throw new RuntimeException("anim is null");
		rigi = getComponent(RigidbodyComponent.class);
		if(rigi == null) throw new RuntimeException("rigi is null");
		inputs = getComponent(EntityInputManagerComponent.class);
	}

	@Override
	public void update(float delta) {
		if(isEnableInput) inputHandle();

		running(delta);
	}

	@Override
	public EntityComponent getEnt() {
		return ent;
	}
	@Override
	public AnimatorComponent getAnim() {
		return anim;
	}
	@Override
	public RigidbodyComponent getRigi() {
		return rigi;
	}
	@Override
	public EntityInputManagerComponent getInputs() {
		return inputs;
	}

	@Override
	public boolean isFalling() {
		return rigi.getVelocity().y < -100f;
	}
	@Override
	public boolean isGround() {
		return footTrigger.isCollision();
	}

	@Override
	public ColliderComponent getFootTrigger() {
		return footTrigger;
	}
	@Override
	public RectColliderComponent getBodyCollider() {
		return bodyCollider;
	}

	@Override
	public void setFootTrigger(ColliderComponent footTrigger) {
		this.footTrigger = footTrigger;
	}
	@Override
	public void setBodyCollider(RectColliderComponent bodyCollider) {
		this.bodyCollider = bodyCollider;
	}
	@Override
	public ColliderComponent getAttackTrigger() {
		return attackTrigger;
	}
	@Override
	public void setAttackTrigger(ColliderComponent c) {
		attackTrigger = c;

		//设定攻击效果
		attackTrigger.onTriggerEnterListeners.add(victim -> {
			EntityFsmComponent victimEnt = victim.getComponent(EntityFsmComponent.class);
			if(victimEnt == null) return;

			victimEnt.beHurt(5);
			Gdx.app.log("", "伤害");
		});
	}


	//RoleInfoAndController Area
	@Override
	public void move(float dirX) {
		//翻转
		int faceX = (int) Math.signum(dirX);
		if (faceX != 0) transform.setFace(faceX, 1);
		//运动
		float speed = ent.getSpeed() * dirX;
		rigi.getVelocity().setX(speed);
	}
	@Override
	public void respawn() {
		changeState(RespawnState.class);
	}


	//Input Area
	public void inputHandle() {
		//持续左右
//		boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.A);
//		boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.D);
		boolean leftJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.A);
		boolean rightJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.D);
//		key_jump = Gdx.input.isKeyJustPressed(Input.Keys.K);//跳
//		key_crouch = Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT);//蹲
//		key_attack = Gdx.input.isKeyJustPressed(Input.Keys.J);//攻击
//		key_speedBoost = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);//疾跑
//		if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) beHurt(5);//受击(测试
//		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) respawn();//重生(测试

//		if (leftPressed) key_dirX = -1;
//		if (rightPressed) key_dirX = 1;
//		if (!leftPressed && !rightPressed) key_dirX = 0;
		if (moveKeyProtect && (leftJustPressed || rightJustPressed)) moveKeyProtect = false;
	}

	@Override
	public float getKeyDirX() {
		return key_dirX;
	}

	@Override
	public void setKeyDirX(float key_dirX) {
		this.key_dirX = key_dirX;
	}

	@Override
	public boolean getKeyJump() {
		return keyJump;
	}
	@Override
	public void setKeyJump(boolean down) {
		if(!isEnableInput()) return;
		keyJump = down;
	}
	@Override
	public boolean getKeyCrouch() {
		return key_crouch;
	}
	@Override
	public void setKeyCrouch(boolean down) {
		if(!isEnableInput()) return;
		key_crouch = down;
	}
	@Override
	public boolean getKeyAttack() {
		return key_attack;
	}
	@Override
	public void setKeyAttack(boolean down) {
		if(!isEnableInput()) return;
		key_attack = down;
	}
	@Override
	public boolean getKeySpeedBoost() {
		return key_speedBoost;
	}
	@Override
	public void setKeySpeedBoost(boolean down) {
		if(!isEnableInput()) return;
		key_speedBoost = down;
	}

	@Override
	public boolean getMoveKeyProtect() {
		return moveKeyProtect;
	}
	@Override
	public void setMoveKeyProtect(boolean moveKeyProtect) {
		this.moveKeyProtect = moveKeyProtect;
	}
	@Override
	public void beHurt(float damage) {
		key_hurt = true;
		beHurt_damage = damage;
	}
	@Override
	public void consumeHurtKey() {
		key_hurt = false;
	}

	@Override
	public boolean getKeyHurt() {
		return key_hurt;
	}
	@Override
	public void setKeyHurt(boolean down) {
		if(!isEnableInput()) return;
		key_hurt = down;
	}

	@Override
	public float getBeHurt_damage() {
		return beHurt_damage;
	}
	public boolean isEnableInput() {
		return isEnableInput;
	}
	public void setEnableInput(boolean enableInput) {
		isEnableInput = enableInput;
	}


	//State Area
	public void initStates() {}
	public void running(float delta){
		getCurrentState().running(delta);
	}
	@Override
	public S getCurrentState(){
		return states.get(current);
	}

	@Override
	public void changeState(Class<? extends IState> key){
		changeState(key, true);
	}

	@Override
	public <T extends IState> T getState(Class<T> key) {
		return (T)states.get(key);
	}

	@Override
	public void changeState(Class<? extends IState> key, boolean resetAnim){
		if(!states.containsKey(key)) return;
		Gdx.app.log(getClass().getSimpleName(), "changeState "+current.getSimpleName()+" -> "+key.getSimpleName() +" "+ new Random().nextInt(1000));

		this.resetAnim = resetAnim;
		if(current != null)getCurrentState().exit();
		current = key;
		getCurrentState().enter();
	}

	@Override
	public void addState(IState state) {
		if(current == null) current = state.getClass();
		states.put(state.getClass(), (S)state);
		state.setFsm(this);
	}

	@Override
	public boolean isResetAnim() {
		return resetAnim;
	}
}
