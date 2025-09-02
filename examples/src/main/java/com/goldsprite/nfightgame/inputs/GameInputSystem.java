package com.goldsprite.nfightgame.inputs;

import com.badlogic.gdx.Input;
import com.goldsprite.nfightgame.ecs.components.basics.EntityInputManagerComponent;
import com.goldsprite.nfightgame.inputs.widgets.VirtualButton;
import com.goldsprite.nfightgame.inputs.widgets.VirtualJoystick;
import com.goldsprite.utils.math.Vector2;

public class GameInputSystem extends InputSystem {
	private static GameInputSystem instance;
	private InputActions inputActions;

	private VirtualJoystickBinding joystickBinding;

	public void setVirtualJoystick(VirtualJoystick joystick) {
		joystickBinding.setJoystick(joystick);
	}
	private VirtualButtonBinding attackVirButtonBinding,
		jumpVirButtonBinding,
		crouchVirButtonBinding,
		speedBoostVirButtonBinding,
		hurtVirButtonBinding,
		respawnVirButtonBinding,
		changeRoleVirButtonBinding;

	public void setAttackVirButton(VirtualButton virtualButton) {
		attackVirButtonBinding.setVirtualButton(virtualButton);
	}
	public void setJumpVirButton(VirtualButton virtualButton) {
		jumpVirButtonBinding.setVirtualButton(virtualButton);
	}
	public void setCrouchVirButton(VirtualButton virtualButton) {
		crouchVirButtonBinding.setVirtualButton(virtualButton);
	}
	public void setSpeedBoostVirButton(VirtualButton virtualButton) {
		speedBoostVirButtonBinding.setVirtualButton(virtualButton);
	}
	public void setHurtVirButton(VirtualButton virtualButton) {
		hurtVirButtonBinding.setVirtualButton(virtualButton);
	}
	public void setRespawnVirButton(VirtualButton virtualButton) {
		respawnVirButtonBinding.setVirtualButton(virtualButton);
	}
	public void setChangeRoleVirButton(VirtualButton virtualButton) {
		changeRoleVirButtonBinding.setVirtualButton(virtualButton);
	}

	public static GameInputSystem getInstance() {
		if(instance == null) {
			instance = new GameInputSystem();
		}
		return instance;
	}

	private GameInputSystem() {
		initInputActions();
	}

	private void initInputActions() {
		//创建按键行为表实例
		inputActions = new InputActions();
		setInputActions(inputActions);

		//创建按键行为并添加到表
		InputAction moveAction = new InputAction();
		moveAction.setValueType(Vector2.class);
		Vector2KeyBinding WASD = new Vector2KeyBinding();
		WASD.Up = new KeyBinding(Input.Keys.W);
		WASD.Down = new KeyBinding(Input.Keys.S);
		WASD.Left = new KeyBinding(Input.Keys.A);
		WASD.Right = new KeyBinding(Input.Keys.D);
		moveAction.addInputBinding(WASD);
		Vector2KeyBinding FourArrowKey = new Vector2KeyBinding();
		FourArrowKey.Up = new KeyBinding(Input.Keys.UP);
		FourArrowKey.Down = new KeyBinding(Input.Keys.DOWN);
		FourArrowKey.Left = new KeyBinding(Input.Keys.LEFT);
		FourArrowKey.Right = new KeyBinding(Input.Keys.RIGHT);
		moveAction.addInputBinding(FourArrowKey);
		joystickBinding = new VirtualJoystickBinding();
		moveAction.addInputBinding(joystickBinding);
		inputActions.addAction("Move", moveAction);

		InputAction attackAction = new InputAction();
		attackAction.setValueType(Boolean.class);
		KeyBinding keyAttack1 = new KeyBinding(Input.Keys.J);
		attackAction.addInputBinding(keyAttack1);
		attackVirButtonBinding = new VirtualButtonBinding();
		attackAction.addInputBinding(attackVirButtonBinding);
		inputActions.addAction("Attack", attackAction);

		InputAction jumpAction = new InputAction();
		jumpAction.setValueType(Boolean.class);
		KeyBinding keyJump1 = new KeyBinding(Input.Keys.K);
		jumpAction.addInputBinding(keyJump1);
		KeyBinding keyJump2 = new KeyBinding(Input.Keys.SPACE);
		jumpAction.addInputBinding(keyJump2);
		jumpVirButtonBinding = new VirtualButtonBinding();
		jumpAction.addInputBinding(jumpVirButtonBinding);
		inputActions.addAction("Jump", jumpAction);

		InputAction crouchAction = new InputAction();
		crouchAction.setValueType(Boolean.class);
		KeyBinding keyCrouch1 = new KeyBinding(Input.Keys.CONTROL_LEFT);
		crouchAction.addInputBinding(keyCrouch1);
		crouchVirButtonBinding = new VirtualButtonBinding();
		crouchAction.addInputBinding(crouchVirButtonBinding);
		inputActions.addAction("Crouch", crouchAction);

		InputAction speedBoostAction = new InputAction();
		speedBoostAction.setValueType(Boolean.class);
		KeyBinding keySpeedBoost1 = new KeyBinding(Input.Keys.SHIFT_LEFT);
		speedBoostAction.addInputBinding(keySpeedBoost1);
		speedBoostVirButtonBinding = new VirtualButtonBinding();
		speedBoostAction.addInputBinding(speedBoostVirButtonBinding);
		inputActions.addAction("SpeedBoost", speedBoostAction);

		InputAction hurtAction = new InputAction();
		hurtAction.setValueType(Boolean.class);
		KeyBinding keyHurt1 = new KeyBinding(Input.Keys.Y);
		hurtAction.addInputBinding(keyHurt1);
		hurtVirButtonBinding = new VirtualButtonBinding();
		hurtAction.addInputBinding(hurtVirButtonBinding);
		inputActions.addAction("Hurt", hurtAction);

		InputAction respawnAction = new InputAction();
		respawnAction.setValueType(Boolean.class);
		KeyBinding keyRespawn1 = new KeyBinding(Input.Keys.R);
		respawnAction.addInputBinding(keyRespawn1);
		respawnVirButtonBinding = new VirtualButtonBinding();
		respawnAction.addInputBinding(respawnVirButtonBinding);
		inputActions.addAction("Respawn", respawnAction);

		InputAction changeRoleAction = new InputAction();
		changeRoleAction.setValueType(Boolean.class);
		KeyBinding keyChangeRole1 = new KeyBinding(Input.Keys.R);
		changeRoleAction.addInputBinding(keyChangeRole1);
		changeRoleVirButtonBinding = new VirtualButtonBinding();
		changeRoleAction.addInputBinding(changeRoleVirButtonBinding);
		inputActions.addAction("ChangeRole", changeRoleAction);
	}

	public InputAction getInputAction(String name) {
		return inputActions.getAction(name);
	}
}
